package server.websocket;
import ExceptionClasses.AlreadyTakenException;
import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.jdbc.ConnectionGroupManager;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions sessions = new WebSocketSessions();
    private final GameDataAccess gameData;
    private final AuthDataAccess authData;

    public WebSocketHandler() throws DataAccessException {
        gameData = new MySqlGameDataAccess();
        authData = new MySqlAuthDataAccess();
    }

    //private GameService game;

   // private final ConnectionManager connections = new ConnectionManager();



    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, SQLException {
        UserGameCommand user = new Gson().fromJson(message, UserGameCommand.class);
        switch (user.getCommandType()) {
            case  JOIN_PLAYER-> {
                JoinPlayer player = new Gson().fromJson(message, JoinPlayer.class);
                joinPlayer(player.getGameID(), player.getColor(), user.getAuthString(), session);
            }
            case JOIN_OBSERVER -> {
                JoinObserver observer = new Gson().fromJson(message, JoinObserver.class);
                joinObserver(observer.getGameID(), user.getAuthString(), session);
            }
        }
    }


    private void joinPlayer(int gameID, ChessGame.TeamColor playerColor, String authToken, Session session) throws IOException, DataAccessException, SQLException {
        AuthData auth = authData.getAuth(authToken);
        testValues(gameID, authToken, session);
        String username = auth.username();
        GameData d = gameData.getGame(gameID);
        System.out.println("Original username " + username + " gameData White Username "
                 + d.whiteUsername() + " gameData black username " + d.blackUsername());
        if(d.game() == null) {
            System.out.println("true");
        }
        if(playerColor == null) {
            Error error = new Error("No color");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        if(playerColor == ChessGame.TeamColor.WHITE && !String.valueOf(d.whiteUsername()).equals(username)) {
            Error error = new Error("Spot already taken");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        } else if (playerColor == ChessGame.TeamColor.BLACK && !String.valueOf(d.blackUsername()).equals(username)) {
            Error error = new Error("Spot already taken");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }
        sessions.add(gameID, authToken, session);
        var message = String.format("%s joined game %d as color %s", username, gameID, playerColor);
        sessions.broadcast(authToken, message, gameID);
        LoadGame load = new LoadGame(d.game());
        // ServerMessage serverMessage = load;
        sessions.sendMessage(gameID, load, authToken);
    }

    private void joinObserver(int gameID, String authToken, Session session) throws DataAccessException, SQLException, IOException {
        testValues(gameID, authToken, session);
        AuthData a = authData.getAuth(authToken);
        GameData d = gameData.getGame(gameID);
        sessions.add(gameID, authToken, session);
        var message = String.format("%s join game %d as observer", a.username(), gameID);
        sessions.broadcast(authToken, message, gameID);
        LoadGame load = new LoadGame(d.game());
        sessions.sendMessage(gameID, load, authToken);
    }

    private void testValues(int gameID, String authToken, Session session) throws DataAccessException, SQLException, IOException {
        AuthData auth = authData.getAuth(authToken);
        GameData d = gameData.getGame(gameID);
        if(auth == null) {
            String message = "Bad auth token";
            Error error = new Error(message);
            session.getRemote().sendString(new Gson().toJson(error));
        }
        if(d == null) {
            String message = "Game ID doesn't exist";
            Error error = new Error(message);
            session.getRemote().sendString(new Gson().toJson(error));
        }

    }

    private void error() {

    }

    private void notification() {

    }

    public void sendMessage(int gameID, String message, String authToken) {

    }

    //public void broadcastMessage(int gameID, String message) {
      //  var removeList = new ArrayList<WebSocketSessions>();

    //}

}
