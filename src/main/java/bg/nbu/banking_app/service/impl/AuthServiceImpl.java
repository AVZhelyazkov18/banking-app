package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Auth.AuthResponse;
import bg.nbu.banking_app.data.dto.Auth.LoginRequest;
import bg.nbu.banking_app.data.dto.Auth.RegisterRequest;
import bg.nbu.banking_app.data.dto.Auth.TokenPair;
import bg.nbu.banking_app.data.dto.Auth.UpdateProfileRequest;
import bg.nbu.banking_app.data.dto.Auth.UserProfileResponse;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.enums.Role;
import bg.nbu.banking_app.data.repository.PersonRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public TokenPair register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already taken: " + request.getEmail());
        }

        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setPin(request.getPin());
        person.setPhone(request.getPhone());
        person.setAddress(request.getAddress());
        Person savedPerson = personRepository.save(person);

        savedPerson.setClientNumber(String.format("CL%08d", savedPerson.getId()));
        personRepository.save(savedPerson);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .customer(savedPerson)
                .build();

        userRepository.save(user);

        return buildTokenPair(user);
    }

    @Override
    public TokenPair login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getEmail()));

        return buildTokenPair(user);
    }

    @Override
    public TokenPair refresh(String refreshToken) {
        final String email = jwtUtil.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        UserDetails userDetails = new UserDetailsImpl(user);

        if (!jwtUtil.isRefreshTokenValid(refreshToken, userDetails)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        return buildTokenPair(user);
    }

    @Override
    public AuthResponse me(String accessToken) {
        final String email = jwtUtil.extractUsername(accessToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        UserDetails userDetails = new UserDetailsImpl(user);

        if (!jwtUtil.isTokenValid(accessToken, userDetails)) {
            throw new IllegalArgumentException("Invalid access token");
        }

        return new AuthResponse(user.getEmail(), user.getRole().name());
    }

    @Override
    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Person person = (Person) user.getCustomer();
        return new UserProfileResponse(
                user.getEmail(),
                user.getRole().name(),
                person.getClientNumber(),
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                person.getAddress()
        );
    }

    @Override
    @Transactional
    public UserProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Person person = (Person) user.getCustomer();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setPhone(request.getPhone());
        person.setAddress(request.getAddress());
        personRepository.save(person);

        return new UserProfileResponse(
                user.getEmail(),
                user.getRole().name(),
                person.getClientNumber(),
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                person.getAddress()
        );
    }

    private TokenPair buildTokenPair(User user) {
        UserDetails userDetails = new UserDetailsImpl(user);
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new TokenPair(accessToken, refreshToken, user.getEmail(), user.getRole().name());
    }
}
