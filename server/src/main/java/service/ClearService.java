package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearService (UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        //make this parameter mine
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws DataAccessException {
        userDAO.clearUserData();
        authDAO.clearAuthData();
        gameDAO.clearGameData();
    }
}
