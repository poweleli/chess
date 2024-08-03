package handler;

import dataaccess.DataAccessException;
import status.ReturnCases;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import requests.LoginRequest;
import responses.*;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class LoginHandler {
    private final Gson gson = new Gson();
    private final UserService service;

    public LoginHandler() throws Exception {
        this.service = new UserService();
    }

    public Object handleRequest(Request req, Response res) {
        try {
            String reqData = req.body();
            LoginRequest loginRequest = gson.fromJson(reqData, LoginRequest.class);

            LoginResult result = service.login(loginRequest);
            res.status(200);
            return gson.toJson(result);
        } catch (Exception e) {
            res.status(ReturnCases.getReturnCode(e.getMessage()));
            return ReturnCases.generateJsonResponse(e.getMessage());
        }
    }

}
