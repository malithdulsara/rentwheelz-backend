package lk.icet.rentwheelz.controller;

import lk.icet.rentwheelz.model.RegisterRequest;
import lk.icet.rentwheelz.model.UserUpdateRequest;
import lk.icet.rentwheelz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/admin/all")
    public List<Map<String, Object>> getAllUsers() {
        return userService.getAllUsersForAdmin();
    }

    @PostMapping("/admin/add-manager")
    public ResponseEntity<String> addFleetManager(@RequestBody RegisterRequest request) {
        try {
            String response = userService.addFleetManager(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/admin/status/{userId}")
    public ResponseEntity<String> toggleUserStatus(
            @PathVariable Integer userId,
            @RequestParam String status) {
        try {
            String response = userService.toggleUserStatus(userId, status);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/admin/paginated")
    public ResponseEntity<Map<String, Object>> getUsersPaginated(
            @RequestParam(defaultValue = "ALL") String role,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(userService.getUsersPaginated(role, search, page, size));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable Integer userId,
            @RequestBody UserUpdateRequest request) {
        try {
            String response = userService.updateUser(userId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update user: " + e.getMessage());
        }
    }
}
