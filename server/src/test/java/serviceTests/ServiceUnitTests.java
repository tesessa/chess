package serviceTests;

import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.BadRequestException;
import ExceptionClasses.UnauthorizedException;
import Result.CreateGameResult;
import Result.ErrorResult;
import Result.GameInformation;
import Result.RegisterResult;
import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestException;
import passoffTests.testClasses.TestModels;
import server.Server;
import service.GameService;
import service.UserService;
import model.*;

import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
public class ServiceUnitTests {

    private static MemoryGameDataAccess gameMemory;
    private static MemoryUserDataAccess userMemory;
    private static MemoryAuthDataAccess authMemory;
    private static GameService gService;
    private static UserService uService;

    @BeforeAll
    public static void setUp() throws DataAccessException {
        gameMemory = new MemoryGameDataAccess();
        userMemory = new MemoryUserDataAccess();
        authMemory = new MemoryAuthDataAccess();
        gService = new GameService(gameMemory, userMemory, authMemory);
        uService = new UserService(userMemory, authMemory);
    }

    @BeforeEach
    public void clearMemory() throws DataAccessException {
        gService.clear();
    }

    @Test
    public void testGoodRegister() throws AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        String username = "Tessa";
        String password = "Andersen";
        String email = "tessaeliseandersen@yahoo.com";
        UserData expected = new UserData(username, password, email);
        RegisterResult actual = uService.register(username, password, email);
        AuthData actualAuth = new AuthData(actual.authToken(), actual.username());
        Assertions.assertEquals(username, actual.username());
        Assertions.assertEquals(expected, userMemory.getUser(username));
        Assertions.assertNotNull(authMemory.getAuth(actual.authToken()), String.valueOf(actualAuth));

    }

    @Test
    public void testBadRegister() throws AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        uService.register("Tessa", "hey", "email");
        String email = null;
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            uService.register("Tessa", "hey", "email");
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            uService.register("", "hey", email );
        });
    }

    @Test
    public void testGoodLogin() throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        String username = "Tessa";
        String password = "password";
        uService.register(username, password, "email");
        RegisterResult actualLogin = uService.login(username, password);
        AuthData actualAuth = new AuthData(actualLogin.authToken(), actualLogin.username());
        Assertions.assertEquals(username, actualLogin.username());
        Assertions.assertEquals(authMemory.getAuth(actualLogin.authToken()), actualAuth);
    }

    @Test
    public void testBadLogin() throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        uService.register("Tessa", "password", "email");
        UserData expected = new UserData("Tessa", "password", "email");
        Assertions.assertEquals(expected, userMemory.getUser("Tessa"));
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            uService.login("Username", "password");
        });
    }

    @Test
    public void testGoodLogout() throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
       AuthData actual;
       RegisterResult register =  uService.register("Tessa", "password", "email");
       actual = new AuthData(register.authToken(), register.username());
       Assertions.assertEquals(actual, authMemory.getAuth(register.authToken()));
       RegisterResult login = uService.login("Tessa", "password");
       actual = new AuthData(login.authToken(), login.username());
       Assertions.assertEquals(actual, authMemory.getAuth(login.authToken()));
       ErrorResult actualResult = uService.logout(login.authToken());
       Assertions.assertEquals("", actualResult.message());
       Assertions.assertEquals(null, authMemory.getAuth(login.authToken()));
    }

    @Test
    public void testBadLogout() throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException {
        AuthData newAuth = authMemory.createAuth("Tessa");
        ErrorResult success = uService.logout(newAuth.authToken());
        Assertions.assertEquals("", success.message());
        Assertions.assertEquals(null, authMemory.getAuth(newAuth.authToken()));
        String fakeAuth = "123";
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            uService.logout(fakeAuth);
        });
    }

    @Test
    public void testGoodCreateGame() throws BadRequestException, UnauthorizedException, DataAccessException {
        AuthData newAuth = authMemory.createAuth("Tessa");
        CreateGameResult gameResult = gService.createGame("gameName", newAuth.authToken());
        GameData gameInMemory = gameMemory.getGame(gameResult.gameID());
        Assertions.assertEquals(gameResult.gameID(), gameInMemory.gameID());
        Assertions.assertEquals("gameName", gameInMemory.gameName());
    }

    @Test
    public void testBadCreateGame() throws BadRequestException, UnauthorizedException {
        String fakeAuth = "123";
        String nullName = null;
        AuthData newAuth = authMemory.createAuth("Tessa");
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gService.createGame("gameName", fakeAuth);
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            gService.createGame(nullName, newAuth.authToken());
        });
    }

    @Test
    public void testGoodListGames() throws BadRequestException, UnauthorizedException {
        HashSet<GameInformation> expectedGames = new HashSet<GameInformation>();
        HashSet<GameInformation> actualGames = new HashSet<GameInformation>();
        AuthData newAuth = authMemory.createAuth("username");
        for(int i = 0; i < 5; i++) {
            int gameID = i + 1;
            String gameName = "game" + String.valueOf(gameID);
            gService.createGame(gameName, newAuth.authToken());
            expectedGames.add(new GameInformation(gameID, null, null, gameName));
        }
        actualGames = gService.listGames(newAuth.authToken());
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    public void testBadListGames() throws UnauthorizedException {
        String fakeAuth = "123";
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gService.listGames(fakeAuth);
        });
    }

    @Test
    public void testGoodJoinGame() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        HashSet<GameInformation> expectedGames = new HashSet<GameInformation>();
        HashSet<GameInformation> actualGames = new HashSet<GameInformation>();
        AuthData newAuth = authMemory.createAuth("Tessa");
        expectedGames.add(new GameInformation(2, null, null, "game2"));
        expectedGames.add(new GameInformation(1, null, "Tessa", "game1"));
        gService.createGame("game1", newAuth.authToken());
        gService.createGame("game2", newAuth.authToken());
        gService.joinGame(ChessGame.TeamColor.BLACK, 1, newAuth.authToken());
        actualGames = gService.listGames(newAuth.authToken());
        Assertions.assertEquals(expectedGames,actualGames);
        // test with more variables
        expectedGames.clear();
        gService.clear();
        actualGames.clear();
        AuthData user1 = authMemory.createAuth("Jake");
        AuthData user2 = authMemory.createAuth("Austin");
        AuthData user3 = authMemory.createAuth("Nick");
        AuthData user4 = authMemory.createAuth("Tessa");
        expectedGames.add(new GameInformation(3, "Jake", null, "Game3"));
        expectedGames.add(new GameInformation(2, null, "Nick", "Game2"));
        expectedGames.add(new GameInformation(1,"Austin", "Tessa", "Game1"));
        gService.createGame("Game1", user1.authToken());
        gService.createGame("Game2", user2.authToken());
        gService.createGame("Game3", user4.authToken());
        gService.joinGame(ChessGame.TeamColor.WHITE, 1, user2.authToken());
        gService.joinGame(ChessGame.TeamColor.WHITE, 3, user1.authToken());
        gService.joinGame(ChessGame.TeamColor.BLACK, 2, user3.authToken());
        gService.joinGame(ChessGame.TeamColor.BLACK, 1, user4.authToken());
        actualGames = gService.listGames(user3.authToken());
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    public void testBadJoinGame() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException{
        testGoodJoinGame();
        AuthData authToken = authMemory.createAuth("user");
        String fakeAuth = "123";
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gService.joinGame(ChessGame.TeamColor.WHITE, 2, fakeAuth);
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            gService.joinGame(ChessGame.TeamColor.BLACK, 5, authToken.authToken());
        });
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            gService.joinGame(ChessGame.TeamColor.WHITE, 1, authToken.authToken());
        });
    }

    @Test
    public void clear() throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        HashSet<GameInformation> emptyGameList = new HashSet<GameInformation>();
        HashSet<GameInformation> emptyList = new HashSet<GameInformation>();
        testGoodJoinGame();
        AuthData newAuth = authMemory.createAuth("Temporary");
        uService.register("Tessa", "password", "email");
        gService.clear();
        AuthData emptyAuth = authMemory.getAuth(newAuth.authToken());
        GameData emptyGame = gameMemory.getGame(1);
        emptyGameList = gameMemory.listGames();
        UserData emptyUser = userMemory.getUser("Tessa");
        Assertions.assertEquals(null, emptyAuth);
        Assertions.assertEquals(null, emptyGame);
        Assertions.assertEquals(emptyList, emptyGameList);
        Assertions.assertEquals(null, emptyUser);
    }









}
