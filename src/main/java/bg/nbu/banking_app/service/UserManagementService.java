package bg.nbu.banking_app.service;

import bg.nbu.banking_app.data.dto.Auth.UserListResponse;

import java.util.List;

public interface UserManagementService {
    List<UserListResponse> getAllUsers();
    UserListResponse updateUserRole(Long userId, String role);
}
