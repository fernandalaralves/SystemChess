package br.com.systemchess.server;

import br.com.systemchess.controller.TorneioController;
import br.com.systemchess.util.Json;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public final class WebServer {

    private final int porta;
    private final TorneioController controller;

    public WebServer(int porta, TorneioController controller) {
        this.porta = porta;
        this.controller = controller;
    }

    public void iniciar() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);
            server.createContext("/", this::rotear);
            server.setExecutor(null);
            server.start();
            System.out.println("SystemChess em http://localhost:" + porta);
        } catch (IOException ex) {
            throw new IllegalStateException("Nao foi possivel iniciar o servidor", ex);
        }
    }

    private void rotear(HttpExchange exchange) throws IOException {
        try {
            String metodo = exchange.getRequestMethod();
            String caminho = exchange.getRequestURI().getPath();

            if ("GET".equals(metodo) && "/api/health".equals(caminho)) {
                responderJson(exchange, 200, Json.mensagem("ok"));
                return;
            }
            if ("GET".equals(metodo) && "/api/torneio".equals(caminho)) {
                responderJson(exchange, 200, controller.obterTorneio());
                return;
            }
            if ("POST".equals(metodo) && "/api/jogadores".equals(caminho)) {
                responderJson(exchange, 201, controller.cadastrarJogador(corpo(exchange)));
                return;
            }
            if ("POST".equals(metodo) && "/api/rodadas".equals(caminho)) {
                responderJson(exchange, 201, controller.gerarRodada());
                return;
            }
            if ("POST".equals(metodo) && caminho.matches("/api/partidas/\\d+/resultado")) {
                int partidaId = extrairIdPartida(caminho);
                responderJson(exchange, 200, controller.registrarResultado(partidaId, corpo(exchange)));
                return;
            }
            if ("POST".equals(metodo) && "/api/reiniciar".equals(caminho)) {
                responderJson(exchange, 200, controller.reiniciar(corpo(exchange)));
                return;
            }
            servirArquivo(exchange, caminho);
        } catch (IllegalArgumentException ex) {
            responderJson(exchange, 400, Json.erro(ex.getMessage()));
        } catch (IllegalStateException ex) {
            responderJson(exchange, 409, Json.erro(ex.getMessage()));
        } catch (RuntimeException ex) {
            responderJson(exchange, 500, Json.erro("Erro interno"));
        }
    }

    private int extrairIdPartida(String caminho) {
        String[] partes = caminho.split("/");
        return Integer.parseInt(partes[3]);
    }

    private String corpo(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private void responderJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }

    private void servirArquivo(HttpExchange exchange, String caminho) throws IOException {
        String recurso = "/public" + ("/".equals(caminho) ? "/index.html" : caminho);
        try (InputStream input = WebServer.class.getResourceAsStream(recurso)) {
            if (input == null) {
                responderJson(exchange, 404, Json.erro("Rota nao encontrada"));
                return;
            }
            byte[] bytes = input.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", contentType(recurso));
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(bytes);
            }
        }
    }

    private String contentType(String recurso) {
        if (recurso.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (recurso.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        return "text/html; charset=utf-8";
    }
}
