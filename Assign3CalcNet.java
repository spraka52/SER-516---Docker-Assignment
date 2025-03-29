import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.regex.*;

public class Assign3CalcNet {

    static final Map<String, String> SERVICE_URLS = Map.of(
        "+", "http://assign3-plus-net:8080/calculate_plus",
        "-", "http://assign3-minus-net:8080/calculate_minus",
        "*", "http://assign3-multiply-net:8080/calculate_multiply",
        "/", "http://assign3-divide-net:8080/calculate_divide"
    );

    static HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/calculate", Assign3CalcNet::handle);
        server.start();
    }

    private static void handle(HttpExchange exchange) throws IOException {
        String response = "1";
        int code = 400;

        try {
            String query = exchange.getRequestURI().getQuery();
            if (query == null || !query.startsWith("expr=")) throw new Exception("Invalid query");

            String expr = query.substring(5).replaceAll("%20", "");
           

            List<String> tokens = tokenize(expr);
            if (tokens == null) throw new Exception("Invalid tokens");

            tokens = evaluate(tokens, Set.of("*", "/"));
            tokens = evaluate(tokens, Set.of("+", "-"));

            if (tokens.size() == 1) {
                response = tokens.get(0);
                code = 200;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        exchange.sendResponseHeaders(code, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static List<String> tokenize(String expr) {
        if (!expr.matches("[0-9+\\-*/ ]+")) return null;
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("\\d+|[+\\-*/]").matcher(expr);
        while (m.find()) tokens.add(m.group());
        if (tokens.size() < 3 || "+*/".contains(tokens.get(0)) || "+-*/".contains(tokens.get(tokens.size() - 1)))
            return null;
        return tokens;
    }

    private static List<String> evaluate(List<String> tokens, Set<String> ops) throws Exception {
        List<String> result = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            if (ops.contains(token)) {
                int a = Integer.parseInt(result.remove(result.size() - 1));
                int b = Integer.parseInt(tokens.get(++i));
                int res = callService(token, a, b);
                result.add(String.valueOf(res));
            } else {
                result.add(token);
            }
            i++;
        }
        return result;
    }
    private static int callService(String op, int a, int b) throws Exception {
        String url = SERVICE_URLS.get(op) + "?a=" + a + "&b=" + b;
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        return Integer.parseInt(res.body());
    }
}
