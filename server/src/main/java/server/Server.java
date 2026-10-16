package server;

import dataaccess.*;
import io.javalin.*;
import service.ClearService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        UserDAO myUserDAO = new MemoryUserDAO();
        AuthDAO myAuthDAO = new MemoryAuthDAO();
        GameDAO myGameDAO = new MemoryGameDAO();

        ClearService myClearService = new ClearService(myUserDAO, myAuthDAO, myGameDAO);
        ClearHandler myClearHandler = new ClearHandler(myClearService);
        //wire up
        javalin.delete("/db", myClearHandler :: clear);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
