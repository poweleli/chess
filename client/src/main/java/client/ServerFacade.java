package client;

import com.google.gson.Gson;
import responses.*;
import requests.*;
import exception.ResponseException;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrlString) {
        serverUrl = serverUrlString;
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, req, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, req, LoginResult.class, null);
    }

    public LogoutResult logout(LogoutRequest req) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, req, LogoutResult.class, req.authToken());
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, req, ListGamesResult.class, req.authToken());
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, req, CreateGameResult.class, req.authToken());
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, req, JoinGameResult.class, req.authToken());
    }

    public ClearResult deleteDB() throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", path, null, ClearResult.class, null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws ResponseException{
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (auth != null) {
                http.addRequestProperty("Authorization", auth);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, getErrorMessage(status));
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public static String getErrorMessage(int errorCode) {
        return switch (errorCode) {
            case 400 -> "Error: bad request";
            case 401 -> "Error: unauthorized";
            case 403 -> "Error: already taken";
            default -> "Error: unknown"; // Default message for unknown error codes
        };
    }
}

