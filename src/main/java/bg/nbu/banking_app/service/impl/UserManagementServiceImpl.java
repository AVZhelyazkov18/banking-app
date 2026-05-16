package bg.nbu.banking_app.service.impl;

import bg.nbu.banking_app.data.dto.Auth.UserListResponse;
import bg.nbu.banking_app.data.entity.Customer;
import bg.nbu.banking_app.data.entity.Person;
import bg.nbu.banking_app.data.entity.User;
import bg.nbu.banking_app.data.enums.Role;
import bg.nbu.banking_app.data.repository.UserRepository;
import bg.nbu.banking_app.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;

    @Override
    public List<UserListResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserListResponse updateUserRole(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
        return toResponse(user);
    }

    private UserListResponse toResponse(User user) {
        String clientNumber = null;
        String fullName = null;
        Customer customer = user.getCustomer();
        if (customer instanceof Person p) {
            clientNumber = p.getClientNumber();
            fullName = p.getFirstName() + " " + p.getLastName();
        }
        return new UserListResponse(user.getId(), user.getEmail(), user.getRole().name(), clientNumber, fullName);
    }
}
