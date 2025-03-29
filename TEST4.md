# TEST4.md – Testing Instructions for Calculator Microservice

## Setup
1. Ensure all services and Dockerfiles are in the root directory.
2. From the project root, run:
```bash
docker-compose build --no-cache
docker-compose up
```

3. In a second terminal, test using `curl` or Postman.

---

## Valid Test Cases

### 1. `3 + 5 * 7 / 4 - 7`
```bash
curl "http://localhost:8000/calculate?expr=3+5*7/4-7"
```
**Expected Output:** `4`

### 2. `1 - 7 / 3 + 3 * 9`
```bash
curl "http://localhost:8000/calculate?expr=1-7/3+3*9"
```
**Expected Output:** `26`

### 3. `8 + 4 * 2`
```bash
curl "http://localhost:8000/calculate?expr=8+4*2"
```
**Expected Output:** `16`

---

## Error Test Cases

### 1. Division by zero
```bash
curl "http://localhost:8000/calculate?expr=10/0"
```
**Expected Output:** `1` (HTTP 400)

### 2. Malformed expression
```bash
curl "http://localhost:8000/calculate?expr=5++2"
```
**Expected Output:** `1` (HTTP 400)

### 3. Invalid characters
```bash
curl "http://localhost:8000/calculate?expr=3+abc"
```
**Expected Output:** `1` (HTTP 400)

---

## Optional: Postman Testing
1. Open Postman.
2. Create a GET request:
```
http://localhost:8000/calculate?expr=3+5*7/4-7
```
3. Send the request.
4. You should see a `200 OK` response with body:
```
4
```
5. Try invalid input to see `400 Bad Request` and output `1`

---