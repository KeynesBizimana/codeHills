package com.spring.carfueltracker.cli.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public String createCar(String brand, String model, int year) throws Exception {
        String json = String.format(
                "{\"brand\":\"%s\",\"model\":\"%s\",\"year\":%d}",
                brand, model, year
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to create car: " + response.statusCode());
        }
    }

    public String addFuelEntry(long carId, double liters, double price, int odometer) throws Exception {
        String json = String.format(
                "{\"liters\":%.2f,\"price\":%.2f,\"odometer\":%d}",
                liters, price, odometer
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Car not found");
        } else {
            throw new RuntimeException("Failed to add fuel entry: " + response.statusCode());
        }
    }

    public String getFuelStats(long carId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cars/" + carId + "/fuel/stats"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode stats = objectMapper.readTree(response.body());
            double totalFuel = stats.get("totalFuel").asDouble();
            double totalCost = stats.get("totalCost").asDouble();
            double avgConsumption = stats.get("averageConsumption").asDouble();

            return String.format(
                    "Total fuel: %.1f L\nTotal cost: %.2f\nAverage consumption: %.1f L/100km",
                    totalFuel, totalCost, avgConsumption
            );
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Car not found");
        } else {
            throw new RuntimeException("Failed to get stats: " + response.statusCode());
        }
    }
}
