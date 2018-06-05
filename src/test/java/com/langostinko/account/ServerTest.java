package com.langostinko.account;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

public class ServerTest {
    private static final int port = 8001;
    private static Server Server_;

    private String GetHTTPResponse(String action) throws IOException {
        URL url = new URL("http://localhost:" + port + "/" + action);
        URLConnection uc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        return in.readLine();
    }

    @BeforeClass
    public static void Init() throws IOException {
        int port = 8001;
        Server_ = new Server(port);
        Server_.Start();
    }

    @Test(timeout = 1000)
    public void Ping() throws IOException {
        assertEquals("pong", GetHTTPResponse("ping"));
    }

    @Test(timeout = 1000)
    public void Create() throws IOException {
        assertEquals("ok", GetHTTPResponse("create?id=my_id"));
    }

    @Test(timeout = 1000)
    public void AddSubscribe() throws IOException {
        assertEquals("ok", GetHTTPResponse("create?id=add_test"));
        assertEquals("0", GetHTTPResponse("get_count?id=add_test"));
        assertEquals("ok", GetHTTPResponse("add?id=add_test&x=100"));
        assertEquals("100", GetHTTPResponse("get_count?id=add_test"));
        assertEquals("ok", GetHTTPResponse("subscribe?id=add_test&x=42"));
        assertEquals("58", GetHTTPResponse("get_count?id=add_test"));
    }

    @Test(timeout = 1000)
    public void Transfer() throws IOException {
        assertEquals("ok", GetHTTPResponse("create?id=source"));
        assertEquals("ok", GetHTTPResponse("create?id=destination"));
        assertEquals("ok", GetHTTPResponse("add?id=source&x=100"));
        assertEquals("ok", GetHTTPResponse("add?id=destination&x=42"));
        assertEquals("ok", GetHTTPResponse("transfer?from=source&to=destination&x=27"));
        assertEquals("73", GetHTTPResponse("get_count?id=source"));
        assertEquals("69", GetHTTPResponse("get_count?id=destination"));
    }

    // 100 * (100 + 1) / 2 = 5050 transfers
    @Test (timeout = 5000)
    public void TransactionPerformance() throws IOException {
        final int CNT = 100;
        for (int i = 0; i < CNT; ++i) {
            String from = String.valueOf(i);
            assertEquals("ok", GetHTTPResponse("create?id=" + from));
            assertEquals("ok", GetHTTPResponse("add?id=" + from + "&x=" + CNT));
            for (int j = 0; j < i; ++j) {
                String to = String.valueOf(j);
                assertEquals("ok", GetHTTPResponse("transfer?from=" + from + "&to=" + to + "&x=1"));
            }
        }
    }

    /**
     * Compare this test results with TransactionPerformance test
     *   to check that most of performance depends on HTTP connection
     *   (not on the transaction itself)
    */
    @Test (timeout = 5000)
    public void PingPerformance() throws IOException {
        final int CNT = 100;
        for (int i = 0; i < CNT; ++i) {
            assertEquals("pong", GetHTTPResponse("ping"));
            assertEquals("pong", GetHTTPResponse("ping"));
            for (int j = 0; j < i; ++j) {
                assertEquals("pong", GetHTTPResponse("ping"));
            }
        }
    }

    // (8 * (8 + 1) / 2) * 140 = 5040 transfers
    @Test (timeout = 5000)
    public void ConcurrentTransactionPerformance() throws InterruptedException, IOException {
        final int THREADS = 8;
        final int CNT = 140;
        for (int i = 0; i < THREADS; i++) {
            String id = String.valueOf(i);
            assertEquals("ok", GetHTTPResponse("create?id=" + id));
            assertEquals("ok", GetHTTPResponse("add?id=" + id + "&x=" + THREADS * CNT));
        }

        ExecutorService service = Executors.newCachedThreadPool();
        for(int i = 0; i < THREADS; i++) {
            int finalI = i;
            service.submit(() -> {
                try {
                    for (int k = 0; k < CNT; ++k) {
                        String from = String.valueOf(finalI);
                        for (int j = 0; j < finalI; ++j) {
                            String to = String.valueOf(j);
                            assertEquals("ok", GetHTTPResponse("transfer?from=" + from + "&to=" + to + "&x=1"));
                        }
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);

        for (int i = 0; i < THREADS; i++) {
            String id = String.valueOf(i);
            int cnt = Integer.parseInt(GetHTTPResponse("get_count?id=" + id));
            assertEquals(THREADS * CNT + CNT * ((THREADS - i - 1) - i), cnt);
        }
    }
}
