package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserServiceTest {
    private UserService userService;
    @BeforeEach
    public void setup() {
        UserDAO myUserDAO = new MemoryUserDAO();
        AuthDAO myAuthDAO = new MemoryAuthDAO();
        userService = new UserService(myUserDAO, myAuthDAO);
    }

    @Test
    public void registerPositive() throws BadRequestException, DataAccessException, AlreadyTakenException {
        UserData testUserData = new UserData("username", "password", "email");
        AuthData authData = userService.register(testUserData);
        assertEquals("username", authData.username());
        assertNotNull(authData.authToken());
    }

}
