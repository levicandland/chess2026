package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService (UserDAO userDAO, AuthDAO authDAO) {
        //make this parameter mine
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData register (UserData user) throws DataAccessException, AlreadyTakenException, BadRequestException {

        if (((user.username() == null) || (user.password() == null) || (user.email() == null))
        || (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty())){
            throw new BadRequestException("Error: bad request");
        }

        UserData userData = userDAO.getUser(user.username());

        if (userData != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        userDAO.createUser(user);
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authDAO.storeAuthToken(auth);
        return auth;
    }

    public AuthData login (UserData user) throws DataAccessException, UnauthorizedException, BadRequestException {

        if (((user.username() == null) || (user.password() == null))
                || (user.username().isEmpty() || user.password().isEmpty())){
            throw new BadRequestException("Error: bad request");
        }

        UserData userData = userDAO.getUser(user.username());

        if ((userData == null) || !(BCrypt.checkpw(user.password(), userData.password()))) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, user.username());
        authDAO.storeAuthToken(auth);
        return auth;
    }

    public void logout (String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDAO.getAuthToken(authToken);

        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        authDAO.deleteAuthToken(authToken);
    }

}
