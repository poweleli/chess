package handler;

import dataaccess.DataAccessException;
import responses.CreateGameResult;
import status.ReturnCases;
import com.google.gson.Gson;
import requests.CreateGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    public CreateGameHandler() throws DataAccessException {
        this.service = new GameService();
    }

    public Object handleRequest(Request req, Response res) {
        try {
            String reqData = req.body();
            String authToken = req.headers("Authorization");

            CreateGameRequest createGameRequest = gson.fromJson(reqData, CreateGameRequest.class);
            createGameRequest = new CreateGameRequest(authToken, createGameRequest.gameName());

            CreateGameResult result = service.createGame(createGameRequest);
            res.status(200);
            return gson.toJson(result);
        } catch (Exception e) {
            res.status(ReturnCases.getReturnCode(e.getMessage()));
            return ReturnCases.generateJsonResponse(e.getMessage());
        }
    }
}
