package handler;

import dataaccess.DataAccessException;
import responses.JoinGameResult;
import status.ReturnCases;
import com.google.gson.Gson;
import requests.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    public JoinGameHandler() throws DataAccessException {
        this.service = new GameService();
    }

    public Object handleRequest(Request req, Response res) {
        try {
            String reqData = req.body();
            String authToken = req.headers("Authorization");

            JoinGameRequest joinGameRequest = gson.fromJson(reqData, JoinGameRequest.class);
            joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());

            JoinGameResult result = service.joinGame(joinGameRequest);
            res.status(200);
            return gson.toJson(result);
        } catch (Exception e) {
            res.status(ReturnCases.getReturnCode(e.getMessage()));
            return ReturnCases.generateJsonResponse(e.getMessage());

        }
    }
}
