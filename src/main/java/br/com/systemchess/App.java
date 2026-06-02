package br.com.systemchess;

import br.com.systemchess.controller.TorneioController;
import br.com.systemchess.server.WebServer;
import br.com.systemchess.service.TorneioService;

public class App {

    public static void main(String[] args) {
        int porta = definirPorta(args);
        TorneioService service = TorneioService.criarPadrao();
        TorneioController controller = new TorneioController(service);
        WebServer server = new WebServer(porta, controller);
        server.iniciar();
    }

    private static int definirPorta(String[] args) {
        if (args.length == 0) {
            return 8080;
        }

        try {
            int porta = Integer.parseInt(args[0]);
            if (porta < 1024 || porta > 65535) {
                return 8080;
            }
            return porta;
        } catch (NumberFormatException ex) {
            return 8080;
        }
    }
}
