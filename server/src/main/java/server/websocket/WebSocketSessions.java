package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
public class WebSocketSessions {
    public final HashMap<Integer, HashMap<String, Session>> sessions = new HashMap<>();
  //  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
    public void add(int gameID, String authToken, Session session) {
        //var connection = new Connection(authToken, session);
        Integer id = Integer.valueOf(gameID);
        HashMap<String, Session> temp = new HashMap<>();
        temp.put(authToken, session);
        sessions.put(gameID, temp);
        //connections.put(authToken, connection);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {

        Integer id = Integer.valueOf(gameID);
        HashMap<String, Session> temp = sessions.get(id);
        temp.remove(authToken);
        //sessions.remove()
        //connections.remove(authToken);
    }

    public HashMap<String, Session> getSessionsForGame(int gameID) {
        Integer id = Integer.valueOf(gameID);
        return sessions.get(id);
    }

    public void sendMessage(int gameID, ServerMessage message, String authToken) throws IOException {
       HashMap<String, Session> temp = sessions.get(gameID);
       temp.get(authToken).getRemote().sendString(new Gson().toJson(message.toString()));

    }


    public void broadcast(String excludeAuthToken, String message, int gameID) throws IOException {
        HashMap<String, Session> users = sessions.get(gameID);
        for(var auth : users.keySet()) {
            if(!auth.equals(excludeAuthToken)) {
                Notification notify = new Notification(message);
                users.get(auth).getRemote().sendString(new Gson().toJson(notify));
            }
        }
    }

}
