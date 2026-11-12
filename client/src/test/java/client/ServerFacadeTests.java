package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.clear();
    }

    @Test
    public void registerPositive() throws ResponseException {
        UserData user = new UserData("username", "password", "email");
        AuthData auth = facade.register(user);
        assertNotNull(auth.authToken());
    }

    @Test
    public void registerNegative() throws ResponseException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        assertThrows(ResponseException.class, () -> facade.register(user));
    }

    @Test
    public void loginPositive() throws ResponseException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        AuthData auth = facade.login("username","password");
        assertNotNull(auth.authToken());
    }

    @Test
    public void loginNegative() throws ResponseException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        assertThrows(ResponseException.class, () ->
                facade.login("username", "wrongPassword"));
    }

}
