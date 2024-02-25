package server;
import dataAccess.*;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;
import Result.*;


public class Server {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;

    private final GameDataAccess gameMemory;
    private final UserService uService;

    private final GameService gService;

   // private final AuthService aService;


   // private final GameDataAccess gameData = new

   public Server() {
        userMemory = new MemoryUserDataAccess();
        authMemory = new MemoryAuthDataAccess();
        gameMemory = new MemoryGameDataAccess();
        uService = new UserService(userMemory, authMemory);
        gService = new GameService(gameMemory, userMemory, authMemory);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        RegisterResult u = uService.register(user.username(), user.password(), user.email());
        if(u.message() == "Error: already taken") {
            res.status(403);
        }
        return new Gson().toJson(u);

    }

    private Object clear(Request req, Response res) throws DataAccessException {
       ClearResult clear = gService.clear();
       return new Gson().toJson(clear);
    }
    //return "{}" when body is blank
}
