package lk.icet.rentwheelz.repository;

import lk.icet.rentwheelz.model.User;
import lk.icet.rentwheelz.model.UserUpdateRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    int save(User user);
    List<Map<String, Object>> getAllUsersForAdmin();
    void updateUserStatus(Integer userId, String newStatus);
    List<Map<String, Object>> getUsersPaginated(String role,String search, int offset, int size);
    int getTotalUsersCount(String role,String search);
    void updateUser(Integer userId, UserUpdateRequest request);
}
