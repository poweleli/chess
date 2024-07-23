package handler;

import status.ReturnCases;
import com.google.gson.Gson;
import requests.CreateGameRequest;
import responses.ResultInterface;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    public CreateGameHandler() {
        this.service = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        String authToken = req.headers("Authorization");

        CreateGameRequest createGameRequest = gson.fromJson(reqData, CreateGameRequest.class);
        createGameRequest = new CreateGameRequest(authToken, createGameRequest.gameName());

        ResultInterface result = service.createGame(createGameRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }
}
