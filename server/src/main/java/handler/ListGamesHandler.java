package handler;

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

    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
        put("Error: unauthorized", 401);
    }};

    public ListGamesHandler() {
        this.service = GameService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String authToken = req.headers("Authorization");
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);

        ResultInterface result = service.listGame(listGamesRequest);
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
