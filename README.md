# codeHills

# Car Fuel Tracker

A multi-module Spring Boot application for managing cars and tracking fuel consumption with a command-line interface.

## 🏗️ Project Architecture

The system consists of two main components:

- **Backend Server**: Spring Boot REST API with in-memory storage
- **CLI Client**: Standalone Java application that communicates with the backend via HTTP

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- Git

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/car-fuel-tracker.git
cd car-fuel-tracker
```

### Build the Project

```bash
mvn clean install
```

## 🖥️ Running the Application

### 1. Start the Backend Server

```bash
cd backend
mvn spring-boot:run
```

The server will start on `http://localhost:8080`

**Or in IntelliJ IDEA:**
- Navigate to `backend/src/main/java/com/spring/carfueltracker/BackendApplication.java`
- Right-click and select "Run 'BackendApplication'"

### 2. Run the CLI Client

Open a new terminal window and execute commands:

#### Create a Car
```bash
cd client
mvn exec:java -Dexec.mainClass="com.spring.carfueltracker.cli.CliClientApplication" -Dexec.args="create-car --brand Toyota --model Corolla --year 2018"
```

#### Add Fuel Entry
```bash
mvn exec:java -Dexec.mainClass="com.spring.carfueltracker.cli.CliClientApplication" -Dexec.args="add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000"
```

#### Get Fuel Statistics
```bash
mvn exec:java -Dexec.mainClass="com.spring.carfueltracker.cli.CliClientApplication" -Dexec.args="fuel-stats --carId 1"
```

## 📡 API Endpoints

### REST API Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/api/cars` | Create a new car | `{"brand":"Toyota","model":"Corolla","year":2018}` |
| GET | `/api/cars` | List all cars | N/A |
| POST | `/api/cars/{id}/fuel` | Add fuel entry | `{"liters":40,"price":52.5,"odometer":45000}` |
| GET | `/api/cars/{id}/fuel/stats` | Get fuel statistics | N/A |

### Servlet Endpoint

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/servlet/fuel-stats?carId={id}` | Get fuel statistics (Manual Servlet) |

**Example:**
```
http://localhost:8080/servlet/fuel-stats?carId=1
```

## 🧪 Testing the Application

### Complete Test Flow

1. **Start the Backend** (keep it running)

2. **Create a car:**
   ```bash
   create-car --brand Honda --model Civic --year 2020
   ```
   Expected output: Car created with ID 1

3. **Add first fuel entry:**
   ```bash
   add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
   ```

4. **Add second fuel entry:**
   ```bash
   add-fuel --carId 1 --liters 45 --price 60.0 --odometer 45500
   ```

5. **Get statistics:**
   ```bash
   fuel-stats --carId 1
   ```
   Expected output:
   ```
   Total fuel: 85.0 L
   Total cost: 112.50
   Average consumption: 17.0 L/100km
   ```

6. **Test the Servlet** (in browser):
   ```
   http://localhost:8080/servlet/fuel-stats?carId=1
   ```

### Using cURL

**Create a car:**
```bash
curl -X POST http://localhost:8080/api/cars \
  -H "Content-Type: application/json" \
  -d '{"brand":"Toyota","model":"Corolla","year":2018}'
```

**Add fuel:**
```bash
curl -X POST http://localhost:8080/api/cars/1/fuel \
  -H "Content-Type: application/json" \
  -d '{"liters":40,"price":52.5,"odometer":45000}'
```

**Get statistics:**
```bash
curl http://localhost:8080/api/cars/1/fuel/stats
```

## 📁 Project Structure

```
Car-fuel-tracker/
├── backend/                        # Backend Spring Boot module
│   ├── src/main/java/
│   │   └── com/spring/carfueltracker/
│   │       ├── BackendApplication.java
│   │       ├── controller/
│   │       │   └── CarController.java
│   │       ├── servlet/
│   │       │   └── FuelStatsServlet.java
│   │       ├── service/
│   │       │   └── CarService.java
│   │       └── model/
│   │           ├── Car.java
│   │           ├── FuelEntry.java
│   │           └── FuelStats.java
│   └── pom.xml
├── client/                         # CLI Client module
│   ├── src/main/java/
│   │   └── com/spring/carfueltracker/cli/
│   │       ├── CliClientApplication.java
│   │       └── service/
│   │           └── ApiClient.java
│   └── pom.xml
└── pom.xml                         # Parent POM
```

## 🔑 Key Features

### Backend
- ✅ RESTful API with Spring Boot
- ✅ In-memory storage (HashMap)
- ✅ Manual Java Servlet implementation
- ✅ Proper error handling (404 for invalid IDs)
- ✅ Service layer reuse between REST and Servlet

### CLI Client
- ✅ Command-line argument parsing
- ✅ HTTP communication using `java.net.http.HttpClient`
- ✅ JSON processing with Jackson
- ✅ User-friendly output formatting

### Fuel Statistics Calculation
- **Total Fuel**: Sum of all fuel entries (liters)
- **Total Cost**: Sum of all fuel entry prices
- **Average Consumption**: Calculated as `(totalFuel / distanceTraveled) * 100` L/100km
  - Distance = Last odometer reading - First odometer reading

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3.4.1**
- **Maven**
- **Jackson** (JSON processing)
- **Java HttpClient** (HTTP communication)
- **Jakarta Servlet API**

## ⚠️ Important Notes

- The backend uses **in-memory storage** - all data is lost when the server stops
- No database or authentication is required
- The CLI must be run while the backend server is running
- Default server port is **8080**

## 🐛 Troubleshooting

### Backend won't start
- Check if port 8080 is available
- Ensure Java 21+ is installed: `java -version`

### CLI returns "Connection refused"
- Make sure the backend server is running
- Verify the backend is on `http://localhost:8080`

### "Car not found" error
- Ensure you've created a car first using `create-car`
- Use the correct car ID returned from car creation

## 📝 Assignment Requirements Met

✅ Spring Boot REST API with in-memory storage  
✅ Manual Java Servlet with explicit lifecycle management  
✅ CLI application using HttpClient  
✅ All required endpoints implemented  
✅ Proper error handling (404 for invalid IDs)  
✅ Multi-module Maven project structure  

## 👤 Author

Keynes Bizimana

## 📄 License

This project is created for educational purposes as part of the AEM Academy Technical Assignment.
