package model;

import java.util.Random;
import java.util.Base64;

public class AuthData {
    private String username;
    private String authToken;
    private static final Random random = new Random(); // Random instance
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public AuthData(String username) {
        this.username = username;
        this.authToken = generateToken();
    }

    public static String generateToken() {
        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes); // Generate 24 random bytes
        return base64Encoder.encodeToString(randomBytes); // Convert to a base64 string
    }
}
