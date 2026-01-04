package com.spring.carfueltracker.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.carfueltracker.model.FuelStats;
import com.spring.carfueltracker.service.CarService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/servlet/fuel-stats")
public class FuelStatsServlet extends HttpServlet {

    private CarService carService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        carService = WebApplicationContextUtils
                .getRequiredWebApplicationContext(getServletContext())
                .getBean(CarService.class);
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String carIdParam = request.getParameter("carId");

        if (carIdParam == null || carIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing carId\"}");
            return;
        }

        try {
            Long carId = Long.parseLong(carIdParam);
            Optional<FuelStats> stats = carService.getFuelStats(carId);

            if (stats.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Car not found\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(stats.get()));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid carId\"}");
        }
    }
}