package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    Collection<GameData> listGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGameData(int gameID) throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    void clearGameData() throws DataAccessException;
}
