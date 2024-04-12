package clientTests;

import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.ResponseException;
import Request.CreateGameRequest;
import Request.JoinGameRequest;
import Request.LoginRequest;
import Request.LogoutRequest;
import Result.CreateGameResult;
import Result.GameInformation;
import Result.ListGameResult;
import Result.RegisterResult;
import chess.ChessGame;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Client;
import Server.ServerFacade;
import java.io.IOException;
import java.util.HashSet;
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
        //UserData data = new UserData("Jillian", "")
        Assertions.assertTrue(true);
    }

    @Test
    public void goodRegister() throws IOException {
        UserData data = new UserData("Charlie", "pass", "charlie@email.com");
        UserData data2 = new UserData("Jennifer", "p", "jenn");
        UserData data3 = new UserData("Jason", "ocean", "email");
        RegisterResult res = facade.register(data);
        RegisterResult result2 = facade.register(data2);
        RegisterResult result3 = facade.register(data3);
        Assertions.assertEquals("Jennifer", result2.username());
        Assertions.assertEquals("Jason", result3.username());
        Assertions.assertEquals("Charlie", res.username());
        Assertions.assertTrue(res.authToken().length() > 10);
        Assertions.assertTrue(result2.authToken().length() > 10);
        Assertions.assertTrue(result3.authToken().length() > 10);
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
        Assertions.assertEquals("Bob", registerResult2.username());
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
    public void goodLogout() throws IOException {
        goodRegister();
        goodCreateGame();
        LoginRequest login1 = new LoginRequest("Tessa", "password");
        LoginRequest login2 = new LoginRequest("Jennifer", "p");
        LoginRequest login3 = new LoginRequest("Jason", "ocean");
        LoginRequest login4 = new LoginRequest("Charlie", "pass");
        RegisterResult loginResult1 = facade.login(login1);
        RegisterResult loginResult2 = facade.login(login2);
        RegisterResult loginResult3 = facade.login(login3);
        RegisterResult loginResult4 = facade.login(login4);
        Assertions.assertDoesNotThrow(() -> facade.logout(loginResult1.authToken()));
        Assertions.assertDoesNotThrow(() -> facade.logout(loginResult2.authToken()));
        Assertions.assertDoesNotThrow(() -> facade.logout(loginResult3.authToken()));
        Assertions.assertDoesNotThrow(() -> facade.logout(loginResult4.authToken()));
    }

    @Test
    public void badLogout() throws IOException {
        Assertions.assertThrows(IOException.class, () -> {
            facade.logout("yay");
        });
        goodRegister();
        LoginRequest login = new LoginRequest("Jennifer", "p");
        RegisterResult loginResult = facade.login(login);
        facade.logout(loginResult.authToken());
        Assertions.assertThrows(IOException.class, () -> {
            facade.logout(loginResult.authToken());
        });
    }

    @Test
    public void listGames() throws IOException {
        goodCreateGame();
        LoginRequest login = new LoginRequest("Tessa", "password");
        RegisterResult loginResult = facade.login(login);
        CreateGameRequest request1 = new CreateGameRequest("Yay");
        CreateGameRequest request2 = new CreateGameRequest("Nope");
        CreateGameRequest request3 = new CreateGameRequest("what");
        facade.createGame(request1, loginResult.authToken());
        facade.createGame(request2, loginResult.authToken());
        facade.createGame(request3, loginResult.authToken());
        HashSet<GameInformation> expectedGames = new HashSet<GameInformation>();
        expectedGames.add(new GameInformation(1, null, null, "gameName"));
        expectedGames.add(new GameInformation(2, null, null, "Yay"));
        expectedGames.add(new GameInformation(3, null, null, "Nope"));
        expectedGames.add(new GameInformation(4, null, null, "what"));
        ListGameResult actualGames = facade.listGame(loginResult.authToken());
        Assertions.assertEquals(expectedGames, actualGames.games());

    }

    @Test
    public void badListGames() throws IOException {
        Assertions.assertThrows(IOException.class, () -> {
            facade.listGame("345");
        });
    }

    @Test
    public void goodJoinGame() throws IOException, ResponseException {
        goodCreateGame();
        goodRegister();
        LoginRequest login1 = new LoginRequest("Tessa", "password");
        LoginRequest login2 = new LoginRequest("Jennifer", "p");
        LoginRequest login3 = new LoginRequest("Jason", "ocean");
        LoginRequest login4 = new LoginRequest("Charlie", "pass");
        RegisterResult loginResult1 = facade.login(login1);
        RegisterResult loginResult2 = facade.login(login2);
        RegisterResult loginResult3 = facade.login(login3);
        RegisterResult loginResult4 = facade.login(login4);
        CreateGameRequest newGame = new CreateGameRequest("tiger");
        CreateGameResult gameid = facade.createGame(newGame, loginResult1.authToken());
        JoinGameRequest join1 = new JoinGameRequest(ChessGame.TeamColor.WHITE, 1);
        JoinGameRequest join2 = new JoinGameRequest(ChessGame.TeamColor.BLACK, 2);
        JoinGameRequest join3 = new JoinGameRequest(null, 1);
        JoinGameRequest join4 = new JoinGameRequest(ChessGame.TeamColor.BLACK, 1);
        facade.joinGame(join1, loginResult1.authToken());
        facade.joinGame(join2, loginResult2.authToken());
        facade.joinGame(join3, loginResult3.authToken());
        facade.joinGame(join4, loginResult4.authToken());
        HashSet<GameInformation> expectedGames = new HashSet<GameInformation>();
        GameInformation game1 = new GameInformation(1, "Tessa", "Charlie", "gameName");
        GameInformation game2 = new GameInformation(2, null, "Jennifer", "tiger");
        expectedGames.add(game2);
        expectedGames.add(game1);
        ListGameResult actualGames = facade.listGame(loginResult2.authToken());
        Assertions.assertEquals(expectedGames, actualGames.games());
    }

    @Test
    public void badJoinGame() throws IOException {
        goodCreateGame();
        LoginRequest login = new LoginRequest("Tessa", "password");
        RegisterResult loginResult = facade.login(login);
        JoinGameRequest join1 = new JoinGameRequest(ChessGame.TeamColor.WHITE, 3);
        JoinGameRequest join2 = new JoinGameRequest(ChessGame.TeamColor.BLACK, 1);
        JoinGameRequest join3 = new JoinGameRequest(ChessGame.TeamColor.WHITE, 0);
        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame(join1, loginResult.authToken());
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame(join2, "123");
        });
        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame(join3, loginResult.authToken());
        });
        facade.logout(loginResult.authToken());
        Assertions.assertThrows(IOException.class, () -> {
            facade.joinGame(join2, loginResult.authToken());
        });
    }
}
