package handler;

import model.*;
import requests.*;
import responses.*;
import service.*;
import spark.*;
import com.google.gson.Gson;

import java.util.HashMap;

public class RegisterHandler {
    private final Gson gson = new Gson();
    private final HashMap<String, Integer> statusCodeMap = new HashMap<String, Integer>() {{
            put("Error: bad request", 400);
            put("Error: already taken", 403);
        }};

    public Object handleRequest(Request req, Response res) {
        String reqData = req.body();
        RegisterRequest registerRequest = gson.fromJson(reqData, RegisterRequest.class);

        UserService service = new UserService();
        ResultInterface result = service.register(registerRequest);


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
