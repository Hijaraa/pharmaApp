# Pharmacy Management Application

This is a full-stack application for managing a pharmacy's inventory, sales, purchases, and suppliers.

## Features

- Inventory management
- Sales tracking
- Purchase management
- Supplier management
- User authentication
- Reporting

## Technology Stack

- **Backend**: Spring Boot, Java
- **Frontend**: React, TypeScript, Vite, Shadcn UI
- **Database**: MySQL
- **Containerization**: Docker

## Prerequisites

- Docker and Docker Compose
- Git

## Getting Started

### Running with Docker Compose

#### Development Environment

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd pharmaApp
   ```

2. Start the application using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Access the application:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080/api
   - Database Admin (Adminer): http://localhost:8081
     - System: MySQL
     - Server: db
     - Username: pharmacyuser
     - Password: pharmacypass
     - Database: pharmacy

4. To stop the application:
   ```bash
   docker-compose down
   ```

#### Production Environment

For production deployment, we use a different Docker Compose configuration that builds the frontend and serves it from the backend:

1. Build the frontend and copy it to the backend's static resources:
   ```bash
   build_frontend.bat
   ```

2. Start the application using the production Docker Compose file:
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

3. Access the application:
   - Application (Frontend + Backend): http://localhost:8080
   - Database Admin (Adminer): http://localhost:8081
     - System: MySQL
     - Server: db
     - Username: pharmacyuser
     - Password: pharmacypass
     - Database: pharmacy

4. To stop the application:
   ```bash
   docker-compose -f docker-compose.prod.yml down
   ```

### Development Setup

#### Backend (Spring Boot)

1. Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Run the `PharmacyApplication.java` file

#### Frontend (React)

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Access the frontend at http://localhost:3000

### Production Deployment

To build the frontend and integrate it with the backend for production deployment:

1. Run the setup script to install frontend dependencies (if not already done):
   ```bash
   setup_frontend.bat
   ```

2. Build the frontend and copy it to the backend's static resources:
   ```bash
   build_frontend.bat
   ```

3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application at http://localhost:8080

This setup allows the Spring Boot application to serve both the backend API and the frontend from the same origin, eliminating any CORS issues in production.

## Testing the Connection

To verify that the frontend, backend, and database are properly connected:

1. Run the test connection script:
   ```bash
   test_connection.bat
   ```

This script will:
1. Test the connection to the MySQL database
2. Test the connection to the backend API
3. Check if the frontend is integrated with the backend

If any of these tests fail, the script will provide instructions for resolving the issues.

## Database

The application uses MySQL as the database. When the application starts for the first time, it will:

1. Create the database if it doesn't exist
2. Create the necessary tables
3. Initialize the database with sample data if it's empty

## API Documentation

The backend exposes RESTful APIs for the following resources:

- `/api/medicaments` - Medications
- `/api/fournisseurs` - Suppliers
- `/api/achats` - Purchases
- `/api/ventes` - Sales
- `/api/admin` - Admin operations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License.
