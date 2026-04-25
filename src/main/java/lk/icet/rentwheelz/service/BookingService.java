package lk.icet.rentwheelz.service;

import java.util.List;
import java.util.Map;

public interface BookingService {
    void createBookingRequest(Map<String, Object> bookingData);
    List<Map<String, Object>> getMyBookings(String email);
    List<Map<String, Object>> getPendingBookings();
    void approveBooking(Integer bookingId, Integer carId);
    void rejectBooking(Integer bookingId, Integer carId);
    String cancelBooking(Integer bookingId, Integer carId);
    Map<String, Object> getMyBookingsPaginated(String email, int page, int size, String status);
    String completeReturn(Integer bookingId, Integer carId, String newCarStatus);
    List<Map<String, Object>> getApprovedBookings();
}
