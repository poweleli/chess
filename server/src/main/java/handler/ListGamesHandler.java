package handler;

import dataaccess.DataAccessException;
import status.ReturnCases;
import com.google.gson.Gson;
import requests.*;
import responses.*;
import service.*;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    public ListGamesHandler() throws DataAccessException {
        this.service = new GameService();
    }

    public Object handleRequest(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

            ListGamesResult result = service.listGame(listGamesRequest);
            res.status(200);
            return gson.toJson(result);
        } catch (Exception e) {
            res.status(ReturnCases.getReturnCode(e.getMessage()));
            return ReturnCases.generateJsonResponse(e.getMessage());

        }
    }

}
