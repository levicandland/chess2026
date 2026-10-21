package server;

import dataaccess.*;
import io.javalin.*;
import service.ClearService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // create "kitchens"
        UserDAO myUserDAO = new MemoryUserDAO();
        AuthDAO myAuthDAO = new MemoryAuthDAO();
        GameDAO myGameDAO = new MemoryGameDAO();

        //create customer and then waiter respectively for clear
        ClearService myClearService = new ClearService(myUserDAO, myAuthDAO, myGameDAO);
        ClearHandler myClearHandler = new ClearHandler(myClearService);

        //create customer and waiter for user requests
        UserService myUserService = new UserService(myUserDAO, myAuthDAO);
        UserHandler myUserHandler = new UserHandler(myUserService);

        //instruct restaurant host to send respective requests to right waiter
        javalin.delete("/db", myClearHandler :: clear);
        javalin.post("/user", myUserHandler :: register);
        javalin.post("/session", myUserHandler :: login);
        javalin.delete("/session", myUserHandler :: logout);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
