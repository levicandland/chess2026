package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

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
    //listGames
    @Test
    public void listGamesPositive() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        gameService.createGame("testToken", "testGameOne");
        gameService.createGame("testToken", "testGameTwo");
        Collection<GameData> listTestGames = gameService.listGames("testToken");
        assertEquals(2, listTestGames.size());
    }

    @Test
    public void listGamesNegative() throws BadRequestException, DataAccessException, AlreadyTakenException {
        assertThrows(UnauthorizedException.class, () -> gameService.listGames("fakeToken"));
    }
}
