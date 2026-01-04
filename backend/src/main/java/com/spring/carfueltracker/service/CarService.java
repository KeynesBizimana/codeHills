package com.spring.carfueltracker.service;

import com.spring.carfueltracker.model.Car;
import com.spring.carfueltracker.model.FuelEntry;
import com.spring.carfueltracker.model.FuelStats;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CarService {

    private final Map<Long, Car> cars = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Car createCar(String brand, String model, Integer year) {
        Long id = idCounter.getAndIncrement();
        Car car = new Car(id, brand, model, year);
        cars.put(id, car);
        return car;
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars.values());
    }

    public Optional<Car> getCarById(Long id) {
        return Optional.ofNullable(cars.get(id));
    }

    public boolean addFuelEntry(Long carId, FuelEntry fuelEntry) {
        Car car = cars.get(carId);
        if (car == null) return false;
        car.addFuelEntry(fuelEntry);
        return true;
    }

    public Optional<FuelStats> getFuelStats(Long carId) {
        Car car = cars.get(carId);
        if (car == null) return Optional.empty();

        List<FuelEntry> entries = car.getFuelEntries();
        if (entries.isEmpty()) {
            return Optional.of(new FuelStats(0.0, 0.0, 0.0));
        }

        double totalFuel = 0.0;
        double totalCost = 0.0;
        for (FuelEntry entry : entries) {
            totalFuel += entry.getLiters();
            totalCost += entry.getPrice();
        }

        double averageConsumption = 0.0;
        if (entries.size() >= 2) {
            int firstOdometer = entries.get(0).getOdometer();
            int lastOdometer = entries.get(entries.size() - 1).getOdometer();
            int distance = lastOdometer - firstOdometer;
            if (distance > 0) {
                averageConsumption = (totalFuel / distance) * 100;
            }
        }

        return Optional.of(new FuelStats(totalFuel, totalCost, averageConsumption));
    }
}