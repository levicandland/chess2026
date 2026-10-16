package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private int gameTag = 0;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        gameTag++;
        GameData game = new GameData(gameTag, null, null, gameName, new ChessGame());
        games.put(gameTag, game);
        return gameTag;
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
    }

    @Override
    public void clearGameData() throws DataAccessException {
        games.clear();
    }
}
