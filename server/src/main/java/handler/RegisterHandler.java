package handler;

import ReturnCode.ReturnCases;
import requests.*;
import responses.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

import java.util.HashMap;

public class RegisterHandler {
    private final Gson gson = new Gson();
    private final UserService service;

    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
            put("Error: bad request", 400);
            put("Error: already taken", 403);
        }};

    public RegisterHandler() {
        this.service = UserService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        RegisterRequest registerRequest = gson.fromJson(reqData, RegisterRequest.class);

        ResultInterface result = service.register(registerRequest);
        res.status(ReturnCases.getReturnCode(result));
        return gson.toJson(result);
    }

}
