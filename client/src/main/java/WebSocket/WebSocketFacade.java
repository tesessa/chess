package WebSocket;

import ExceptionClasses.ResponseException;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) throws ResponseException {
        try {
            this.gameHandler = gameHandler;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
        //    System.out.println("URL: " + url);
           // System.out.println("URI: " + socketURI);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            container.setDefaultMaxSessionIdleTimeout(5 * 60 * 1000);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
               //message from server
                @Override
                public void onMessage(String message) {
                    ServerMessage s = new Gson().fromJson(message, ServerMessage.class);
                    gameHandler.printMessage(message);
                    if(s.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        LoadGame load = new Gson().fromJson(message, LoadGame.class);
                        gameHandler.updateGame(load);
                    }
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
           // send(new Gson().toJson(join));
            this.session.getBasicRemote().sendText(new Gson().toJson(join));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws ResponseException {
        try {
            JoinObserver observe = new JoinObserver(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(observe));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        try {
            Leave leaveGame = new Leave(gameID, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveGame));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(int gameID, ChessMove move, String authToken) throws ResponseException {
        try {
            MakeMove newMove = new MakeMove(gameID, move, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(newMove));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
