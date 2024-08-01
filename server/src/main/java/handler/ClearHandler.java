package handler;

import com.google.gson.Gson;
import responses.*;
import service.*;
import spark.*;


public class ClearHandler {
    private final Gson gson = new Gson();
    private final UserService userService;
    private final GameService gameService;

    public ClearHandler() throws Exception {
        this.userService = new UserService();
        this.gameService = new GameService();
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
