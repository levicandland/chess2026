package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO () throws DataAccessException {
        DatabaseManager.createDatabase();
        String userTableMaker = """
                CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(256) NOT NULL,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL,
                PRIMARY KEY (username)
                )""";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(userTableMaker);
                preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to create user table", ex);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUserData() throws DataAccessException {

    }
}
