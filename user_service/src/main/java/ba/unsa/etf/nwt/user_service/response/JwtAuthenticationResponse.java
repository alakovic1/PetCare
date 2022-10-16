package ba.unsa.etf.nwt.user_service.response;

public class JwtAuthenticationResponse {
    private Long userId;
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(Long userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
