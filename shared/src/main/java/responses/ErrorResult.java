package responses;

public record ErrorResult(int statusCode, String message) implements ResultInterface {
}
