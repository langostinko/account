package com.langostinko.account;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8000);
        server.Start();
        System.out.println("Server started");
    }
}
