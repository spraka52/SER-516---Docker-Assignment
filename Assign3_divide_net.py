from flask import Flask, request
import os

app = Flask(__name__)

@app.route('/calculate_divide')
def calculate_divide():
    try:
        a = int(request.args.get('a', ''))
        b = int(request.args.get('b', ''))
        if a < 0 or b <= 0:  
            return "1", 400
        return str(a // b)
    except:
        return "1", 400

if __name__ == '__main__':
    port = int(os.environ.get("PORT", 8080))
    app.run(host='0.0.0.0', port=port)
