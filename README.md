# ⚙️ RentWheelz - Backend REST API

This is the backend server for the **RentWheelz Car Rental System**. It provides a robust, secure, and scalable REST API to manage users, vehicles, and booking operations.

## ✨ Key Features
- **Secure Authentication:** Spring Security implementation with JSON Web Tokens (JWT) for stateless authentication.
- **Role-Based Access Control (RBAC):** API endpoints secured based on user roles (ROLE_ADMIN, ROLE_CUSTOMER, ROLE_FLEET_MANAGER).
- **Comprehensive CRUD Operations:** Full inventory and user management with automated status updates (e.g., ON_HOLD, RENTED).
- **Data Integrity:** Transactional methods to ensure booking and vehicle statuses are always in sync.
- **Pagination & Filtering:** Optimized API endpoints for handling large datasets (e.g., paginated user lists and vehicle catalogs).

## 🛠️ Tech Stack
- **Framework:** Java Spring Boot
- **Database:** MySQL
- **ORM / Data Access:** Spring Data JPA & Spring JDBC Template
- **Security:** Spring Security & JWT
- **Build Tool:** Maven

## 🚀 How to Run Locally
1. Clone this repository: `git clone https://github.com/your-username/rentwheelz-backend.git`
2. Configure your MySQL database settings in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
   spring.datasource.username=root
   spring.datasource.password=your_password
