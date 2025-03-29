import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.stream.Collectors;

public class Assign3_plus_net {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/calculate_plus", (HttpExchange exchange) -> {
            String response = "1";
            int statusCode = 400;

            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                    return;
                }

                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                if (!params.containsKey("a") || !params.containsKey("b")) {
                    throw new Exception("Missing parameters");
                }

                int a = Integer.parseInt(params.get("a"));
                int b = Integer.parseInt(params.get("b"));

                if (a < 0 || b < 0) throw new Exception("Negative input");

                int result = Math.addExact(a, b);
                response = Integer.toString(result);
                statusCode = 200;
            } catch (Exception e) {
                // response is already set to "1"
            }

            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();
    }

    private static Map<String, String> queryToMap(String query) {
        return query == null ? Map.of() : 
            java.util.Arrays.stream(query.split("&"))
                .map(kv -> kv.split("="))
                .filter(kv -> kv.length == 2)
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
    }
}
