package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private GameService gameService;
    @BeforeEach
    public void setup() throws DataAccessException {
        AuthDAO myAuthDAO = new MemoryAuthDAO();
        GameDAO myGameDAO = new MemoryGameDAO();
        gameService = new GameService(myAuthDAO, myGameDAO);
        AuthData authData = new AuthData("testToken", "testUsername");
        myAuthDAO.storeAuthToken(authData);
    }
    //createGame
    @Test
    public void createGamePositive() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        int gameID = gameService.createGame("testToken", "testGameName");
        assertEquals(1, gameID);
    }

    @Test
    public void createGameNegative() throws BadRequestException, DataAccessException, AlreadyTakenException {
        assertThrows(BadRequestException.class, () -> gameService.createGame("testToken", ""));
    }
}
