package handler;

import ReturnCode.ReturnCases;
import com.google.gson.Gson;
import requests.*;
import responses.*;
import service.*;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class ListGamesHandler {
    private final Gson gson = new Gson();
    private final GameService service;

    public ListGamesHandler() {
        this.service = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String authToken = req.headers("Authorization");
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        ResultInterface result = service.listGame(listGamesRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }

}
