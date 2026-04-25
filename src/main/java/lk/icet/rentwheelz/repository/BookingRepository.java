package lk.icet.rentwheelz.repository;

import lk.icet.rentwheelz.model.Booking;

import java.util.List;
import java.util.Map;

public interface BookingRepository {
    void saveBookingAndHoldCar(Booking booking);
    List<Map<String, Object>> getCustomerBookings(String email);
    void approveBooking(Integer bookingId, Integer carId);
    void rejectBooking(Integer bookingId, Integer carId);
    List<Map<String, Object>> findPendingBookings();
    String cancelBooking(Integer bookingId, Integer carId);
    Map<String, Object> getBookingStatusAndTiming(Integer bookingId);
    void performCancellation(Integer bookingId, Integer carId);
    List<Map<String, Object>> getCustomerBookingsPaginated(String email, String status, int offset, int size);
    int getTotalCustomerBookingsCount(String email, String status);
    void returnVehicleAndUpdateBooking(Integer bookingId, Integer carId, String newCarStatus);
    List<Map<String, Object>> findApprovedBookings();
}
