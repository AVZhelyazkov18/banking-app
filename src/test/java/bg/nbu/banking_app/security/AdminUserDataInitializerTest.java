package bg.nbu.banking_app.security;

import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.enums.Role;
import bg.nbu.banking_app.data.repository.PersonRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserDataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void runCreatesAdminPersonBeforeCreatingAdminUser() {
        when(personRepository.findByClientNumber("ADMIN00001")).thenReturn(Optional.empty());
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(passwordEncoder.encode("admin123")).thenReturn("encoded-admin-password");

        AdminUserDataInitializer initializer = new AdminUserDataInitializer(
                userRepository,
                passwordEncoder,
                personRepository
        );

        initializer.run();

        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(personRepository).save(personCaptor.capture());
        verify(userRepository).save(userCaptor.capture());

        Person savedPerson = personCaptor.getValue();
        assertThat(savedPerson.getClientNumber()).isEqualTo("ADMIN00001");
        assertThat(savedPerson.getFirstName()).isEqualTo("ADMIN");
        assertThat(savedPerson.getLastName()).isEqualTo("ADMIN");

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getEmail()).isEqualTo("admin@test.com");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-admin-password");
        assertThat(savedUser.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(savedUser.getCustomer()).isSameAs(savedPerson);

        InOrder inOrder = inOrder(personRepository, userRepository);
        inOrder.verify(personRepository).findByClientNumber("ADMIN00001");
        inOrder.verify(personRepository).save(any(Person.class));
        inOrder.verify(userRepository).existsByEmail("admin@test.com");
        inOrder.verify(userRepository).save(any(User.class));
    }

    @Test
    void runDoesNotCreateDuplicateAdminUserWhenEmailAlreadyExists() {
        Person existingPerson = new Person();
        existingPerson.setClientNumber("ADMIN00001");

        when(personRepository.findByClientNumber("ADMIN00001")).thenReturn(Optional.of(existingPerson));
        when(userRepository.existsByEmail("admin@test.com")).thenReturn(true);

        AdminUserDataInitializer initializer = new AdminUserDataInitializer(
                userRepository,
                passwordEncoder,
                personRepository
        );

        initializer.run();

        verify(personRepository, never()).save(any(Person.class));
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
