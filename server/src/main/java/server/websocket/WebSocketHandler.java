package server.websocket;
import com.google.gson.Gson;
import com.mysql.cj.jdbc.ConnectionGroupManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import service.GameService;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;
//import server.websocket.ConnectionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions sessions = new WebSocketSessions();
    //private GameService game;

   // private final ConnectionManager connections = new ConnectionManager();



    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand user = new Gson().fromJson(message, UserGameCommand.class);
        switch (user.getCommandType()) {
            case  JOIN_PLAYER-> {
                JoinPlayer player = new Gson().fromJson(message, JoinPlayer.class);
                joinPlayer(player.getGameID(), user.getAuthString(), session);
            }
        }
    }


    private void joinPlayer(int gameID, String authToken, Session session) throws IOException {
        sessions.add(gameID, authToken, session);
        var message = String.format("%s joined game %d", authToken, gameID);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        sessions.broadcast(authToken, message, gameID);
        sessions.sendMessage(gameID, serverMessage, authToken);
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
