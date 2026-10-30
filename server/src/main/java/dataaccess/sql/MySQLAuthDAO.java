package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO () throws DataAccessException {
        DatabaseManager.createDatabase();
        String authTableMaker = """
                CREATE TABLE IF NOT EXISTS auths (
                authToken VARCHAR(256) NOT NULL,
                username VARCHAR(256) NOT NULL,
                PRIMARY KEY (authToken)
                )""";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(authTableMaker);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to create authentication token table", ex);
        }
    }

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        String getAuthTableContent =
                "SELECT authToken, username FROM auths WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(getAuthTableContent);
            preparedStatement.setString(1, authToken);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new AuthData(resultSet.getString("authToken"),
                        resultSet.getString("username")
                );
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to get from authentication token table", ex);
        }
        return null;
    }

    @Override
    public void storeAuthToken(AuthData auth) throws DataAccessException {
        String fillAuthTable = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(fillAuthTable);
            preparedStatement.setString(1, auth.authToken());
            preparedStatement.setString(2, auth.username());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to fill authentication token table", ex);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        String deleteFromAuthTable = "DELETE FROM auths WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(deleteFromAuthTable);
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to delete from authentication token table", ex);
        }
    }

    @Override
    public void clearAuthData() throws DataAccessException {
        String clearAuthTable = "TRUNCATE TABLE auths";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(clearAuthTable);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to clear authentication token table", ex);
        }
    }
}
