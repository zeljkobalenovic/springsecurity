package zeljko.springsecurity.model;

/**
 * AuthenticationResponse
 */
public class AuthenticationResponse {

    private String jwtToken;

    

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }


}