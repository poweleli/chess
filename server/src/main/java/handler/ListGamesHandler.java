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
        String authToken = req.headers("Authorization");
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        ResultInterface result = service.listGame(listGamesRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }

}
