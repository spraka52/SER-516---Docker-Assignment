# TASK4 – Design Rationale for Calculator Microservice

## Overview
The `calc` service is a stateless microservice that accepts infix arithmetic expressions via HTTP, evaluates them while respecting operator precedence, and computes the result using four lightweight networked microservices (`+`, `-`, `*`, `/`).

This task was implemented in **Java** using a minimal HTTP server (`com.sun.net.httpserver.HttpServer`) to avoid heavy frameworks and keep the service lightweight.

---

## Design Decisions

### Language: Java
Java was chosen to balance performance, ease of string parsing, and availability of lightweight HTTP server libraries without needing a full web framework.

###  Lightweight HTTP Server
Used `HttpServer` from the JDK to implement an efficient GET-based service without any extra dependencies.

###  Stateless Architecture
The service handles each request independently and performs all operations through synchronous HTTP calls to four separate arithmetic services:
- `assign3-plus-net`
- `assign3-minus-net`
- `assign3-multiply-net`
- `assign3-divide-net`

###  Operator Precedence
Evaluation is done in two passes:
1. First pass for `*` and `/`
2. Second pass for `+` and `-`

This ensures proper precedence without using `eval()` or building a full parser.

###  Error Handling
- Invalid expressions return HTTP `400` with response `1`
- Division by zero and malformed queries also return `1`

###  Docker & Network
The service is containerized and connected to the same `assign3-net` as the arithmetic services. Only the `calc` service is exposed to the host.

---