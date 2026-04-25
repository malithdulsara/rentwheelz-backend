package lk.icet.rentwheelz.service.impl;

import lk.icet.rentwheelz.model.Booking;
import lk.icet.rentwheelz.repository.BookingRepository;
import lk.icet.rentwheelz.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createBookingRequest(Map<String, Object> bookingData) {
        String email = (String) bookingData.get("customerEmail");

        Integer userId;
        try {
            userId = jdbcTemplate.queryForObject(
                    "SELECT user_id FROM users WHERE email = ?",
                    Integer.class,
                    email
            );
        } catch (Exception e) {
            throw new RuntimeException("User not found with email: " + email);
        }

        Booking booking = new Booking();
        booking.setUserId(userId);

        booking.setCarId(Integer.parseInt(bookingData.get("carId").toString()));

        if (bookingData.get("returnDate") == null || bookingData.get("pickupDate") == null) {
            throw new RuntimeException("Error: Dates cannot be null!");
        }
        booking.setPickupDate(Date.valueOf(bookingData.get("pickupDate").toString()));
        booking.setReturnDate(Date.valueOf(bookingData.get("returnDate").toString()));

        Object priceObj = bookingData.get("totalPrice");
        if (priceObj != null) {
            booking.setTotalAmount(Double.parseDouble(priceObj.toString()));
        }

        bookingRepository.saveBookingAndHoldCar(booking);
    }

    public List<Map<String, Object>> getMyBookings(String email) {
        return bookingRepository.getCustomerBookings(email);
    }
    @Override
    public List<Map<String, Object>> getPendingBookings() {
        return bookingRepository.findPendingBookings();
    }
    @Override
    public void approveBooking(Integer bookingId, Integer carId) {
        bookingRepository.approveBooking(bookingId, carId);
    }

    @Override
    public void rejectBooking(Integer bookingId, Integer carId) {
        bookingRepository.rejectBooking(bookingId, carId);
    }

    @Override
    @Transactional
    public String cancelBooking(Integer bookingId, Integer carId) {
        Map<String, Object> booking = bookingRepository.getBookingStatusAndTiming(bookingId);

        if (booking == null || booking.isEmpty()) {
            throw new RuntimeException("Booking not found!");
        }

        java.sql.Timestamp createdAt = (java.sql.Timestamp) booking.get("created_at");
        String status = (String) booking.get("status");

        long diffInMinutes = (System.currentTimeMillis() - createdAt.getTime()) / (1000 * 60);

        if (diffInMinutes > 15) {
            throw new RuntimeException("Cancellation time has expired (Limit: 15 mins).");
        }

        if (!"PENDING".equals(status)) {
            throw new RuntimeException("Cannot cancel booking. Status is already " + status);
        }

        bookingRepository.performCancellation(bookingId, carId);

        return "Booking cancelled successfully!";
    }

    @Override
    public Map<String, Object> getMyBookingsPaginated(String email, int page, int size, String status) {
        int offset = (page - 1) * size;
        if (offset < 0) offset = 0; // Page 0 හෝ සෘණ ආවොත් ආරක්ෂාවට
        List<Map<String, Object>> bookings = bookingRepository.getCustomerBookingsPaginated(email, status, offset, size);
        int totalItems = bookingRepository.getTotalCustomerBookingsCount(email, status);
        int totalPages = (int) Math.ceil((double) totalItems / size);

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("bookings", bookings);
        response.put("currentPage", page);
        response.put("totalItems", totalItems);
        response.put("totalPages", totalPages);

        return response;
    }

    @Override
    public String completeReturn(Integer bookingId, Integer carId, String newCarStatus) {
        if (!"AVAILABLE".equals(newCarStatus) && !"MAINTENANCE".equals(newCarStatus)) {
            throw new RuntimeException("Invalid car status provided.");
        }

        bookingRepository.returnVehicleAndUpdateBooking(bookingId, carId, newCarStatus);
        return "Vehicle returned and booking completed successfully!";
    }

    @Override
    public List<Map<String, Object>> getApprovedBookings() {
        return bookingRepository.findApprovedBookings();
    }
}