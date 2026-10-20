package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService (UserDAO userDAO, AuthDAO authDAO) {
        //make this parameter mine
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register (UserData user) throws DataAccessException, UsernameTakenException {
        UserData userData = userDAO.getUser(user.username());

        if (userData != null) {
            throw new UsernameTakenException("Error: already taken");
        }

        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authDAO.storeAuthToken(auth);
        return auth;
    }
}
