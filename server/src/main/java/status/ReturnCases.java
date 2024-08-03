package status;

import com.google.gson.JsonObject;
import responses.*;
import com.google.gson.Gson;

public class ReturnCases {
    private static final Gson gson = new Gson();

    public static int getErrorCode(String errorMessage) {
        return switch (errorMessage) {
            case "Error: bad request" -> 400;
            case "Error: unauthorized" -> 401;
            case "Error: already taken" -> 403;
            default -> 500; // Unknown error
        };
    }

    public static int getReturnCode(String error) {
        return getErrorCode(error);
    }

    public static JsonObject generateJsonResponse(String errorMessage) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", errorMessage);
        return jsonObject;
    }
}
