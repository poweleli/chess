package handler;

import ReturnCode.ReturnCases;
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
