package handler;

import status.*;
import com.google.gson.Gson;
import responses.ResultInterface;
import service.*;

public class Handler {
    protected final UserService userService;
    protected final GameService gameService;
    protected final Gson gson = new Gson();

    public Handler() {
        this.userService = UserService.getInstance();
        this.gameService = GameService.getInstance();
    }

    public int getStatusCode(ResultInterface res) {
        ReturnCases rc = new ReturnCases();
        return ReturnCases.getReturnCode(res);
    }

}
