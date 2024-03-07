package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeAll;
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
    private static GameService gService;
    private static UserService uService;

    @BeforeAll
    public static void setUp() throws DataAccessException {
        gameMemory = new MySqlGameDataAccess();
        userMemory = new MySqlUserDataAccess();
        authMemory = new MySqlAuthDataAccess();
        gService = new GameService(gameMemory, userMemory, authMemory);
        uService = new UserService(userMemory, authMemory);
    }

    @BeforeEach
    public void clearMemory() throws DataAccessException {
        gService.clear();
    }

    @Test
    public void goodRegister() throws AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        String username = "username";
        String password = "password";
        String email = "email";
        UserData expected = new UserData(username, password, email);
        RegisterResult actual = uService.register(username, password, email);
        AuthData actualAuth = new AuthData(actual.authToken(), actual.username());
        Assertions.assertEquals(username, actual.username());
        Assertions.assertEquals(expected, userMemory.getUser(username));
        Assertions.assertNotNull(authMemory.getAuth(actual.authToken()), String.valueOf(actualAuth));
    }

    @Test
    public void badRegister() throws AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        goodRegister();
        String username = null;
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            uService.register("username", "hey", "email");
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            uService.register(username, "hey", "email");
        });
    }
}


