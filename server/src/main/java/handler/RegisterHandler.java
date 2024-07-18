package handler;

import model.*;
import requests.*;
import responses.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

public class RegisterHandler {
    private final Gson gson = new Gson();

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        RegisterRequest registerRequest = gson.fromJson(reqData, RegisterRequest.class);

        UserService service = new UserService();
        RegisterResult result = service.register(registerRequest);

        return gson.toJson(result);
    }


}
