package com.langostinko.account;

import com.langostinko.account.account.AccountManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

class Server {
    private int Port_;
    private AccountManager Manager_;

    Server(int port) {
        this.Port_ = port;
        this.Manager_ = new AccountManager();
    }

    void Start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.Port_), 0);
        server.createContext("/ping", new PingHandler());
        server.createContext("/create", new CreateHandler(Manager_));
        server.createContext("/add", new AddHandler(Manager_));
        server.createContext("/subscribe", new SubscribeHandler(Manager_));
        server.createContext("/transfer", new TransferHandler(Manager_));
        server.createContext("/get_count", new GetCountHandler(Manager_));
        server.setExecutor(null);
        server.start();
    }

    private static Map<String, String> QueryToMap(String query) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        if (null == query) {
            return result;
        }
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], java.net.URLDecoder.decode(entry[1], "UTF-8"));
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    private static String GetParameter(final Map<String, String> map, String key) throws ParameterNotDefinedException {
        String result = map.get(key);
        if (null == result) {
            throw new ParameterNotDefinedException(key);
        }
        return result;
    }

    private static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "pong";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
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
            try {
                this.Manager_.Create(GetParameter(params, "id"));
                code = 200;
                response = "ok";
            } catch (Exception e){
                code = 501;
                response = e.toString();
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private class AddHandler implements HttpHandler {
        private AccountManager Manager_;
        AddHandler(AccountManager manager) {
            this.Manager_ = manager;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = QueryToMap(exchange.getRequestURI().getRawQuery());
            int code;
            String response;
            try {
                this.Manager_.Add(GetParameter(params, "id"), Integer.parseInt(GetParameter(params, "x")));
                code = 200;
                response = "ok";
            } catch (Exception e){
                code = 501;
                response = e.toString();
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private class TransferHandler implements HttpHandler {
        private AccountManager Manager_;
        TransferHandler(AccountManager manager) {
            this.Manager_ = manager;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = QueryToMap(exchange.getRequestURI().getRawQuery());
            int code;
            String response;
            try {
                this.Manager_.Transfer(GetParameter(params, "from"), GetParameter(params, "to"), Integer.parseInt(GetParameter(params, "x")));
                code = 200;
                response = "ok";
            } catch (Exception e){
                code = 501;
                response = e.toString();
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private class GetCountHandler implements HttpHandler {
        private AccountManager Manager_;
        GetCountHandler(AccountManager manager_) {
            this.Manager_ = manager_;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = QueryToMap(exchange.getRequestURI().getRawQuery());
            int code;
            String response;
            try {
                code = 200;
                response = String.valueOf(this.Manager_.GetCount(GetParameter(params, "id")));
            } catch (Exception e){
                code = 501;
                response = e.toString();
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private class SubscribeHandler implements HttpHandler {
        private AccountManager Manager_;
        SubscribeHandler(AccountManager manager) {
            this.Manager_ = manager;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> params = QueryToMap(exchange.getRequestURI().getRawQuery());
            int code;
            String response;
            try {
                this.Manager_.Subscribe(GetParameter(params, "id"), Integer.parseInt(GetParameter(params, "x")));
                response = "ok";
                code = 200;
            } catch (Exception e){
                code = 501;
                response = e.toString();
            }
            exchange.sendResponseHeaders(code, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
