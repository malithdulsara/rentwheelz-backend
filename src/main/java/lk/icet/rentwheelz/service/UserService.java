package lk.icet.rentwheelz.service;

import lk.icet.rentwheelz.model.RegisterRequest;
import lk.icet.rentwheelz.model.UserUpdateRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Map<String, Object>> getAllUsersForAdmin();
    String addFleetManager(RegisterRequest request);
    String toggleUserStatus(Integer userId, String newStatus);
    Map<String, Object> getUsersPaginated(String role,String search, int page, int size);
    String updateUser(Integer userId, UserUpdateRequest request);
}
