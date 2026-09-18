# VJ Pay 💳

VJ Pay is a modern, premium digital payment application. It provides users with a seamless and intuitive platform to manage their bank accounts, perform instant money transfers, and pay bills securely. 

The project features a robust Spring Boot backend coupled with a stunning, highly responsive Angular frontend utilizing modern design principles (glassmorphism, animations, and Tailwind CSS).

## 🛠️ Tech Stack
- **Backend**: Java 17, Spring Boot, Spring Security, Hibernate, MySQL.
- **Frontend**: Angular 17, Tailwind CSS, TypeScript.

---

## 🚀 Local Run Steps

### Prerequisites
- Node.js (v18+)
- Angular CLI
- Java Development Kit (JDK 17+)
- Maven
- MySQL Server (Ensure it is running on default port `3306`)

### 1. Database Setup
1. Open your MySQL client or terminal.
2. Create the database:
   ```sql
   CREATE DATABASE paymentdb;
   ```
3. Update the database credentials in the backend configuration if your root password is not the default:
   `Payment-WalletApplication/src/main/resources/application.properties`

### 2. Running the Backend (Spring Boot)
1. Open a terminal and navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the application using Maven wrapper:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```
3. The backend API will start on `http://localhost:8080`.

### 3. Running the Frontend (Angular)
1. Open a new terminal and navigate to the Angular workspace:
   ```bash
   cd frontend
   ```
2. Install the necessary NPM dependencies (if running for the first time):
   ```bash
   npm install
   ```
3. Start the Angular development server:
   ```bash
   ng serve
   ```
4. Open your browser and navigate to `http://localhost:4200/`. You should see the premium VJ Pay landing page.

---

## 👨‍💻 Default Admin Access
If the application includes seeded default users, you can usually access the admin dashboard via the `/admin-login` route using the default configured credentials.


VJpayapp@123

VJ PAY
vjpayapp@gmail.com