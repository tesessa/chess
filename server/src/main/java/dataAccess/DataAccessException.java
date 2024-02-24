package dataAccess;


/**
 * Indicates there was an error connecting to the database
 */

/*
examples of methods that DAOs will need to support
clear, createUser, getUser, createGame, getGame, listGames, updateGame, createAuth, getAuth, deleteAuth

 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }



}
