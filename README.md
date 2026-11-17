# IT Service Desk Management System

A enterprise-grade IT Service Desk application built with **Java 17** and **Spring Boot 3**. This system enables employees to raise technical support tickets and IT support engineers to triage, assign, and resolve service requests.

---

## 🚀 Technology Stack

- **Java**: 17 (LTS)
- **Framework**: Spring Boot 3.3.5
- **Build Tool**: Maven (with Maven Wrapper `mvnw`)
- **Database**: MySQL 8.x
- **Key Starters & Libraries**:
  - `spring-boot-starter-web`: RESTful APIs and embedded Tomcat
  - `spring-boot-starter-data-jpa`: Spring Data JPA and Hibernate ORM
  - `spring-boot-devtools`: Hot reloading and accelerated development turnaround
  - `mysql-connector-j`: Official MySQL JDBC Driver
  - `spring-boot-starter-test`: JUnit 5, AssertJ, and Mockito

---

## 📂 Project Structure

```text
it-service-desk/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties   # Pins the exact Maven version for builds
├── src/
│   ├── main/
│   │   ├── java/com/servicedesk/
│   │   │   ├── ServiceDeskApplication.java   # Application entry point (@SpringBootApplication)
│   │   │   ├── controller/                   # REST Controllers (exposing HTTP endpoints)
│   │   │   │   └── HealthCheckController.java # Health check endpoint
│   │   │   ├── service/                      # Business logic layer
│   │   │   ├── repository/                   # Spring Data JPA data access interfaces
│   │   │   ├── entity/                       # JPA Database entities
│   │   │   ├── dto/                          # Data Transfer Objects (request/response payloads)
│   │   │   └── config/                       # Application beans & security configurations
│   │   └── resources/
│   │       ├── application.properties        # Application configuration & DB settings
│   │       ├── static/                       # Static web assets (if applicable)
│   │       └── templates/                    # Server-rendered HTML templates (if applicable)
│   └── test/
│       └── java/com/servicedesk/
│           └── ServiceDeskApplicationTests.java # Context loading integration tests
├── .gitignore                                # Ignores build artifacts, IDE files, and secrets
├── mvnw / mvnw.cmd                           # Cross-platform Maven wrappers
├── pom.xml                                   # Project Object Model (dependencies & build rules)
└── README.md                                 # Documentation and getting started guide
```

---

## ⚙️ Prerequisites

Before running the application, ensure you have:
1. **JDK 17 or higher** installed (`java -version`).
2. **MySQL Server 8.x** running (optional for health-check, required for database persistence).
3. (Optional) Maven installed locally. If not installed, use the included `./mvnw` (Linux/macOS) or `mvnw.cmd` (Windows).

---

## 🔐 Environment Variables & Security

Sensitive credentials (such as database passwords) should **never** be hardcoded into `application.properties` or pushed to version control.

The application reads database configuration via environment variables with fallback defaults:

| Variable | Description | Default Value |
| :--- | :--- | :--- |
| `DB_URL` | JDBC Connection URL | `jdbc:mysql://localhost:3306/servicedesk_db?...` |
| `DB_USERNAME` | Database username | `root` |
| `DB_PASSWORD` | Database password | *(empty)* |

### Setting Environment Variables (Local Development)

**Windows (PowerShell):**
```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "YourSecurePassword"
```

**Linux / macOS:**
```bash
export DB_USERNAME="root"
export DB_PASSWORD="YourSecurePassword"
```

---

## 🏃 Running the Application

### Option 1: Using the Maven Wrapper (Recommended)

**Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**Linux / macOS:**
```bash
./mvnw spring-boot:run
```

### Option 2: Building and Running the Executable JAR

```bash
# Package the project (skipping tests during initial setup)
.\mvnw.cmd clean package -DskipTests

# Run the packaged JAR
java -jar target/it-service-desk-0.0.1-SNAPSHOT.jar
```

---

## 🧪 Testing the Health Check Endpoint

Once the application has started (default port: `8080`), you can test the health check endpoint:

### Using cURL:
```bash
curl -X GET http://localhost:8080/api/v1/health
```

### Expected Response:
```json
{
  "status": "UP",
  "service": "IT Service Desk Management System",
  "message": "Application is healthy and running smoothly.",
  "timestamp": "2026-11-17T08:00:00Z"
}
```

---

## 🛡️ Pre-Commit Checklist

Before committing your code to Git / GitHub, always verify:
- [ ] `.gitignore` is present and ignores `.env`, `target/`, `.idea/`, `.vscode/`, and secret property files.
- [ ] No hardcoded passwords, API keys, or IP addresses exist in `application.properties`.
- [ ] The application compiles cleanly with `.\mvnw.cmd clean compile`.
- [ ] Only intended source files are staged (`git status`).
