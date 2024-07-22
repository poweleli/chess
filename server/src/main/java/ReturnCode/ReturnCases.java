package ReturnCode;

import responses.*;

public class ReturnCases {
    public static int getErrorCode(String errorMessage) {
        return switch (errorMessage) {
            case "Error: bad request" -> 400;
            case "Error: unauthorized" -> 401;
            case "Error: already taken" -> 403;
            default -> 500; // Unknown error
        };
    }

    public static int getReturnCode(ResultInterface res) {
        if (res instanceof ErrorResult) {
            return getErrorCode(((ErrorResult) res).message());
        } else {
            return 200;
        }
    }
}
