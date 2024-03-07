package serviceTests;

import dataAccess.*;
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
       // Assertions.assertEquals(hashedPassword, user.password());
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
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @Test
    public void badGetUser() {

    }

    @Test
    public void goodCheckPassword() {

    }

    @Test
    public void badCheckPassword() {

    }

    @Test
    public void goodClearUser() {

    }

    @Test
    public void badClearUser() {

    }


}


