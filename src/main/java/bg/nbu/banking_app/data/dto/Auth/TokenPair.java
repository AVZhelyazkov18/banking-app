package bg.nbu.banking_app.data.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenPair {

    private String accessToken;
    private String refreshToken;
    private String email;
    private String role;
}
