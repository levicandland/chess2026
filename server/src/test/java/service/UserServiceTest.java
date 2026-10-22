package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private UserService userService;
    @BeforeEach
    public void setup() {
        UserDAO myUserDAO = new MemoryUserDAO();
        AuthDAO myAuthDAO = new MemoryAuthDAO();
        userService = new UserService(myUserDAO, myAuthDAO);
    }
    //register
    @Test
    public void registerPositive() throws BadRequestException, DataAccessException, AlreadyTakenException {
        UserData testUserData = new UserData("username", "password", "email");
        AuthData authData = userService.register(testUserData);
        assertEquals("username", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    public void registerNegative() throws BadRequestException, DataAccessException, AlreadyTakenException {
        UserData testUserData = new UserData("username", "password", "email");
        userService.register(testUserData);
        assertThrows(AlreadyTakenException.class, () -> userService.register(testUserData));
    }
    //login
    @Test
    public void loginPositive() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        UserData testUserData = new UserData("username", "password", "email");
        userService.register(testUserData);
        AuthData loginAuthData = userService.login(testUserData);
        assertEquals("username", loginAuthData.username());
        assertNotNull(loginAuthData.authToken());
    }

    @Test
    public void loginNegative() throws BadRequestException, DataAccessException, AlreadyTakenException {
        UserData testUserData = new UserData("username", "password", "email");
        userService.register(testUserData);
        UserData wrongUserData = new UserData("username", "wrongpassword", "email");
        assertThrows(UnauthorizedException.class, () -> userService.login(wrongUserData));
    }
    //logout
    @Test
    public void logoutPositive() throws BadRequestException, DataAccessException, AlreadyTakenException, UnauthorizedException {
        UserData testUserData = new UserData("username", "password", "email");
        AuthData authData = userService.register(testUserData);
        userService.logout(authData.authToken());
        assertThrows(UnauthorizedException.class, () -> userService.logout(authData.authToken()));
    }

    @Test
    public void logoutNegative() {
        assertThrows(UnauthorizedException.class, () -> userService.logout("fakeToken"));
    }
}
