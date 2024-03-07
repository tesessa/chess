package serviceTests;

import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import service.GameService;
import service.UserService;
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
import java.util.HashSet;
import java.sql.SQLException;

public class ServiceSQLDaoTests {
    private static MySqlUserDataAccess userMemory;
    private static MySqlAuthDataAccess authMemory;
    private static MySqlGameDataAccess gameMemory;

    @BeforeAll
    public static void setUp() throws DataAccessException {
        gameMemory = new MySqlGameDataAccess();
        userMemory = new MySqlUserDataAccess();
        authMemory = new MySqlAuthDataAccess();
    }

    @BeforeEach
    public void clearMemory() throws DataAccessException {
        gameMemory.clear();
        userMemory.clear();
        authMemory.clear();
    }

    @Test
    public void goodCreateUser() throws DataAccessException, SQLException {
        String password = "password";
        userMemory.createUser("username", password, "email");
        UserData user = userMemory.getUser("username");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        Assertions.assertEquals(true, userMemory.verifyUser("username", "password"));
        Assertions.assertEquals("username", user.username());
        Assertions.assertEquals("email", user.email());
    }

    @Test
    public void badCreateUser() throws DataAccessException, SQLException {
        String email = null;
        Assertions.assertThrows(DataAccessException.class, () -> {
            userMemory.createUser("username", "password", null);
        });
        goodCreateUser();
        Assertions.assertThrows(DataAccessException.class, () -> {
            userMemory.createUser("username", "password", "email");
        });
    }

    @Test
    public void goodGetUser() throws DataAccessException, SQLException{
        goodCreateUser();
        userMemory.createUser("Tessa", "hey", "hi");
        UserData expected1 = new UserData("username", "password", "email");
        UserData expected2 = new UserData("Tessa", "hey", "hi");
        UserData actual1 = userMemory.getUser("username");
        UserData actual2 = userMemory.getUser("Tessa");
        Assertions.assertEquals(expected1.username(), actual1.username());
        Assertions.assertEquals(expected1.email(), actual1.email());
        Assertions.assertEquals(true, userMemory.verifyUser(expected1.username(), expected1.password()));
        Assertions.assertEquals(true, userMemory.verifyUser(expected2.username(), expected2.password()));
        Assertions.assertEquals(expected2.username(), actual2.username());
        Assertions.assertEquals(expected2.email(), actual2.email());
    }

    @Test
    public void badGetUser() throws DataAccessException, SQLException {
        UserData actual = userMemory.getUser("Bob");
        Assertions.assertEquals(null, actual);
        Assertions.assertEquals(null, userMemory.getUser(null));
    }

    @Test
    public void goodCheckPassword() throws DataAccessException, SQLException {
        goodCreateUser();
        String actual = userMemory.checkPassword("password", "username");
        String expected = "username";
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void badCheckPassword() throws DataAccessException, SQLException {
        String fail = userMemory.checkPassword("what", "where");
        Assertions.assertEquals(null, fail);

    }

    @Test
    public void goodClearUser() throws DataAccessException, SQLException {
        goodCreateUser();
        userMemory.clear();
        Assertions.assertEquals(null, userMemory.getUser("username"));
    }

    /*
    Here are the auth tests
     */
    @Test
    public void goodCreateAuth() throws DataAccessException {
        String username = "username";
        AuthData actual = authMemory.createAuth(username);
        AuthData expected = new AuthData(actual.authToken(), username);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void badCreateAuth() throws DataAccessException {
        String badUsername = null;
        Assertions.assertThrows(DataAccessException.class, () -> {
            authMemory.createAuth(badUsername);
        });
    }

    @Test
    public void goodGetAuth() throws DataAccessException, SQLException {
        String username = "username";
        AuthData expected = authMemory.createAuth(username);
        AuthData actual = authMemory.getAuth(expected.authToken());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void badGetAuth() throws DataAccessException, SQLException {
        AuthData actual = authMemory.getAuth("fake");
        Assertions.assertEquals(null, actual);
    }

    @Test
    public void goodDeleteAuth() throws DataAccessException, SQLException {
        AuthData auth = authMemory.createAuth("username");
        authMemory.deleteAuth(auth.authToken());
        Assertions.assertEquals(null, authMemory.getAuth(auth.authToken()));
    }

  /*  @Test
    public void badDeleteAuth() throws DataAccessException {
        authMemory.deleteAuth(null);
       /* Assertions.assertThrows(DataAccessException.class, () -> {
            authMemory.deleteAuth("fake");
        });
    }*/

    @Test
    public void clearAuth() throws DataAccessException, SQLException {
        AuthData temp = authMemory.createAuth("username");
        authMemory.clear();
        Assertions.assertEquals(null, authMemory.getAuth(temp.authToken()));
    }

    //game tests
    @Test
    public void goodCreateGame() throws DataAccessException {
        int gameID = gameMemory.createGame("gameName");
        Assertions.assertEquals(1, gameID);
        int gameID2 = gameMemory.createGame("game2");
        Assertions.assertEquals(2, gameID2);
    }

    @Test
    public void badCreateGame() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameMemory.createGame(null);
        });
    }

    @Test
    public void goodGetGame() throws DataAccessException, SQLException {
        goodCreateGame();
        ChessGame game = new ChessGame();
        GameData actual = gameMemory.getGame(1);
        GameData expected = new GameData(1, null, null, "gameName", actual.game());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void badGetGame() throws DataAccessException {
        Assertions.assertEquals(null, gameMemory.getGame(1));
    }

    @Test
    public void goodUpdateGame() throws DataAccessException {
        int gameID = gameMemory.createGame("gameName");
        GameData data = gameMemory.getGame(gameID);
        gameMemory.updateGame(data, "newUser", ChessGame.TeamColor.WHITE);
        GameData actual1 = gameMemory.getGame(1);
        GameData expected1 = new GameData(1,"newUser", null, "gameName", actual1.game());
        Assertions.assertEquals(expected1, actual1);
        gameMemory.updateGame(actual1, "User", ChessGame.TeamColor.BLACK);
        GameData actual2 = gameMemory.getGame(1);
        GameData expected2 = new GameData(1, "newUser", "User", "gameName", actual2.game());
        Assertions.assertEquals(expected2, actual2);
        gameMemory.updateGame(actual2, "user", null);
        GameData actual3 = gameMemory.getGame(1);
        GameData expected3 = new GameData(1, "newUser", "User", "gameName", actual3.game());
        Assertions.assertEquals(expected3, actual3);
    }

    @Test
    public void badUpdateGame() throws DataAccessException, SQLException {
        Assertions.assertThrows(NullPointerException.class, ()-> {
           gameMemory.updateGame(null, "user", ChessGame.TeamColor.WHITE);
        });
    }

    @Test
    public void goodListGames() {
        
    }

    @Test
    public void badListGames() {

    }

    @Test
    public void clearGame() {

    }
}


