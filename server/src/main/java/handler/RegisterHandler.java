package handler;

import status.ReturnCases;
import requests.*;
import responses.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

import java.util.HashMap;

public class RegisterHandler {
    private final Gson gson = new Gson();
    private final UserService service;


    public RegisterHandler() throws Exception {
        this.service = new UserService();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        RegisterRequest registerRequest = gson.fromJson(reqData, RegisterRequest.class);

        ResultInterface result = service.register(registerRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }

}
