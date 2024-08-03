package handler;

import com.google.gson.Gson;
import responses.*;
import service.*;
import spark.*;
import status.ReturnCases;


public class ClearHandler {
    private final Gson gson = new Gson();
    private final UserService userService;
    private final GameService gameService;

    public ClearHandler() throws Exception {
        this.userService = new UserService();
        this.gameService = new GameService();
    }

    public Object handleRequest(Request req, Response res) {
        try {
            ClearResult resultUser = userService.clear();
            ClearResult resultGame = gameService.clear();
            res.status(200);
            return gson.toJson(resultGame);
        } catch (Exception e) {
            res.status(ReturnCases.getReturnCode(e.getMessage()));
            return ReturnCases.generateJsonResponse(e.getMessage());
        }

    }
}
