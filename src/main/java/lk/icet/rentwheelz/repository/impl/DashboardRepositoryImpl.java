package lk.icet.rentwheelz.repository.impl;

import lk.icet.rentwheelz.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int getTotalCustomers() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE role = 'ROLE_CUSTOMER'", Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public int getTotalManagers() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE role = 'ROLE_FLEET_MANAGER'", Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public int getTotalCars() {

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cars", Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public int getActiveBookings() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bookings WHERE status IN ('PENDING', 'APPROVED')", Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public double getTotalRevenue() {
        try {
            Double total = jdbcTemplate.queryForObject("SELECT SUM(total_amount) FROM bookings WHERE status = 'COMPLETED'", Double.class);
            return total != null ? total : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    @Override
    public List<Double> getMonthlyRevenue() {
        List<Double> revenueList = new ArrayList<>(Collections.nCopies(12, 0.0));

        try {
            String sql = "SELECT MONTH(created_at) as month, SUM(total_amount) as total " +
                    "FROM bookings " +
                    "WHERE status = 'COMPLETED' " +
                    "GROUP BY MONTH(created_at)";

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map<String, Object> row : rows) {
                int month = ((Number) row.get("month")).intValue();
                double total = ((Number) row.get("total")).doubleValue();

                if (month >= 1 && month <= 12) {
                    revenueList.set(month - 1, total);
                }
            }

            System.out.println("Monthly Revenue Array: " + revenueList);

        } catch (Exception e) {
            System.out.println("Error fetching monthly revenue: " + e.getMessage());
        }

        return revenueList;
    }

    @Override
    public List<Map<String, Object>> getRecentBookings() {
        String sql = "SELECT b.booking_id,b.user_id, u.full_name as user_name, b.pickup_date, b.return_date, b.total_amount, b.status " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.user_id " +
                "ORDER BY b.created_at DESC LIMIT 5";
        try {
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
