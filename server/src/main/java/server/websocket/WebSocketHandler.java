package server.websocket;
import com.google.gson.Gson;
import com.mysql.cj.jdbc.ConnectionGroupManager;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.Timer;
public class WebSocketHandler {

   // private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> loadGame();
            case ERROR -> error();
            case NOTIFICATION -> notification();
        }
    }

    private void loadGame() {

    }

    private void error() {

    }

    private void notification() {

    }
}
