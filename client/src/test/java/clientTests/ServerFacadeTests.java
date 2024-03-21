package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import ui.PreloginUI;


public class ServerFacadeTests {

    private static Server server;
    static PreloginUI clientPrelogin;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        var url = "http://localhost:" + port;
        clientPrelogin = new PreloginUI(url);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void Register() {
        
    }

}
