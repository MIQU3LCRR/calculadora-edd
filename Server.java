import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final int MIN_VALOR = -99999;
    private static final int MAX_VALOR = 99999;
    private static final int PUERTO = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PUERTO), 0);
        server.createContext("/", new HtmlHandler());
        server.createContext("/calcular", new CalculoHandler());
        server.setExecutor(null);
        System.out.println("Servidor iniciado en http://localhost:" + PUERTO);
        server.start();
    }

    static class HtmlHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarRespuesta(exchange, 405, "text/plain; charset=UTF-8", "Método no permitido");
                return;
            }

            String ruta = exchange.getRequestURI().getPath();
            if ("/".equals(ruta) || "/index.html".equals(ruta)) {
                Path archivoHtml = Paths.get("index.html");
                if (Files.exists(archivoHtml)) {
                    byte[] contenido = Files.readAllBytes(archivoHtml);
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, contenido.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(contenido);
                    }
                } else {
                    enviarRespuesta(exchange, 404, "text/plain; charset=UTF-8", "No se encontró index.html");
                }
            } else {
                enviarRespuesta(exchange, 404, "text/plain; charset=UTF-8", "Ruta no encontrada");
            }
        }
    }

    static class CalculoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviarJson(exchange, 405, false, "Método no permitido");
                return;
            }

            Map<String, String> params = parsearQuery(exchange.getRequestURI().getRawQuery());
            String rawNum1 = params.get("num1");
            String rawNum2 = params.get("num2");
            String operacion = params.get("operacion");

            if (rawNum1 == null || rawNum2 == null || operacion == null
                    || rawNum1.isBlank() || rawNum2.isBlank() || operacion.isBlank()) {
                enviarJson(exchange, 400, false, "Faltan parámetros obligatorios.");
                return;
            }

            int num1;
            int num2;
            try {
                num1 = Integer.parseInt(rawNum1.trim());
                num2 = Integer.parseInt(rawNum2.trim());
            } catch (NumberFormatException e) {
                enviarJson(exchange, 400, false, "Los valores deben ser números enteros.");
                return;
            }

            if (num1 < MIN_VALOR || num1 > MAX_VALOR || num2 < MIN_VALOR || num2 > MAX_VALOR) {
                enviarJson(exchange, 400, false, "Solo se permiten enteros de 5 dígitos [-99999, 99999].");
                return;
            }

            String resultado;
            switch (operacion.trim().toLowerCase()) {
                case "suma":
                    resultado = String.valueOf((long) num1 + (long) num2);
                    break;
                case "resta":
                    resultado = String.valueOf((long) num1 - (long) num2);
                    break;
                case "multiplicacion":
                    resultado = String.valueOf((long) num1 * (long) num2);
                    break;
                case "potencia":
                    if (num1 == 0 && num2 < 0) {
                        enviarJson(exchange, 400, false, "No se puede elevar 0 a una potencia negativa.");
                        return;
                    }
                    if (num2 < 0) {
                        resultado = String.valueOf(Math.pow(num1, num2));
                    } else if (num2 > 5000 && Math.abs(num1) > 1) {
                        enviarJson(exchange, 400, false, "Exponente demasiado grande.");
                        return;
                    } else {
                        resultado = BigInteger.valueOf(num1).pow(num2).toString();
                    }
                    break;
                default:
                    enviarJson(exchange, 400, false, "Operación no permitida.");
                    return;
            }

            enviarJson(exchange, 200, true, resultado);
        }
    }

    private static Map<String, String> parsearQuery(String query) {
        Map<String, String> mapa = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return mapa;
        }
        for (String par : query.split("&")) {
            String[] claveValor = par.split("=", 2);
            String clave = URLDecoder.decode(claveValor[0], StandardCharsets.UTF_8);
            String valor = claveValor.length > 1 ? URLDecoder.decode(claveValor[1], StandardCharsets.UTF_8) : "";
            mapa.put(clave, valor);
        }
        return mapa;
    }

    private static void enviarJson(HttpExchange exchange, int status, boolean ok, String mensaje) throws IOException {
        String escapado = mensaje.replace("\\", "\\\\").replace("\"", "\\\"");
        String json = ok
                ? "{\"ok\":true,\"resultado\":\"" + escapado + "\"}"
                : "{\"ok\":false,\"error\":\"" + escapado + "\"}";
        enviarRespuesta(exchange, status, "application/json; charset=UTF-8", json);
    }

    private static void enviarRespuesta(HttpExchange exchange, int status, String contentType, String cuerpo)
            throws IOException {
        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}