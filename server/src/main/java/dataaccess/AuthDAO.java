package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData getAuthToken(String authToken) throws DataAccessException;
    void storeAuthToken(AuthData auth) throws DataAccessException;
    void deleteAuthToken(String authToken) throws DataAccessException;
    void clearAuthData() throws DataAccessException;
}
