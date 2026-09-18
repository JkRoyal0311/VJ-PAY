<p align="center">
  <h1 align="center">💳 VJ Pay</h1>
  <p align="center">
    A modern, full-stack digital payment & wallet application
    <br />
    <a href="#-features">Features</a> · <a href="#-tech-stack">Tech Stack</a> · <a href="#-getting-started">Getting Started</a> · <a href="#-deployment">Deployment</a>
  </p>
</p>

---

## 📖 About

**VJ Pay** is a premium digital payment platform that lets users manage bank accounts, transfer money instantly, pay bills, and track transactions — all through a sleek, responsive interface. It features a full **admin dashboard** for platform management and a secure **JWT-based authentication** system.

---

## ✨ Features

### Customer Portal
- **Dashboard** — Real-time overview of balances, recent transactions, and quick actions
- **Wallet** — Digital wallet with add-money, withdraw, and balance tracking
- **Bank Accounts** — Link and manage multiple bank accounts
- **Money Transfer** — Instant peer-to-peer transfers via beneficiary management
- **Bill Payments** — Pay electricity, water, gas, internet, and mobile bills
- **Transaction History** — Filterable and categorized transaction logs

### Admin Panel
- **Overview Dashboard** — Platform-wide analytics and metrics
- **Customer Management** — View, search, and manage all registered users
- **Wallet Management** — Monitor and audit wallet balances
- **Bank Account Oversight** — Review linked bank accounts across the platform
- **Beneficiary Management** — Track beneficiary records
- **Bill Payment Monitoring** — Audit all bill payment activities
- **Transaction Logs** — Full transaction history with advanced filtering

### Security
- JWT-based stateless authentication
- Role-based access control (Customer / Admin)
- Route guards on both frontend and backend
- Password encryption with Spring Security

---

## 🛠 Tech Stack

| Layer        | Technology                                                                 |
|--------------|----------------------------------------------------------------------------|
| **Frontend** | Angular 19, TypeScript, Tailwind CSS, RxJS                                |
| **Backend**  | Java 17, Spring Boot 3.5, Spring Security, Spring Data JPA, Hibernate     |
| **Database** | PostgreSQL (production) · H2 (development)                                |
| **Auth**     | JSON Web Tokens (JJWT 0.11.5)                                            |
| **Payments** | Razorpay Java SDK                                                         |
| **Email**    | Spring Boot Mail (SMTP / Gmail)                                           |
| **Migration**| Flyway                                                                    |
| **Build**    | Maven (backend) · Angular CLI (frontend)                                  |
| **DevOps**   | Docker, Docker Compose, Render                                            |

---

## 📁 Project Structure

```
VJ-PAY/
├── backend/                          # Spring Boot application
│   ├── src/main/java/com/coforge/
│   │   ├── controllers/              # REST API controllers
│   │   ├── entities/                 # JPA entity models
│   │   ├── repositories/            # Spring Data repositories
│   │   ├── services/                 # Business logic layer
│   │   ├── dtos/                     # Data transfer objects
│   │   ├── security/                 # JWT & Spring Security config
│   │   ├── filters/                  # Request filters
│   │   └── exception/                # Global exception handling
│   ├── src/main/resources/
│   │   └── application.properties    # App configuration
│   ├── Dockerfile.backend
│   └── pom.xml
│
├── frontend/                         # Angular 19 application
│   ├── src/app/
│   │   ├── components/
│   │   │   ├── landing/              # Public landing page
│   │   │   ├── auth/                 # Login & Signup
│   │   │   ├── dashboard/            # User dashboard
│   │   │   ├── wallet/               # Wallet management
│   │   │   ├── bank-account/         # Bank account management
│   │   │   ├── beneficiary/          # Beneficiary management
│   │   │   ├── bill-payment/         # Bill payment module
│   │   │   ├── transaction/          # Transaction history
│   │   │   ├── admin/                # Admin dashboard & modules
│   │   │   ├── navbar/               # Navigation bar
│   │   │   └── layout/               # App shell layout
│   │   ├── guards/                   # Auth & Admin route guards
│   │   ├── services/                 # API communication services
│   │   └── models/                   # TypeScript interfaces
│   ├── Dockerfile.frontend
│   └── package.json
│
├── docker-compose.yml                # Multi-container orchestration
├── render.yaml                       # Render.com deployment blueprint
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

| Tool            | Version  |
|-----------------|----------|
| Java JDK        | 17+      |
| Maven            | 3.8+     |
| Node.js          | 18+      |
| Angular CLI      | 19+      |
| PostgreSQL       | 14+      |

### 1. Clone the Repository

```bash
git clone https://github.com/JkRoyal0311/VJ-PAY.git
cd VJ-PAY
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE paymentdb;
```

> The default connection expects PostgreSQL running on `localhost:5432` with username `postgres` and password `postgres`. Override via environment variables if needed (see [Configuration](#-configuration)).

### 3. Run the Backend

```bash
cd backend
./mvnw spring-boot:run        # Linux / macOS
.\mvnw.cmd spring-boot:run    # Windows
```

The API server starts at **`http://localhost:8080`**.

### 4. Run the Frontend

```bash
cd frontend
npm install
ng serve
```

The app opens at **`http://localhost:4200`**.

---

## ⚙ Configuration

All backend configuration is managed via environment variables with sensible defaults:

| Variable        | Default                              | Description                    |
|-----------------|--------------------------------------|--------------------------------|
| `DB_URL`        | `jdbc:postgresql://localhost:5432/paymentdb` | JDBC connection URL    |
| `DB_USER`       | `postgres`                           | Database username              |
| `DB_PASSWORD`   | `postgres`                           | Database password              |
| `JWT_SECRET`    | *(built-in default)*                 | JWT signing secret (32+ chars) |
| `MAIL_USER`     | —                                    | Gmail address for notifications|
| `MAIL_PASSWORD`  | —                                    | Gmail app password             |

---

## 🔌 API Reference

### Authentication
| Method | Endpoint              | Description            | Auth  |
|--------|-----------------------|------------------------|-------|
| POST   | `/auth/register`      | Register a new user    | No    |
| POST   | `/auth/login`         | Login & get JWT token  | No    |
| POST   | `/auth/admin-login`   | Admin login            | No    |

### Customer
| Method | Endpoint              | Description              | Auth  |
|--------|-----------------------|--------------------------|-------|
| GET    | `/customer/profile`   | Get logged-in user info  | JWT   |
| PUT    | `/customer/update`    | Update profile           | JWT   |

### Wallet
| Method | Endpoint              | Description           | Auth  |
|--------|-----------------------|-----------------------|-------|
| GET    | `/wallet`             | Get wallet details    | JWT   |
| POST   | `/wallet/add-money`   | Add money to wallet   | JWT   |

### Bank Accounts
| Method | Endpoint               | Description                | Auth  |
|--------|------------------------|----------------------------|-------|
| GET    | `/bank-account`        | List user's bank accounts  | JWT   |
| POST   | `/bank-account/add`    | Link a new bank account    | JWT   |
| DELETE | `/bank-account/{id}`   | Remove a bank account      | JWT   |

### Beneficiaries
| Method | Endpoint                  | Description             | Auth  |
|--------|---------------------------|-------------------------|-------|
| GET    | `/beneficiary`            | List beneficiaries      | JWT   |
| POST   | `/beneficiary/add`        | Add a beneficiary       | JWT   |
| DELETE | `/beneficiary/{id}`       | Remove a beneficiary    | JWT   |

### Transactions
| Method | Endpoint                 | Description               | Auth  |
|--------|--------------------------|---------------------------|-------|
| GET    | `/transaction`           | Transaction history       | JWT   |
| POST   | `/transaction/transfer`  | Transfer money            | JWT   |

### Bill Payments
| Method | Endpoint                    | Description            | Auth  |
|--------|-----------------------------|------------------------|-------|
| GET    | `/bill-payment`             | List bill payments     | JWT   |
| POST   | `/bill-payment/pay`         | Pay a bill             | JWT   |

### Admin Endpoints
All admin endpoints are prefixed with `/admin/` and require admin-level JWT authentication.

---

## 🐳 Docker

Run the entire stack with Docker Compose:

```bash
docker-compose up --build
```

| Service    | Port   |
|------------|--------|
| Frontend   | `80`   |
| Backend    | `8080` |
| MySQL (DB) | `3306` |

---

## 🌐 Deployment

### Render (One-Click)

This project includes a [`render.yaml`](render.yaml) blueprint for instant deployment on [Render](https://render.com):

1. Fork this repository
2. Go to **Render Dashboard → Blueprints → New Blueprint Instance**
3. Connect your forked repo
4. Render will auto-provision:
   - PostgreSQL database (`vjpay-db`)
   - Backend web service (`vjpay-backend`)
   - Frontend static site (`vjpay-frontend`)

---

## 👥 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is for educational and demonstration purposes.

---

<p align="center">
  Built with ❤️ by <a href="https://github.com/JkRoyal0311">JkRoyal0311</a>
</p>