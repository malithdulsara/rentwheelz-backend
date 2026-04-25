package lk.icet.rentwheelz.service.impl;

import lk.icet.rentwheelz.model.AuthResponse;
import lk.icet.rentwheelz.model.LoginRequest;
import lk.icet.rentwheelz.model.RegisterRequest;
import lk.icet.rentwheelz.model.User;
import lk.icet.rentwheelz.repository.UserRepository;
import lk.icet.rentwheelz.security.JwtUtils;
import lk.icet.rentwheelz.service.AuthService;
import lk.icet.rentwheelz.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        user.setContactNumber(request.getContactNumber());
        user.setAddress(request.getAddress());

        userRepository.save(user);
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, "User registered successfully!");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: User not found!"));
        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("Error: Your account has been suspended by the Admin. Please contact support.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Error: Invalid email or password!");
        }
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, "Login successful!");
    }
}
