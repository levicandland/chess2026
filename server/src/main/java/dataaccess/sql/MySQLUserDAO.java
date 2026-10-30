package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String fillUserTable = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(fillUserTable);
            preparedStatement.setString(1, user.username());
            String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.email());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to fill user table", ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String getUserTableContent =
                "SELECT username, password, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(getUserTableContent);
            preparedStatement.setString(1, username);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserData(resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"));
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to get from user table", ex);
        }
        return null;
    }

    @Override
    public void clearUserData() throws DataAccessException {
        String clearUserTable = "TRUNCATE TABLE users";
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(clearUserTable);
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("failed to clear user table", ex);
        }
    }
}
