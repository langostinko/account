package com.langostinko.account;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static junit.framework.TestCase.assertEquals;

public class ServerTest {
    @Test
    public void Ping() throws IOException {
        int port = 8001;
        Server server = new Server(port);
        server.Start();

        URL yahoo = new URL("http://localhost:" + port + "/ping");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String response = in.readLine();
        assertEquals("pong", response);
    }
}
