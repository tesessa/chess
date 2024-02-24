package server;
import dataAccess.*;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;


public class Server {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;
    private final UserService uService;


   // private final GameDataAccess gameData = new

   public Server() {
        userMemory = new MemoryUserDataAccess();
        authMemory = new MemoryAuthDataAccess();
        uService = new UserService(userMemory, authMemory);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.post("/user", this::register);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        String u = uService.register(user.username(), user.password(), user.email());
        return new Gson().toJson(u);

    }
}
