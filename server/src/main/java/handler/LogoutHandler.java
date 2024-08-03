package handler;

import status.ReturnCases;
import com.google.gson.Gson;
import requests.*;
import responses.*;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class LogoutHandler {
    private final Gson gson = new Gson();
    private final UserService service;

    public LogoutHandler() throws Exception {
        this.service = new UserService();
    }

    public Object handleRequest(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            LogoutRequest logoutRequest = new LogoutRequest(authToken);

            LogoutResult result = service.logout(logoutRequest);
            res.status(200);
            return gson.toJson(result);
        } catch (Exception e) {
            res.status(ReturnCases.getReturnCode(e.getMessage()));
            return ReturnCases.generateJsonResponse(e.getMessage());

        }
    }
}
