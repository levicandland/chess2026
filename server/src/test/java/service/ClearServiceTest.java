package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearServiceTest {
    private ClearService clearService;
    private UserDAO myUserDAO;
    private AuthDAO myAuthDAO;
    private GameDAO myGameDAO;

    @BeforeEach
    public void setup() {
        myUserDAO = new MemoryUserDAO();
        myAuthDAO = new MemoryAuthDAO();
        myGameDAO = new MemoryGameDAO();
        clearService = new ClearService(myUserDAO, myAuthDAO, myGameDAO);
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

}
