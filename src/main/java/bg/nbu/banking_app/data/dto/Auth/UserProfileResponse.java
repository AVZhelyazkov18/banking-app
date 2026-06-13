package bg.nbu.banking_app.data.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponse {
    private String email;
    private String role;
    private String clientNumber;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
