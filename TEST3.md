# TEST3.md – Testing Instructions for Network Microservices

### Docker Build Commands

```bash
docker build -t assign3-plus-net -f Dockerfile.plus-net .
docker build -t assign3-minus-net -f Dockerfile.minus-net .
docker build -t assign3-multiply-net -f Dockerfile.multiply-net .
docker build -t assign3-divide-net -f Dockerfile.divide-net .
```

---

## Run Docker Containers

Run each container in **detached mode** (`-d`) and map unique host ports to container port `8080`.

```bash
docker run -d -p 8010:8080 assign3-plus-net
docker run -d -p 8011:8080 assign3-minus-net
docker run -d -p 8012:8080 assign3-multiply-net
docker run -d -p 8013:8080 assign3-divide-net
```

---

## Test with `curl`

All services accept two **non-negative integers** as query parameters: `a` and `b`.

### Addition

```bash
curl "http://localhost:8010/calculate_plus?a=5&b=10"
# Expected Output: 15
```

#### Postman:
- Method: `GET`
- URL: `http://localhost:8010/calculate_plus`
- Params tab:
  - `a` = `5`
  - `b` = `10`
- Send → Response: `15`

---

### Subtraction

```bash
curl "http://localhost:8011/calculate_minus?a=20&b=8"
# Expected Output: 12
```

#### Postman:
- Method: `GET`
- URL: `http://localhost:8011/calculate_minus`
- Params:
  - `a` = `20`
  - `b` = `8`
- Send → Response: `12`

---

### Multiplication

```bash
curl "http://localhost:8012/calculate_multiply?a=3&b=4"
# Expected Output: 12
```
#### Postman:
- Method: `GET`
- URL: `http://localhost:8012/calculate_multiply`
- Params:
  - `a` = `3`
  - `b` = `4`
- Send → Response: `12`

---

### Division

```bash
curl "http://localhost:8013/calculate_divide?a=20&b=5"
# Expected Output: 4
```
#### Postman:
- Method: `GET`
- URL: `http://localhost:8013/calculate_divide`
- Params:
  - `a` = `20`
  - `b` = `5`
- Send → Response: `4`

---

## Error Case Testing

All error responses return:
- HTTP status code: `400`
- Body: `"1"`

#### Examples for All Services

| Scenario             | curl Example                                             | Postman Parameters       |
|----------------------|----------------------------------------------------------|--------------------------|
| Missing param        | `?a=5`                                                   | `a=5`                    |
| Negative input       | `?a=-1&b=5`                                              | `a=-1`, `b=5`            |
| Non-integer input    | `?a=abc&b=10`                                            | `a=abc`, `b=10`          |
| Divide by zero       | `?a=10&b=0` (only for divide service)                    | `a=10`, `b=0`            |

**Example curl for error:**
```bash
curl "http://localhost:8010/calculate_plus?a=5"
# Expected: 1 with HTTP 400
```

---

##  Summary

- Each service runs independently and accepts requests via HTTP GET.
- Only integer results are returned (or `1` for any error).
- Services are lightweight, stateless, and suitable for automated testing with `curl`, Postman, or other tools.

