package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.GameData;
import model.UserData;
import service.*;

import java.util.Collection;
import java.util.Map;

public class GameHandler {

    private final GameService gameService;

    public GameHandler (GameService gameService) {
        this.gameService = gameService;
    }

    public void createGame(Context ctx) {
        try {
            //store info given from ctx
            String ctxHeader = ctx.header("authorization");
            String ctxBody = ctx.body();
            //turn ctxBody into GameData
            GameData gameData = new Gson().fromJson(ctxBody, GameData.class);
            //create game
            int gameID = gameService.createGame(ctxHeader, gameData.gameName());
            //return
            ctx.status(200);
            ctx.result(new Gson().toJson(Map.of("gameID", gameID)));
        }
        catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        }
        catch (BadRequestException bRE) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", bRE.getMessage())));
        }
        catch (UnauthorizedException uE) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", uE.getMessage())));
        }
    }

    public void listGames(Context ctx) {
        try {
            //store info given from ctx
            String ctxHeader = ctx.header("authorization");
            //list games
            Collection<GameData> gamesList = gameService.listGames(ctxHeader);
            //return
            ctx.status(200);
            ctx.result(new Gson().toJson(Map.of("games", gamesList)));
        }
        catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        }
        catch (BadRequestException bRE) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", bRE.getMessage())));
        }
        catch (UnauthorizedException uE) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", uE.getMessage())));
        }
    }

    public void joinGame(Context ctx) {
        try {
            //store info given from ctx
            String ctxHeader = ctx.header("authorization");
            String ctxBody = ctx.body();
            //turn ctxBody into joinGameRequest
            JoinGameRequest joinGameRequest = new Gson().fromJson(ctxBody, JoinGameRequest.class);
            //create join game
            gameService.joinGame(ctxHeader, joinGameRequest.playerColor(), joinGameRequest.gameID());
            //return
            ctx.status(200);
            ctx.result("{}");
        } catch (UnauthorizedException uE) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", uE.getMessage())));
        } catch (DataAccessException dAE) {
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + dAE.getMessage())));
        } catch (AlreadyTakenException aTE) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", aTE.getMessage())));
        } catch (BadRequestException bRE) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", bRE.getMessage())));
        }
    }
}
