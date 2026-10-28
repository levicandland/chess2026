package dataaccess;

import model.UserData;

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
