package com.spring.carfueltracker.cli;

import com.spring.carfueltracker.cli.service.ApiClient;

public class CliClientApplication {

    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        ApiClient apiClient = new ApiClient(BASE_URL);
        String command = args[0];

        try {
            switch (command) {
                case "create-car":
                    handleCreateCar(args, apiClient);
                    break;
                case "add-fuel":
                    handleAddFuel(args, apiClient);
                    break;
                case "fuel-stats":
                    handleFuelStats(args, apiClient);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    printUsage();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleCreateCar(String[] args, ApiClient apiClient) throws Exception {
        String brand = getArgValue(args, "--brand");
        String model = getArgValue(args, "--model");
        String yearStr = getArgValue(args, "--year");

        if (brand == null || model == null || yearStr == null) {
            System.out.println("Missing required parameters for create-car");
            printUsage();
            return;
        }

        int year = Integer.parseInt(yearStr);
        String response = apiClient.createCar(brand, model, year);
        System.out.println("Car created successfully!");
        System.out.println(response);
    }

    private static void handleAddFuel(String[] args, ApiClient apiClient) throws Exception {
        String carIdStr = getArgValue(args, "--carId");
        String litersStr = getArgValue(args, "--liters");
        String priceStr = getArgValue(args, "--price");
        String odometerStr = getArgValue(args, "--odometer");

        if (carIdStr == null || litersStr == null || priceStr == null || odometerStr == null) {
            System.out.println("Missing required parameters for add-fuel");
            printUsage();
            return;
        }

        long carId = Long.parseLong(carIdStr);
        double liters = Double.parseDouble(litersStr);
        double price = Double.parseDouble(priceStr);
        int odometer = Integer.parseInt(odometerStr);

        String response = apiClient.addFuelEntry(carId, liters, price, odometer);
        System.out.println("Fuel entry added successfully!");
        System.out.println(response);
    }

    private static void handleFuelStats(String[] args, ApiClient apiClient) throws Exception {
        String carIdStr = getArgValue(args, "--carId");

        if (carIdStr == null) {
            System.out.println("Missing required parameter: --carId");
            printUsage();
            return;
        }

        long carId = Long.parseLong(carIdStr);
        String stats = apiClient.getFuelStats(carId);
        System.out.println(stats);
    }

    private static String getArgValue(String[] args, String flag) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(flag)) {
                return args[i + 1];
            }
        }
        return null;
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  create-car --brand <brand> --model <model> --year <year>");
        System.out.println("  add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>");
        System.out.println("  fuel-stats --carId <id>");
    }
}