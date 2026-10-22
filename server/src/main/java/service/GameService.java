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

    public void joinGame (String authToken, String playerColor, int gameID) throws DataAccessException,
            UnauthorizedException, BadRequestException, AlreadyTakenException {
        //ticket check
        AuthData authData = authDAO.getAuthToken(authToken);

        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        //playerColor check
        if ((playerColor == null) || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {
            throw new BadRequestException("Error: bad request");
        }
        //valid game check
        GameData game = gameDAO.getGameData(gameID);

        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        //seat check
        if ((playerColor.equals("WHITE")) && (game.whiteUsername() != null)) {
            throw new AlreadyTakenException("Error: already taken");
        }

        if ((playerColor.equals("BLACK")) && (game.blackUsername() != null)) {
            throw new AlreadyTakenException("Error: already taken");
        }
        //set seats
        if (playerColor.equals("WHITE")) {
            GameData updatedGame = new GameData(gameID, authData.username(),
                    game.blackUsername(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        }

        if (playerColor.equals("BLACK")) {
            GameData updatedGame = new GameData(gameID, game.whiteUsername(),
                    authData.username(), game.gameName(), game.game());
            gameDAO.updateGame(updatedGame);
        }
    }


    }
