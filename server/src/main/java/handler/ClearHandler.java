package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import responses.*;
import service.*;
import spark.*;


import java.util.HashMap;

public class ClearHandler {
    private final Gson gson = new Gson();
    private final UserService userService;
    private final GameService gameService;

    public ClearHandler() {
        this.userService = UserService.getInstance();
        this.gameService = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        ResultInterface resultUser = userService.clear();
        ResultInterface resultGame = gameService.clear();

        if (resultUser instanceof ErrorResult) {
            res.status(500);
            return gson.toJson(resultUser);
        } else if (resultGame instanceof ErrorResult) {
            res.status(500);
            return gson.toJson(resultGame);
        } else {
            res.status(200);
            return gson.toJson(resultGame);
        }
    }
}
