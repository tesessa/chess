package server;
import ExceptionClasses.AlreadyTakenException;
import dataAccess.*;
import model.*;
import service.*;
import spark.*;
import com.google.gson.Gson;
import Result.*;
import ExceptionClasses.*;
import Request.*;

import java.util.HashSet;


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
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    private Object register(Request req, Response res) {
       RegisterResult u;
       try {
           var user = new Gson().fromJson(req.body(), UserData.class);
           u = uService.register(user.username(), user.password(), user.email());
       } catch(AlreadyTakenException e) {
           res.status(403);
           u = new RegisterResult(null, null, e.getMessage());
       } catch(BadRequestException e) {
            res.status(400);
            u = new RegisterResult(null, null, e.getMessage());
       }
       return new Gson().toJson(u);
    }

    private Object login(Request req, Response res) {
       RegisterResult u;
       try {
           var user = new Gson().fromJson(req.body(), LoginRequest.class);
           u = uService.login(user.username(), user.password());
       } catch(UnauthorizedException e) {
           res.status(401);
           u = new RegisterResult(null, null, e.getMessage());
       }
       return new Gson().toJson(u);
    }

   private Object logout(Request req, Response res) throws UnauthorizedException {
       ErrorResult logout;
       try {
           String auth = req.headers("authorization");
           logout = uService.logout(auth);
       } catch(UnauthorizedException e) {
           res.status(401);
           logout = new ErrorResult(e.getMessage());
       }
       return new Gson().toJson(logout);
    }

    private Object createGame(Request req, Response res) {
       CreateGameResult gameID;
       ErrorResult error;
       try {
           var gameName = new Gson().fromJson(req.body(), CreateGameRequest.class);
           String auth = req.headers("authorization");
           gameID = gService.createGame(gameName.gameName(), auth);
       } catch(UnauthorizedException e) {
           res.status(401);
           error = new ErrorResult(e.getMessage());
           return new Gson().toJson(error);
       } catch(BadRequestException e) {
           res.status(400);
           error = new ErrorResult(e.getMessage());
           return new Gson().toJson(error);
       }
       return new Gson().toJson(gameID);
    }

    private Object joinGame(Request req, Response res) {
       ErrorResult error;
       try {
           var join = new Gson().fromJson(req.body(), JoinGameRequest.class);
           String auth = req.headers("authorization");
           error = gService.joinGame(join.playerColor(), join.gameID(), auth);
       } catch (UnauthorizedException e) {
           res.status(401);
           error = new ErrorResult(e.getMessage());
       } catch (BadRequestException e) {
           res.status(400);
           error = new ErrorResult(e.getMessage());
       } catch (AlreadyTakenException e) {
           res.status(403);
           error = new ErrorResult(e.getMessage());
       }
       return new Gson().toJson(error);
    }

    private Object listGames(Request req, Response res)  {
       ErrorResult error;
       HashSet<GameInformation> gamesList;
       ListGameResult games;
        try {
            String auth = req.headers("authorization");
            gamesList = gService.listGames(auth);
            games = new ListGameResult(gamesList);
        } catch (UnauthorizedException e) {
            res.status(401);
            error = new ErrorResult(e.getMessage());
            return new Gson().toJson(error);
        }
        return new Gson().toJson(games);
    }

    private Object clear(Request req, Response res) {
       ErrorResult clear;
       try {
           clear = gService.clear();
       } catch(DataAccessException e) {
           res.status(500);
           clear = new ErrorResult("Error: uh oh");
       }
       return new Gson().toJson(clear);
    }
    //return "{}" when body is blank
}
