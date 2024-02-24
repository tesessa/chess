package dataAccess;

import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;

public class SqlUserDataAccess {
    AuthData register(UserData user) {
        var statement = "INSERT INTO user (";
       // var json = new Gson.toJson(user);
         AuthData data = new AuthData(user.username(), "123");
         return data;
    }
}
