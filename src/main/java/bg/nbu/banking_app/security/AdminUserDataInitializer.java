package bg.nbu.banking_app.security;

import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.PersonRepository;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.data.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    public AdminUserDataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PersonRepository personRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    @Override
    public void run(String... args) {
        Person personAdmin = personRepository.findByClientNumber("ADMIN00001")
                .orElseGet(() -> {
                    Person person = new Person();
                    person.setAddress("ADMIN");
                    person.setPin("12345678");
                    person.setFirstName("ADMIN");
                    person.setLastName("ADMIN");
                    person.setClientNumber("ADMIN00001");

                    return personRepository.save(person);
                });

        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setCustomer(personAdmin);

            userRepository.save(admin);
        }
    }
}