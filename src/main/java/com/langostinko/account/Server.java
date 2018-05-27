package com.langostinko.account;

import com.langostinko.account.account.AccountManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private int Port_;
    private AccountManager Manager_;

    Server(int port) {
        this.Port_ = port;
        this.Manager_ = new AccountManager();
    }

    public void Start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.Port_), 0);
        server.createContext("/ping", new PingHandler());
        server.createContext("/create", new CreateHandler(Manager_));
        server.setExecutor(null);
        server.start();
    }

    private static Map<String, String> QueryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (null == query) {
            return result;
        }
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "pong";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os  = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private class CreateHandler implements HttpHandler {
        private AccountManager Manager_;
        CreateHandler(AccountManager manager) {
            this.Manager_ = manager;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = QueryToMap(exchange.getRequestURI().getRawQuery());
            int code;
            String response;
            if (!params.containsKey("id")) {
                code = 500;
                response = "id not provided";
            } else {
                try {
                    this.Manager_.Create(params.get("id"));
                    code = 200;
                    response = "ok";
                } catch (Exception e){
                    code = 501;
                    response = e.toString();
                }
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os  = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
