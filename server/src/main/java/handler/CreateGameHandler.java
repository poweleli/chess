package handler;

import com.google.gson.Gson;
import requests.CreateGameRequest;
import requests.LoginRequest;
import responses.CreateGameResult;
import responses.ErrorResult;
import responses.ResultInterface;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class CreateGameHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
        put("Error: bad request", 400);
        put("Error: unauthorized", 401);
    }};

    public CreateGameHandler() {
        this.service = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        String authToken = req.headers("Authorization");

        CreateGameRequest createGameRequest = gson.fromJson(reqData, CreateGameRequest.class);
        createGameRequest = new CreateGameRequest(authToken, createGameRequest.gameName());

        ResultInterface result = service.createGame(createGameRequest);
        res.status(getStatusCode(result));
        return gson.toJson(result);
    }

    public int getStatusCode(ResultInterface result) {
        if (result instanceof ErrorResult) {
            if (statusCodeMap.get(((ErrorResult) result).message()) == null) {
                return 500;
            } else {
                return statusCodeMap.get(((ErrorResult) result).message());
            }
        }
        return 200;
    }
}
