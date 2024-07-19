package handler;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.ErrorResult;
import responses.ResultInterface;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class LoginHandler {
    private final Gson gson = new Gson();
    private final UserService service;

    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
        put("Error: unauthorized", 401);
    }};

    public LoginHandler() {
        this.service = UserService.getInstance();
    }

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        LoginRequest loginRequest = gson.fromJson(reqData, LoginRequest.class);

        ResultInterface result = service.login(loginRequest);
        res.status(getStatusCode(result));
        return gson.toJson(result);
    }

    public int getStatusCode(ResultInterface result) {
        if (result instanceof ErrorResult) {
            System.out.println("Status error");
            if (statusCodeMap.get(((ErrorResult) result).message()) == null) {
                return 500;
            } else {
                return statusCodeMap.get(((ErrorResult) result).message());
            }
        }
        return 200;
    }

}
