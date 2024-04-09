package WebSocket;

import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade {
    Session session;

    public WebSocketFacade(String url)  {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            /*this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void
            }*/

        } catch (URISyntaxException | DeploymentException | IOException ex) {

        }
    }

}
