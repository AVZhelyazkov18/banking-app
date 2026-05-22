package bg.nbu.banking_app.security;

import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.data.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserDataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@test.com")) {
            User admin = new User();
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);

            userRepository.save(admin);
        }
    }
}