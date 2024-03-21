package clientTests;

import ExceptionClasses.ResponseException;
import Result.RegisterResult;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Client;
import Server.ServerFacade;
import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        var url = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.valueOf(url));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws IOException {
        facade.clear();
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void goodRegister() throws IOException, ResponseException {
        UserData data = new UserData("Charlie", "pass", "charlie@email.com");
        RegisterResult res = facade.register(data);
        Assertions.assertEquals("Charlie", res.username());
        Assertions.assertTrue(res.authToken().length() > 10);
    }

    @Test
    public void badRegister() throws IOException {

    }
}
