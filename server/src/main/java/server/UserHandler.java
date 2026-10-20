package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.BadRequestException;
import service.UserService;
import service.UsernameTakenException;

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
        catch (UsernameTakenException uTE) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", uTE.getMessage())));
        }
        catch (BadRequestException bRE) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", bRE.getMessage())));
        }
    }

}
