package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO () throws DataAccessException {
        DatabaseManager.createDatabase();
        String gameTableMaker = """
            CREATE TABLE IF NOT EXISTS games (
	        gameID INT NOT NULL AUTO_INCREMENT,
            whiteUsername VARCHAR(256),
	        blackUsername VARCHAR(256),
	        gameName VARCHAR(256) NOT NULL,
            game TEXT NOT NULL,
            PRIMARY KEY (gameID)
            )""";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(gameTableMaker);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to create game table", ex);
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void clearGameData() throws DataAccessException {
        String clearGameTable = "TRUNCATE TABLE games";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(clearGameTable);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to clear game table", ex);
        }
    }
}
