package server;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.MemoryUserDataAccess;
import dataAccess.UserDataAccess;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;


public class Server {
    //private final UserService userService;
    private final UserService user;
    public Server(UserDataAccess userData) {
        //UserDataAccess userData;
        user = new UserService(userData);

    }

   // private final GameDataAccess gameData = new

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //Spark.post("/user", this::register);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object register(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        user = user.register(user);
        return new Gson().toJson(user);

    }
}
