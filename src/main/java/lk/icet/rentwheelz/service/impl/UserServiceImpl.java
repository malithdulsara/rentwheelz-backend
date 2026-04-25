package lk.icet.rentwheelz.service.impl;

import lk.icet.rentwheelz.model.RegisterRequest;
import lk.icet.rentwheelz.model.User;
import lk.icet.rentwheelz.model.UserUpdateRequest;
import lk.icet.rentwheelz.repository.UserRepository;
import lk.icet.rentwheelz.service.UserService;
import lk.icet.rentwheelz.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public List<Map<String, Object>> getAllUsersForAdmin() {
       return userRepository.getAllUsersForAdmin();
    }

    @Override
    public String addFleetManager(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_FLEET_MANAGER);
        user.setContactNumber(request.getContactNumber());
        user.setAddress(request.getAddress());
        user.setStatus("ACTIVE");

        userRepository.save(user);
        return "Fleet Manager added successfully!";
    }

    @Override
    public String toggleUserStatus(Integer userId, String newStatus) {
        if (!"ACTIVE".equalsIgnoreCase(newStatus) && !"BLOCKED".equalsIgnoreCase(newStatus)) {
            throw new RuntimeException("Invalid status provided. Only ACTIVE or BLOCKED are allowed.");
        }

        userRepository.updateUserStatus(userId, newStatus.toUpperCase());
        return "User has been successfully " + newStatus.toUpperCase() + "!";
    }

    @Override
    public Map<String, Object> getUsersPaginated(String role, String search, int page, int size) {
        int offset = page * size;

        List<Map<String, Object>> users = userRepository.getUsersPaginated(role, search, offset, size);
        int totalItems = userRepository.getTotalUsersCount(role, search);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new HashMap<>();
        response.put("users", users);
        response.put("currentPage", page + 1);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }

    @Override
    public String updateUser(Integer userId, UserUpdateRequest request) {
        userRepository.updateUser(userId, request);
        return "User details updated successfully!";
    }
}
