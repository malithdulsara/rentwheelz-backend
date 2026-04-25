package lk.icet.rentwheelz.repository.impl;

import lk.icet.rentwheelz.model.Booking;
import lk.icet.rentwheelz.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveBookingAndHoldCar(Booking booking) {
        String bookingSql = "INSERT INTO bookings (user_id, car_id, pickup_date, return_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(bookingSql,
                booking.getUserId(),
                booking.getCarId(),
                booking.getPickupDate(),
                booking.getReturnDate(),
                booking.getTotalAmount(),
                "PENDING"
        );

        String carUpdateSql = "UPDATE cars SET status = 'ON_HOLD' WHERE car_id = ?";

        jdbcTemplate.update(carUpdateSql, booking.getCarId());
    }

    public List<Map<String, Object>> getCustomerBookings(String email) {
        String sql = "SELECT b.booking_id, b.pickup_date, b.return_date, b.total_amount, b.status, b.created_at, " +
                "c.car_id,c.brand, c.model, c.image_url " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.car_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE u.email = ? ORDER BY b.created_at DESC";

        return jdbcTemplate.queryForList(sql, email);
    }
    @Override
    @Transactional
    public void approveBooking(Integer bookingId, Integer carId) {
        jdbcTemplate.update("UPDATE bookings SET status = 'APPROVED' WHERE booking_id = ?", bookingId);

        jdbcTemplate.update("UPDATE cars SET status = 'RENTED' WHERE car_id = ?", carId);
    }

    @Override
    @Transactional
    public void rejectBooking(Integer bookingId, Integer carId) {
        jdbcTemplate.update("UPDATE bookings SET status = 'REJECTED' WHERE booking_id = ?", bookingId);

        jdbcTemplate.update("UPDATE cars SET status = 'AVAILABLE' WHERE car_id = ?", carId);
    }

    @Override
    public List<Map<String, Object>> findPendingBookings() {
        String sql = "SELECT b.booking_id, b.pickup_date, b.return_date, b.total_amount, b.status, " +
                "c.car_id, c.brand, c.model, u.full_name, u.email " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.car_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.status = 'PENDING' ORDER BY b.created_at DESC";

        return jdbcTemplate.queryForList(sql);
    }

    @Override
    @Transactional
    public String cancelBooking(Integer bookingId, Integer carId) {
        String checkSql = "SELECT created_at, status FROM bookings WHERE booking_id = ?";
        Map<String, Object> booking = jdbcTemplate.queryForMap(checkSql, bookingId);

        Timestamp createdAt = (Timestamp) booking.get("created_at");
        String status = (String) booking.get("status");

        long diffInMinutes = (System.currentTimeMillis() - createdAt.getTime()) / (1000 * 60);

        if (diffInMinutes > 15) {
            throw new RuntimeException("Cancellation time (15 mins) has expired.");
        }
        if (!status.equals("PENDING")) {
            throw new RuntimeException("Cannot cancel booking as it is already " + status);
        }

        jdbcTemplate.update("UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?", bookingId);
        jdbcTemplate.update("UPDATE cars SET status = 'AVAILABLE' WHERE car_id = ?", carId);

        return "Booking cancelled successfully.";
    }

    @Override
    public Map<String, Object> getBookingStatusAndTiming(Integer bookingId) {
        String sql = "SELECT created_at, status FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForMap(sql, bookingId);
    }

    @Override
    @Transactional
    public void performCancellation(Integer bookingId, Integer carId) {
        jdbcTemplate.update("UPDATE bookings SET status = 'CANCELLED' WHERE booking_id = ?", bookingId);

        jdbcTemplate.update("UPDATE cars SET status = 'AVAILABLE' WHERE car_id = ?", carId);
    }

    @Override
    public List<Map<String, Object>> getCustomerBookingsPaginated(String email, String status, int offset, int size) {
        StringBuilder sql = new StringBuilder(
                "SELECT b.booking_id, b.pickup_date, b.return_date, b.total_amount, b.status, b.created_at, " +
                        "c.car_id, c.brand, c.model, c.image_url " +
                        "FROM bookings b " +
                        "JOIN cars c ON b.car_id = c.car_id " +
                        "JOIN users u ON b.user_id = u.user_id " +
                        "WHERE u.email = ? "
        );

        if (!"ALL".equalsIgnoreCase(status)) {
            sql.append("AND b.status = '").append(status).append("' ");
        }

        sql.append("ORDER BY b.created_at DESC LIMIT ? OFFSET ?");

        return jdbcTemplate.queryForList(sql.toString(), email, size, offset);
    }

    @Override
    public int getTotalCustomerBookingsCount(String email, String status) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM bookings b " +
                        "JOIN users u ON b.user_id = u.user_id " +
                        "WHERE u.email = ? "
        );

        if (!"ALL".equalsIgnoreCase(status)) {
            sql.append("AND b.status = '").append(status).append("'");
        }

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, email);
    }

    @Override
    @Transactional
    public void returnVehicleAndUpdateBooking(Integer bookingId, Integer carId, String newCarStatus) {
        jdbcTemplate.update("UPDATE bookings SET status = 'COMPLETED' WHERE booking_id = ?", bookingId);
        jdbcTemplate.update("UPDATE cars SET status = ? WHERE car_id = ?", newCarStatus, carId);
    }

    @Override
    public List<Map<String, Object>> findApprovedBookings() {
        String sql = "SELECT b.booking_id, b.pickup_date, b.return_date, b.total_amount, b.status, " +
                "c.car_id, c.brand, c.model, u.full_name, u.email " +
                "FROM bookings b " +
                "JOIN cars c ON b.car_id = c.car_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE b.status = 'APPROVED' ORDER BY b.created_at DESC";

        return jdbcTemplate.queryForList(sql);
    }
}
