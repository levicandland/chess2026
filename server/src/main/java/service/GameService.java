package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService (AuthDAO authDAO, GameDAO gameDAO) {
        //make this parameter mine
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public int createGame (String authToken, String gameName) throws DataAccessException, UnauthorizedException, BadRequestException {

        AuthData authData = authDAO.getAuthToken(authToken);

        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        if ((gameName == null) || (gameName.isEmpty())){
            throw new BadRequestException("Error: bad request");
        }

        return gameDAO.createGame(gameName);
    }

    public Collection<GameData> listGames (String authToken) throws DataAccessException, UnauthorizedException, BadRequestException {

        AuthData authData = authDAO.getAuthToken(authToken);

        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return gameDAO.listGames();
    }

}
