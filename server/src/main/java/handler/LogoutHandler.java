package handler;

import status.ReturnCases;
import com.google.gson.Gson;
import requests.*;
import responses.ResultInterface;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class LogoutHandler {
    private final Gson gson = new Gson();
    private final UserService service;

    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
        put("Error: unauthorized", 401);
    }};

    public LogoutHandler() {
        this.service = UserService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String authToken = req.headers("Authorization");
        LogoutRequest logoutRequest = new LogoutRequest(authToken);

        ResultInterface result = service.logout(logoutRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }
}
