package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    private final HashMap<String, AuthData> tokens = new HashMap<>();

    @Override
    public AuthData getAuthToken(String authToken) throws DataAccessException {
        return tokens.get(authToken);
    }

    @Override
    public void storeAuthToken(AuthData auth) throws DataAccessException {
        tokens.put(auth.authToken(), auth);
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        tokens.remove(authToken);
    }

    @Override
    public void clearAuthData() throws DataAccessException {
        tokens.clear();
    }
}
