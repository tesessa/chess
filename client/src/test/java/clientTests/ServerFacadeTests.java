package clientTests;

import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.ResponseException;
import Request.CreateGameRequest;
import Request.LoginRequest;
import Result.CreateGameResult;
import Result.RegisterResult;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Client;
import Server.ServerFacade;
import java.io.IOException;
import java.util.concurrent.Callable;


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
    public void goodRegister() throws IOException {
        UserData data = new UserData("Charlie", "pass", "charlie@email.com");
        RegisterResult res = facade.register(data);
        Assertions.assertEquals("Charlie", res.username());
        Assertions.assertTrue(res.authToken().length() > 10);
    }

    @Test
    public void badRegister() throws IOException {
        goodRegister();
        UserData empty = new UserData(null, null, "hey");
        UserData alreadyTaken = new UserData("Charlie", "pass", "charlie@email.com");
        Assertions.assertThrows(IOException.class, () -> {
            facade.register(alreadyTaken);
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.register(empty);
        });
    }

    @Test
    public void goodLogin() throws IOException {
        goodRegister();
        UserData register2 = new UserData("Bob", "pass", "charlie@gmail.com");
        LoginRequest login2 = new LoginRequest("Bob", "pass");
        LoginRequest data = new LoginRequest("Charlie", "pass");
        RegisterResult registerResult2 = facade.register(register2);
        RegisterResult loginResult2 = facade.login(login2);
        RegisterResult loginResult = facade.login(data);
        Assertions.assertEquals("Charlie", loginResult.username());
        Assertions.assertTrue(loginResult.authToken().length() > 10);
        Assertions.assertEquals("Bob",registerResult2.username());
        Assertions.assertTrue(registerResult2.authToken().length() > 10);
        Assertions.assertEquals("Bob", loginResult2.username());
        Assertions.assertTrue(loginResult2.authToken().length() > 10);
        RegisterResult loginResult3 = facade.login(data);
        Assertions.assertEquals("Charlie", loginResult3.username());
        Assertions.assertNotEquals(loginResult.authToken(), loginResult3.authToken());
    }

    @Test
    public void badLogin() throws IOException {
        goodRegister();
        UserData user2 = new UserData("Lily", "river", "lily@mail.com");
        RegisterResult registerUser2 = facade.register(user2);
        LoginRequest loginUser1 = new LoginRequest("Charlie", "river");
        LoginRequest empty = new LoginRequest("Lily", null);
        LoginRequest notRegistered = new LoginRequest("user", "pass");
        Assertions.assertThrows(IOException.class, () -> {
            facade.login(loginUser1);
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.login(empty);
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.login(notRegistered);
        });
    }

    @Test
    public void goodCreateGame() throws IOException {
        UserData data = new UserData("Tessa", "password", "email");
        LoginRequest login = new LoginRequest("Tessa", "password");
        facade.register(data);
        RegisterResult loginResult = facade.login(login);
        CreateGameRequest request = new CreateGameRequest("gameName");
        CreateGameResult result = facade.createGame(request, loginResult.authToken());
        Assertions.assertTrue(result.gameID() > 0);
        Assertions.assertEquals(1, result.gameID());
    }

    @Test
    public void badCreateGame() throws IOException {
        UserData data = new UserData("Tessa", "password", "email");
        LoginRequest login = new LoginRequest("Tessa", "password");
        facade.register(data);
        RegisterResult loginResult = facade.login(login);
        CreateGameRequest empty = new CreateGameRequest(null);
        CreateGameRequest test = new CreateGameRequest("name");
        Assertions.assertThrows(IOException.class, () -> {
            facade.createGame(empty, loginResult.authToken());
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.createGame(test, "123");
        });
        CreateGameResult game = facade.createGame(test, loginResult.authToken());
        Assertions.assertNotEquals(2, game.gameID());
    }

    @Test
    public void goodJoinGame() {

    }
}
