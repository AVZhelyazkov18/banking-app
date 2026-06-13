package bg.nbu.banking_app.data.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserListResponse {
    private Long id;
    private String email;
    private String role;
    private String clientNumber;
    private String fullName;
}
