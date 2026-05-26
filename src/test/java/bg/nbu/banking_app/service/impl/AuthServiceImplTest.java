package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Auth.RegisterRequest;
import bg.nbu.banking_app.data.dto.Auth.TokenPair;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.enums.Role;
import bg.nbu.banking_app.data.repository.PersonRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void registerCreatesPersonWithClientNumberThenCreatesUserAndReturnsTokens() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("client@test.com");
        request.setPassword("secret123");
        request.setRole("ROLE_USER");
        request.setFirstName("Ivan");
        request.setLastName("Petrov");
        request.setPin("9001011234");
        request.setPhone("0888123456");
        request.setAddress("Sofia");

        when(userRepository.existsByEmail("client@test.com")).thenReturn(false);
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            if (person.getId() == 0) {
                setField(person, "id", 42L);
            }
            return person;
        });
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-secret");
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(any(UserDetails.class))).thenReturn("refresh-token");

        AuthServiceImpl service = new AuthServiceImpl(
                userRepository,
                personRepository,
                passwordEncoder,
                jwtUtil,
                authenticationManager
        );

        TokenPair tokenPair = service.register(request);

        assertThat(tokenPair.getAccessToken()).isEqualTo("access-token");
        assertThat(tokenPair.getRefreshToken()).isEqualTo("refresh-token");
        assertThat(tokenPair.getEmail()).isEqualTo("client@test.com");
        assertThat(tokenPair.getRole()).isEqualTo("ROLE_USER");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail()).isEqualTo("client@test.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-secret");
        assertThat(savedUser.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(savedUser.getCustomer()).isInstanceOf(Person.class);
        assertThat(savedUser.getCustomer().getClientNumber()).isEqualTo("CL00000042");

        verify(personRepository, times(2)).save(any(Person.class));
    }

    @Test
    void registerThrowsWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("client@test.com");

        when(userRepository.existsByEmail("client@test.com")).thenReturn(true);

        AuthServiceImpl service = new AuthServiceImpl(
                userRepository,
                personRepository,
                passwordEncoder,
                jwtUtil,
                authenticationManager
        );

        assertThatThrownBy(() -> service.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already taken");

        verify(personRepository, never()).save(any(Person.class));
        verify(userRepository, never()).save(any(User.class));
    }

    private static void setField(Object target, String fieldName, Object value) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
        throw new IllegalArgumentException("Field not found: " + fieldName);
    }
}
