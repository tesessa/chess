package Server;

import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.BadRequestException;
import ExceptionClasses.ResponseException;
import ExceptionClasses.UnauthorizedException;
import Request.CreateGameRequest;
import Request.JoinGameRequest;
import Request.LoginRequest;
import Request.LogoutRequest;
import WebSocket.WebSocketFacade;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

import model.*;
import Result.*;
public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(UserData data) throws IOException {
        var path = "/user";
        return this.makeRequest("POST", path, data, null, RegisterResult.class);
    }

    public RegisterResult login(LoginRequest data) throws IOException {
        var path = "/session";
        return this.makeRequest("POST", path, data, null, RegisterResult.class);
    }

    public void logout(String authToken) throws IOException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, authToken, null);
    }

    public CreateGameResult createGame(CreateGameRequest data, String authToken) throws IOException {
        var path = "/game";
        return this.makeRequest("POST", path, data, authToken, CreateGameResult.class);
    }


    public void joinGame(JoinGameRequest data, String authToken) throws IOException, ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, data, authToken, null);
       // WebSocketFacade ws = new WebSocketFacade(serverUrl);
       // ws.joinPlayer(authToken, data.gameID(), data.playerColor());
    }

    public ListGameResult listGame(String authToken) throws IOException {
        var path = "/game";
        return this.makeRequest("GET", path, null, authToken, ListGameResult.class);
    }

    public void clear() throws IOException {
        var path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Object header, Class<T> resopnseClass) throws IOException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            if(header != null) {
                http.addRequestProperty("authorization", String.valueOf(header));
            }
            http.connect();
            writeBody(request, http);
            throwIfNotSuccessful(http);
            return readBody(http, resopnseClass);
        } catch (Exception ex) {
           throw new IOException(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if(request != null) {
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException, BadRequestException, UnauthorizedException, AlreadyTakenException {
        var status = http.getResponseCode();
        if(!isSuccessful(status)) {
            if(status == 400) {
                throw new BadRequestException();
            } else if (status == 401) {
                throw new UnauthorizedException();
            } else if (status == 403) {
                throw new AlreadyTakenException();
            } else {
                throw new ResponseException(status, "failure: " + status);
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream resBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(resBody);
                if(responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status/100 == 2;
    }
}



