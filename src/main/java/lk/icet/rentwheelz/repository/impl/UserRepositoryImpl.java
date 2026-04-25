package lk.icet.rentwheelz.repository.impl;

import lk.icet.rentwheelz.model.User;
import lk.icet.rentwheelz.model.UserUpdateRequest;
import lk.icet.rentwheelz.repository.UserRepository;
import lk.icet.rentwheelz.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private RowMapper<User> mapRowToUser() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setFullName(rs.getString("full_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            String roleStr = rs.getString("role");
            if (roleStr != null) {
                user.setRole(UserRole.valueOf(roleStr));
            }
            user.setContactNumber(rs.getString("contact_number"));
            user.setAddress(rs.getString("address"));
            user.setCreatedAt(rs.getTimestamp("created_at"));
            user.setStatus(rs.getString("status"));
            return user;
        };
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, mapRowToUser(), email);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public int save(User user) {
        String sql = "INSERT INTO users (full_name, email, password, role, contact_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole().name(),
                user.getContactNumber(),
                user.getAddress()
        );
    }

    @Override
    public List<Map<String, Object>> getAllUsersForAdmin() {
        String sql = "SELECT user_id, full_name, email, role, contact_number, address, created_at, status " +
                "FROM users WHERE role != 'ROLE_ADMIN' ORDER BY created_at DESC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public void updateUserStatus(Integer userId, String newStatus) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, newStatus, userId);
    }

    @Override
    public List<Map<String, Object>> getUsersPaginated(String role, String search, int offset, int size) {
        StringBuilder sql = new StringBuilder(
                "SELECT user_id, full_name, email, role, contact_number, address, created_at, status " +
                        "FROM users WHERE role != 'ROLE_ADMIN' "
        );
        List<Object> params = new ArrayList<>();

        if (!"ALL".equalsIgnoreCase(role)) {
            sql.append("AND role = ? ");
            params.add(role);
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (full_name LIKE ? OR email LIKE ? OR contact_number LIKE ?) ");
            String searchParam = "%" + search.trim() + "%";
            params.add(searchParam);
            params.add(searchParam);
            params.add(searchParam);
        }

        sql.append("ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    @Override
    public int getTotalUsersCount(String role, String search) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM users WHERE role != 'ROLE_ADMIN' ");
        List<Object> params = new ArrayList<>();

        if (!"ALL".equalsIgnoreCase(role)) {
            sql.append("AND role = ? ");
            params.add(role);
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append("AND (full_name LIKE ? OR email LIKE ? OR contact_number LIKE ?) ");
            String searchParam = "%" + search.trim() + "%";
            params.add(searchParam);
            params.add(searchParam);
            params.add(searchParam);
        }

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
    }

    @Override
    public void updateUser(Integer userId, UserUpdateRequest request) {
        String sql = "UPDATE users SET full_name = ?, contact_number = ?, address = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                request.getFullName(),
                request.getContactNumber(),
                request.getAddress(),
                userId);
    }
}
