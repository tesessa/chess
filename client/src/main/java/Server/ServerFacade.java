package Server;

import ExceptionClasses.ResponseException;
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
        return this.makeRequest("POST", path, data, RegisterResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> resopnseClass) throws IOException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            http.addRequestProperty("Content-Type", "application/json");
            http.connect();
            writeBody(request, http);
            throwIfNotSuccessful(http);
            return readBody(http, resopnseClass);
        } catch (Exception ex) {
            throw new IOException();
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if(!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
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



