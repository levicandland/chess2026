package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        String listGameTableGames =
                "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games";
        Collection<GameData> gamesList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(listGameTableGames);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ChessGame game =
                        new Gson().fromJson(resultSet.getString("game"), ChessGame.class);
                gamesList.add(new GameData(resultSet.getInt("gameID"),
                        resultSet.getString("whiteUsername"),
                        resultSet.getString("blackUsername"),
                        resultSet.getString("gameName"),
                        game));
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to list games from game table", ex); }
        return gamesList;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        String fillGameTable = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(fillGameTable, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, new Gson().toJson(new ChessGame()));
            preparedStatement.executeUpdate();
            var resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to fill game table", ex);
        }
        return 0;
    }

    @Override
    public GameData getGameData(int gameID) throws DataAccessException {
        String getGameTableContent =
                "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(getGameTableContent);
            preparedStatement.setInt(1, gameID);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ChessGame game =
                        new Gson().fromJson(resultSet.getString("game"), ChessGame.class);
                return new GameData(resultSet.getInt("gameID"),
                        resultSet.getString("whiteUsername"),
                        resultSet.getString("blackUsername"),
                        resultSet.getString("gameName"),
                        game);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get from game table", ex); }
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        String updateGameTable = "UPDATE games SET whiteUsername = ?, " +
                "blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(updateGameTable);
            preparedStatement.setString(1, game.whiteUsername());
            preparedStatement.setString(2, game.blackUsername());
            preparedStatement.setString(3, game.gameName());
            preparedStatement.setString(4, new Gson().toJson(game.game()));
            preparedStatement.setInt(5, game.gameID());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to update game table", ex);
        }
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
