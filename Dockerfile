# ===================================================================
# Multi-Stage Dockerfile for IT Service Desk Management System
# Base Image: Eclipse Temurin OpenJDK 17
# ===================================================================

# -------------------------------------------------------------------
# Stage 1: Build Application Artifact
# -------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace

# Copy Maven wrapper configuration and dependencies descriptor
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Normalize potential Windows line endings (CRLF -> LF) and make wrapper executable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Resolve dependencies offline to take advantage of Docker build caching
RUN ./mvnw dependency:go-offline -B

# Copy source code and build the executable JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# -------------------------------------------------------------------
# Stage 2: Minimal Production Runtime
# -------------------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create dedicated non-privileged user and group for container security
RUN addgroup --system springboot && adduser --system springboot --ingroup springboot

# Copy compiled JAR from build stage
COPY --from=builder /workspace/target/*.jar app.jar

# Set ownership to unprivileged user
RUN chown -R springboot:springboot /app
USER springboot:springboot

# Expose application port
EXPOSE 8080

# Set default profile to MySQL
ENV SPRING_PROFILES_ACTIVE=mysql

# Application Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/health || exit 1

# Launch Spring Boot Application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
