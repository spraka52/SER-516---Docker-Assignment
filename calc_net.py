from flask import Flask, request
import os
import re
import requests

app = Flask(__name__)
print("\U0001F4A1 CALC service with fixed precedence logic (fresh version)")

# Internal service URLs (Docker service names)
SERVICES = {
    '+': 'http://assign3-plus-net:8080/calculate_plus',
    '-': 'http://assign3-minus-net:8080/calculate_minus',
    '*': 'http://assign3-multiply-net:8080/calculate_multiply',
    '/': 'http://assign3-divide-net:8080/calculate_divide'
}

def call_service(op, a, b):
    try:
        print(f"Calling {op} with {a} and {b}")
        res = requests.get(SERVICES[op], params={'a': a, 'b': b}, timeout=3)
        if res.status_code == 200:
            print(f"Response from {op}: {res.text}")
            return int(res.text)
    except Exception as e:
        print(f"Error calling {op} with {a}, {b}: {e}")
    return None

def evaluate_expression(expr):
    if not re.fullmatch(r'[0-9+\-*/ ]+', expr):
        print("❌ Invalid characters in expression")
        return None

    tokens = re.findall(r'\d+|[+\-*/]', expr.replace(' ', ''))
    print(f"🧩 Raw tokens: {tokens}")

    if not tokens or tokens[0] in '+*/' or tokens[-1] in '+-*/':
        print("❌ Expression starts or ends with an operator")
        return None

    try:
        tokens = [int(t) if i % 2 == 0 else t for i, t in enumerate(tokens)]
        print(f"🔢 Parsed tokens: {tokens}")

        # First pass: *, /
        i = 1
        while i < len(tokens) - 1:
            if tokens[i] in ('*', '/'):
                a, op, b = tokens[i - 1], tokens[i], tokens[i + 1]
                print(f"⚙️ Performing: {a} {op} {b}")
                result = call_service(op, a, b)
                if result is None:
                    print(f"❌ Failed: {a} {op} {b}")
                    return None
                tokens[i - 1:i + 2] = [result]
                print(f"✅ Tokens after {op}: {tokens}")
                i = 1
            else:
                i += 2

        # Second pass: +, -
        i = 1
        while i < len(tokens) - 1:
            if tokens[i] in ('+', '-'):
                a, op, b = tokens[i - 1], tokens[i], tokens[i + 1]
                print(f"⚙️ Performing: {a} {op} {b}")
                result = call_service(op, a, b)
                if result is None:
                    print(f"❌ Failed: {a} {op} {b}")
                    return None
                tokens[i - 1:i + 2] = [result]
                print(f"✅ Tokens after {op}: {tokens}")
                i = 1
            else:
                i += 2

        print(f"🎉 Final result: {tokens[0]}")
        return tokens[0] if len(tokens) == 1 else None

    except Exception as e:
        print(f"💥 Evaluation error: {e}")
        return None


@app.route('/calculate')
def calculate():
    expr = request.args.get('expr', '')
    print(f"Received expression: {expr}")
    result = evaluate_expression(expr)
    if result is None:
        return "1", 400
    return str(result)

if __name__ == '__main__':
    port = int(os.environ.get("PORT", 8080))
    app.run(host='0.0.0.0', port=port)
