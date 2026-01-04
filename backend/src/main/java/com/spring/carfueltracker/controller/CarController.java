package com.spring.carfueltracker.controller;

import com.spring.carfueltracker.model.Car;
import com.spring.carfueltracker.model.FuelEntry;
import com.spring.carfueltracker.model.FuelStats;
import com.spring.carfueltracker.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        if (car.getBrand() == null || car.getModel() == null || car.getYear() == null) {
            return ResponseEntity.badRequest().build();
        }
        Car created = carService.createCar(car.getBrand(), car.getModel(), car.getYear());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/{id}/fuel")
    public ResponseEntity<String> addFuelEntry(
            @PathVariable(name = "id") Long id,
            @RequestBody FuelEntry entry) {
        if (entry.getLiters() == null || entry.getPrice() == null || entry.getOdometer() == null) {
            return ResponseEntity.badRequest().body("Missing fields");
        }
        boolean success = carService.addFuelEntry(id, entry);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }
        return ResponseEntity.ok("Fuel added");
    }

    @GetMapping("/{id}/fuel/stats")
    public ResponseEntity<FuelStats> getFuelStats(
            @PathVariable(name = "id") Long id) {
        Optional<FuelStats> stats = carService.getFuelStats(id);
        if (stats.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(stats.get());
    }
}