package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Auth.AuthResponse;
import bg.nbu.banking_app.data.dto.Auth.LoginRequest;
import bg.nbu.banking_app.data.dto.Auth.RegisterRequest;
import bg.nbu.banking_app.data.dto.Auth.TokenPair;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.enums.Role;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.security.JwtUtil;
import bg.nbu.banking_app.security.UserDetailsImpl;
import bg.nbu.banking_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenPair register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken: " + request.getUsername());
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();

        userRepository.save(user);

        return buildTokenPair(user);
    }

    @Override
    public TokenPair login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getUsername()));

        return buildTokenPair(user);
    }

    @Override
    public TokenPair refresh(String refreshToken) {
        final String username = jwtUtil.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserDetails userDetails = new UserDetailsImpl(user);

        if (!jwtUtil.isRefreshTokenValid(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        return buildTokenPair(user);
    }

    @Override
    public AuthResponse me(String accessToken) {
        final String username = jwtUtil.extractUsername(accessToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserDetails userDetails = new UserDetailsImpl(user);

        if (!jwtUtil.isTokenValid(accessToken, userDetails)) {
            throw new IllegalArgumentException("Invalid access token");
        }

        return new AuthResponse(user.getUsername(), user.getRole().name());
    }

    private TokenPair buildTokenPair(User user) {
        UserDetails userDetails = new UserDetailsImpl(user);
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new TokenPair(accessToken, refreshToken, user.getUsername(), user.getRole().name());
    }
}
