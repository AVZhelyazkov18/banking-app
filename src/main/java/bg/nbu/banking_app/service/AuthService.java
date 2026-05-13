package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Auth.AuthResponse;
import bg.nbu.banking_app.data.dto.Auth.LoginRequest;
import bg.nbu.banking_app.data.dto.Auth.RegisterRequest;
import bg.nbu.banking_app.data.dto.Auth.TokenPair;
import bg.nbu.banking_app.data.dto.Auth.UpdateProfileRequest;
import bg.nbu.banking_app.data.dto.Auth.UserProfileResponse;

public interface AuthService {

    TokenPair register(RegisterRequest request);

    TokenPair login(LoginRequest request);

    TokenPair refresh(String refreshToken);

    AuthResponse me(String accessToken);

    UserProfileResponse getProfile(String email);

    UserProfileResponse updateProfile(String email, UpdateProfileRequest request);
}
