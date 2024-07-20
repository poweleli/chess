package handler;

import com.google.gson.Gson;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.ErrorResult;
import responses.JoinGameResult;
import responses.ResultInterface;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class JoinGameHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
        put("Error: bad request", 400);
        put("Error: unauthorized", 401);
        put("Error: already taken", 403);
    }};

    public JoinGameHandler() {
        this.service = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        String authToken = req.headers("Authorization");

        JoinGameRequest joinGameRequest = gson.fromJson(reqData, JoinGameRequest.class);
        joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());

        ResultInterface result = service.joinGame(joinGameRequest);
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
