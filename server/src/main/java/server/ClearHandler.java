package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.ClearService;

import java.util.Map;

public class ClearHandler {

    private final ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }

    public void clear(Context ctx) {
        try {
            clearService.clear();
            ctx.status(200);
            ctx.result("{}");
        }
        catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        }
    }

}
