package dataaccess.sql;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLDAOTest {
    private ClearService clearService;
    private UserDAO myUserDAO;
    private AuthDAO myAuthDAO;
    private GameDAO myGameDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        myUserDAO = new MySQLUserDAO();
        myAuthDAO = new MySQLAuthDAO();
        myGameDAO = new MySQLGameDAO();
        clearService = new ClearService(myUserDAO, myAuthDAO, myGameDAO);
        clearService.clear();
    }

    @Test
    public void clearPositive() throws DataAccessException {
        myUserDAO.createUser(new UserData("username", "password", "email"));
        myAuthDAO.storeAuthToken(new AuthData("testToken", "username"));
        myGameDAO.createGame("gameName");
        clearService.clear();
        assertNull(myUserDAO.getUser("username"));
        assertNull(myAuthDAO.getAuthToken("testToken"));
        assertEquals(0, myGameDAO.listGames().size());
    }

    @Test
    public void createUserPositive() throws DataAccessException {
        myUserDAO.createUser(new UserData("username", "password", "email"));
        assertNotNull(myUserDAO.getUser("username"));
    }

    @Test
    public void createUserNegative() throws DataAccessException {
        UserData testUserOne = new UserData("username", "password", "email");
        myUserDAO.createUser(testUserOne);
        assertThrows(DataAccessException.class, () -> myUserDAO.createUser(testUserOne));
    }

    @Test
    public void getUserPositive() throws DataAccessException {
        myUserDAO.createUser(new UserData("username", "password", "email"));
        UserData gotUser = myUserDAO.getUser("username");
        assertEquals("email", gotUser.email());
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        assertNull(myUserDAO.getUser("missingUsername"));
    }

    @Test
    public void storeAuthPositive() throws DataAccessException {
        AuthData testAuth = new AuthData("authToken", "username");
        myAuthDAO.storeAuthToken(testAuth);
        assertNotNull(myAuthDAO.getAuthToken("authToken"));
    }

    @Test
    public void storeAuthNegative() throws DataAccessException {
        AuthData testAuth = new AuthData("authToken", "username");
        myAuthDAO.storeAuthToken(testAuth);
        assertNotNull(myAuthDAO.getAuthToken("authToken"));
        assertThrows(DataAccessException.class, () -> myAuthDAO.storeAuthToken(testAuth));
    }

    @Test
    public void getAuthPositive() throws DataAccessException {
        myAuthDAO.storeAuthToken(new AuthData("authToken", "username"));
        AuthData gotAuth = myAuthDAO.getAuthToken("authToken");
        assertEquals("username", gotAuth.username());
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        assertNull(myAuthDAO.getAuthToken("missingAuth"));
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException {

    }

    @Test
    public void deleteAuthNegative() throws DataAccessException {

    }

}
