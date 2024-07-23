package handler;

import status.ReturnCases;
import com.google.gson.Gson;
import requests.JoinGameRequest;
import responses.ResultInterface;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    public JoinGameHandler() {
        this.service = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        String authToken = req.headers("Authorization");

        JoinGameRequest joinGameRequest = gson.fromJson(reqData, JoinGameRequest.class);
        joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());

        ResultInterface result = service.joinGame(joinGameRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }
}
