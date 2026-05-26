# Full Stack E-Commerce Platform

A comprehensive full-stack e-commerce web application inspired by modern online marketplace platforms.

This project was developed with **Angular** on the frontend and **Spring Boot** on the backend. The main goal is to simulate the core features of a real-world shopping platform, including product browsing, user operations, cart management, order workflows, and product reviews.

---

## Project Overview

This project is designed as a marketplace-style e-commerce platform where users can browse products, view product details, manage their profiles, add items to their cart, place orders, and interact with product reviews.

The application includes both frontend and backend development processes, focusing on RESTful API communication, database operations, and full-stack project architecture.

---

## Features

- User registration and login
- User profile management
- Product listing with pagination
- Product detail pages
- Product search and filtering
- Category-based product browsing
- Shopping cart management
- Add/remove products from cart
- Update product quantity
- Order creation and checkout
- Order history tracking
- Product review and rating system
- Database-based data management
- RESTful API integration

---

## Technologies Used

### Frontend
- **Angular** - Modern web framework
- **TypeScript** - Type-safe programming
- **HTML5 & CSS3** - Markup and styling

### Backend
- **Java** - Core language
- **Spring Boot** - Backend framework
- **Spring Data JPA** - Database ORM
- **RESTful APIs** - API architecture

### Database
- **MySQL** - Relational database

### Tools & Infrastructure
- **Git & GitHub** - Version control
- **Postman** - API testing
- **VS Code** - Frontend development
- **IntelliJ IDEA** - Backend development
- **Maven** - Build tool

---

## Project Structure

```
e-commerce-Project/
├── backend/
│   ├── src/
│   │   ├── main/java/
│   │   │   └── com/ecommerce/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       └── ...
│   │   └── test/
│   ├── pom.xml
│   └── ...
│
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   ├── services/
│   │   │   ├── models/
│   │   │   └── ...
│   │   ├── assets/
│   │   └── ...
│   ├── angular.json
│   ├── package.json
│   └── ...
│
├── InitialTestData/
│   └── [SQL scripts for test data]
│
├── README.md
└── LICENSE
```

---

## Installation & Setup

### Prerequisites
- Node.js and npm (for frontend)
- Java 11+ and Maven (for backend)
- MySQL database
- Git

### Clone the Repository

```bash
git clone https://github.com/zeyneptanrivermis/e-commerce-Project.git
cd e-commerce-Project
```

---

## Running the Frontend

### Navigate to frontend directory
```bash
cd frontend
```

### Install dependencies
```bash
npm install
```

### Start the development server
```bash
ng serve
```

### Open in browser
```
http://localhost:4200/
```

---

## Running the Backend

### Navigate to backend directory
```bash
cd backend
```

### Run Spring Boot application
```bash
mvn spring-boot:run
```

**Backend runs on:** `http://localhost:8080/`

---

## API Integration

- The frontend communicates with the backend via **RESTful APIs**
- The backend handles:
  - User authentication and authorization
  - Product management
  - Cart operations
  - Order processing
  - Review management
  - Database operations

---

## Database Schema

The application uses a **relational database** with the following main entities:

| Entity | Description |
|--------|-------------|
| **Users** | User accounts and authentication |
| **Products** | Product catalog |
| **Categories** | Product categories |
| **CartItems** | Shopping cart items |
| **Orders** | Order history and tracking |
| **Reviews** | Product reviews and ratings |

Initial test data is available in the `InitialTestData` folder.

---

## License

This project was developed for **educational and practice purposes**.
