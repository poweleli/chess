package responses;

public record RegisterResult(int statusCode, String username, String authToken) implements ResultInterface{
}
