package faculdade.faculdade;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class ApiServer {

    private static final String URL = "jdbc:sqlite:uirf.db";

    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {

        SalaDAO salaDAO = new SalaDAO(URL);

        salaDAO.init();

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );

        server.createContext("/salas", exchange -> {

            // Permite  que o HTML acesse  a API
            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin",
                    "*"
            );

            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers",
                    "Content-Type"
            );

            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "GET, POST, PATCH, DELETE, OPTIONS"
            );

            // OPTIONS é usado pelo navegador antes de algumas requisições
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {

                String metodo = exchange.getRequestMethod();

                // GET /salas
                if (metodo.equals("GET")) {

                    listarSalas(exchange, salaDAO);

                }

                // POST /salas
                else if (metodo.equals("POST")) {

                    criarSala(exchange, salaDAO);

                }

                // PATCH /salas/1
                else if (metodo.equals("PATCH")) {

                    atualizarSala(exchange, salaDAO);

                }

                // DELETE /salas/1
                else if (metodo.equals("DELETE")) {

                    deletarSala(exchange, salaDAO);

                }

                else {

                    responder(
                            exchange,
                            405,
                            "{\"erro\":\"Método não permitido\"}"
                    );

                }

            } catch (Exception e) {

                e.printStackTrace();

                responder(
                        exchange,
                        500,
                        "{\"erro\":\"Erro no servidor\"}"
                );
            }
        });

        server.start();

        System.out.println("=================================");
        System.out.println("API iniciada!");
        System.out.println("http://localhost:8080");
        System.out.println("=================================");
    }


    // GET
    private static void listarSalas(
            HttpExchange exchange,
            SalaDAO salaDAO
    ) throws SQLException, IOException {

        var salas = salaDAO.listAll();

        String json = gson.toJson(salas);

        responder(
                exchange,
                200,
                json
        );
    }


    // POST
    private static void criarSala(
            HttpExchange exchange,
            SalaDAO salaDAO
    ) throws IOException, SQLException {

        String corpo = lerCorpo(exchange);

        Sala sala = gson.fromJson(corpo, Sala.class);

        int id = salaDAO.create(sala);

        String json = gson.toJson(sala);

        responder(
                exchange,
                201,
                json
        );

        System.out.println("Sala criada: " + id);
    }


    // PATCH
    private static void atualizarSala(
            HttpExchange exchange,
            SalaDAO salaDAO
    ) throws IOException, SQLException {

        int id = pegarId(exchange);

        if (id == -1) {

            responder(
                    exchange,
                    400,
                    "{\"erro\":\"ID inválido\"}"
            );

            return;
        }

        String corpo = lerCorpo(exchange);

        Sala sala = gson.fromJson(corpo, Sala.class);

        sala.setId(id);

        boolean atualizou = salaDAO.update(sala);

        if (atualizou) {

            responder(
                    exchange,
                    200,
                    gson.toJson(sala)
            );

        } else {

            responder(
                    exchange,
                    404,
                    "{\"erro\":\"Sala não encontrada\"}"
            );
        }
    }


    // DELETE
    private static void deletarSala(
            HttpExchange exchange,
            SalaDAO salaDAO
    ) throws IOException, SQLException {

        int id = pegarId(exchange);

        if (id == -1) {

            responder(
                    exchange,
                    400,
                    "{\"erro\":\"ID inválido\"}"
            );

            return;
        }

        boolean apagou = salaDAO.delete(id);

        if (apagou) {

            responder(
                    exchange,
                    200,
                    "{\"mensagem\":\"Sala excluída com sucesso\"}"
            );

        } else {

            responder(
                    exchange,
                    404,
                    "{\"erro\":\"Sala não encontrada\"}"
            );
        }
    }


    // PEGAR ID DA URL
    private static int pegarId(HttpExchange exchange) {

        String caminho = exchange.getRequestURI().getPath();

        String[] partes = caminho.split("/");

        try {

            if (partes.length >= 3) {

                return Integer.parseInt(partes[2]);

            }

        } catch (NumberFormatException e) {

            return -1;
        }

        return -1;
    }


    // LER JSON
    private static String lerCorpo(
            HttpExchange exchange
    ) throws IOException {

        InputStream entrada = exchange.getRequestBody();

        return new String(
                entrada.readAllBytes(),
                StandardCharsets.UTF_8
        );
    }


    // RESPONDER
    private static void responder(
            HttpExchange exchange,
            int codigo,
            String resposta
    ) throws IOException {

        byte[] dados = resposta.getBytes(
                StandardCharsets.UTF_8
        );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        exchange.sendResponseHeaders(
                codigo,
                dados.length
        );

        OutputStream saida = exchange.getResponseBody();

        saida.write(dados);

        saida.close();
    }
}