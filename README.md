🧩 Patient–Billing Microservices (Spring Boot + gRPC + Docker)

A microservices-based application demonstrating inter-service communication between a RESTful Patient Service and a gRPC-based Billing Service.

🚀 Features

Patient Service — Exposes REST APIs to manage patient-related operations.

Billing Service — Handles invoice creation, exposed via gRPC.

gRPC Communication — Patient Service calls Billing Service securely over gRPC.

Dockerized Deployment — Both services are containerized using Docker for easy setup and isolated environments.

🛠️ Tech Stack

Spring Boot 3

gRPC

Docker

Maven

Java 21

▶️ Run Locally
# Build and run services
docker-compose up --build

📦 Example Flow

Patient Service receives a REST request to create a patient.

It triggers a gRPC call to Billing Service.

Billing Service generates an invoice and returns the response.

📚 Purpose

This project demonstrates how to integrate REST and gRPC microservices within a containerized Spring Boot ecosystem.
