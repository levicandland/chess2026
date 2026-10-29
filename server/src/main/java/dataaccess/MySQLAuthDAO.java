package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO{

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
        return null;
    }

    @Override
    public void storeAuthToken(AuthData auth) throws DataAccessException {

    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAuthData() throws DataAccessException {

    }
}
