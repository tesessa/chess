package WebSocket;

import ExceptionClasses.ResponseException;
import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;

    public WebSocketFacade(String url) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
               //message from server
                @Override
                public void onMessage(String message) {
                    ServerMessage s = new Gson().fromJson(message, ServerMessage.class);

                }
            });

        } catch (URISyntaxException | DeploymentException | IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }



    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        try {
            UserGameCommand msg = new UserGameCommand(authToken);
            JoinPlayer join = new JoinPlayer(gameID, color, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(join));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
       // var joinGame = new JoinPlayer();
    }

}
