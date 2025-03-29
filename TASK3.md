# TASK 3 – Design Summary

## Overview
In this task, the four arithmetic operations (plus, minus, multiply, divide) were implemented as lightweight stateless microservices that communicate over HTTP. Each service runs inside its own Docker container, exposes a configurable port, and accepts input via query parameters.

## Technologies Used
- Java (for plus and multiply) using built-in com.sun.net.httpserver.HttpServer for zero-dependency HTTP handling.
- Python (for minus and divide) using Flask microframework for simplicity and lightweight network interface.
- Docker for containerization.
- curl for testing.

## Port Configuration
Each service listens on an internal port (default: 8080), which can be overridden using the `PORT` environment variable. When running with Docker, the external port is specified using the `-p` option and is mapped to the internal one.

## API Design
Each service exposes a GET endpoint:
- `/calculate_plus` for addition
- `/calculate_minus` for subtraction
- `/calculate_multiply` for multiplication
- `/calculate_divide` for division

All endpoints accept two query parameters:
- `a` (non-negative integer)
- `b` (non-negative integer)

## Error Handling
If any of the following conditions are met, the services return a plain `1` with HTTP status code `400`:
- Missing or malformed parameters
- Negative input
- Divide by zero
- Overflow (for Java services using `Math.addExact` and `Math.multiplyExact`)

All services are stateless and can handle multiple requests while running in detached mode.
