import sys

try:
    if len(sys.argv) != 3:
        sys.exit(1)
    a = int(sys.argv[1])
    b = int(sys.argv[2])
    if a < 0 or b < 0:
        sys.exit(1)
    print(a - b)
except:
    sys.exit(1)
