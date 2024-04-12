package WebSocket;

import ExceptionClasses.ResponseException;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.Client;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
//import org.eclipse.jetty.websocket.api.*;
//import org.eclipse.jetty.websocket.api.Session;
//import org.eclipse.jetty.websocket.api.annotations.*;
//import org.eclipse.jetty.websocket.client.io.ConnectionManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    GameHandler gameHandler;
    Client client;
    //ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;

    public WebSocketFacade(String url, Client client) throws ResponseException {
        try {
            this.client = client;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            container.setDefaultMaxSessionIdleTimeout(5 * 60 * 1000);
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
               //message from server
                @Override
                public void onMessage(String message) {
                    System.out.println(message);
                    ServerMessage s = new Gson().fromJson(message, ServerMessage.class);
                    client.printMessage(message, s);
                    if(s.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        LoadGame l = new Gson().fromJson(message, LoadGame.class);
                      //  EscapeSequences es = new EscapeSequences();
                       // int [][] moves = new int[8][8];
                      //  es.printWhiteBoard(l.getGame().getBoard(), moves);
                        client.updateGame(l.getGame());
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

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        try {
            //playerColor = color;
            JoinPlayer join = new JoinPlayer(gameID, color, authToken);
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

    public void resign(int gameID, String authToken) throws ResponseException {
        try {
            Resign resign = new Resign(gameID, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(resign));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}
