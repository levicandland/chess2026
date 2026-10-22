package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.UnauthorizedException;
import service.UserService;

import java.util.Map;

public class UserHandler {

    private final UserService userService;

    public UserHandler (UserService userService) {
        this.userService = userService;
    }

    public void register(Context ctx) {
        try {
            //store info given from ctx
            String ctxBody = ctx.body();
            // turn ctx info into UserData
            UserData userData = new Gson().fromJson(ctxBody, UserData.class);
            AuthData authData = userService.register(userData);
            ctx.status(200);
            ctx.result(new Gson().toJson(authData));
        }
        catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        }
        catch (AlreadyTakenException aTE) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", aTE.getMessage())));
        }
        catch (BadRequestException bRE) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", bRE.getMessage())));
        }
    }

    public void login(Context ctx) {
        try {
            //store info given from ctx
            String ctxBody = ctx.body();
            // turn ctx info into UserData
            UserData userData = new Gson().fromJson(ctxBody, UserData.class);
            AuthData authData = userService.login(userData);
            ctx.status(200);
            ctx.result(new Gson().toJson(authData));
        }
        catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        }
        catch (UnauthorizedException uE) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", uE.getMessage())));
        }
        catch (BadRequestException bRE) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", bRE.getMessage())));
        }
    }

    public void logout(Context ctx) {
        try {
            //store info given from ctx
            String ctxHeader = ctx.header("authorization");
            //log that user at(token) out
            userService.logout(ctxHeader);
            ctx.status(200);
            ctx.result("{}");
        }
        catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        }
        catch (UnauthorizedException uE) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", uE.getMessage())));
        }
    }

}
