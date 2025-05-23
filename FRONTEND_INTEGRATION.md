# Frontend Integration Guide

This document provides instructions for integrating a new frontend with the Pharmacy application backend.

## Backend API Endpoints

The backend provides the following RESTful API endpoints:

- `/api/medicaments` - Medication management
- `/api/fournisseurs` - Supplier management
- `/api/produits` - Product management
- `/api/ventes` - Sales management
- `/api/achats` - Purchase management
- `/api/alerts` - Alerts for low stock medications

## Integration Steps

1. **Build your frontend application**
   - Create your frontend application using your preferred framework (React, Angular, Vue, etc.)
   - Configure it to make API calls to the backend endpoints listed above

2. **Configure API Base URL**
   - During development, set your API base URL to `http://localhost:8080`
   - For production, configure the URL according to your deployment environment

3. **Build and Deploy**
   - Build your frontend application
   - Copy the built files to the Spring Boot static resources directory:
     `src/main/resources/static`
   - You can automate this process with a script similar to the one provided in this repository

4. **CORS Configuration**
   - The backend is already configured to allow CORS requests from `http://localhost:3000`
   - If your frontend development server uses a different port, update the CORS configuration in:
     `src/main/java/com/Pharmacy/Pharmacy/config/WebConfig.java`

## Authentication

Currently, the application does not implement authentication. If you need to add authentication:

1. Implement a login endpoint in the backend
2. Configure Spring Security
3. Implement JWT or session-based authentication
4. Update your frontend to handle authentication tokens

## Example API Requests

### Get all medications
```javascript
fetch('http://localhost:8080/api/medicaments')
  .then(response => response.json())
  .then(data => console.log(data));
```

### Create a new medication
```javascript
fetch('http://localhost:8080/api/medicaments', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    nom: 'Paracetamol',
    prixAchat: 5.0,
    prixVente: 10.0,
    quantiteStock: 100,
    seuilAlerte: 20,
    dateExpiration: '2023-12-31',
    fournisseur: {
      id: 1
    }
  }),
})
.then(response => response.json())
.then(data => console.log(data));
```

## Database Configuration

The backend is configured to connect to a database as specified in `application.properties`. No changes are needed on the frontend side to interact with the database, as all database operations are handled by the backend.