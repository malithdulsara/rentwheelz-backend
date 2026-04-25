package lk.icet.rentwheelz.controller;

import lk.icet.rentwheelz.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public String createBooking(@RequestBody Map<String, Object> bookingData) {
        try {
            bookingService.createBookingRequest(bookingData);
            return "Booking request sent and car is now on hold.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/my-bookings/{email}")
    public ResponseEntity<Map<String, Object>> getMyBookings(
            @PathVariable String email,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ALL") String status) {

        try {
            Map<String, Object> response = bookingService.getMyBookingsPaginated(email, page, size, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pending")
    public List<Map<String, Object>> getPendingBookings() {
        return bookingService.getPendingBookings();
    }

    @PutMapping("/approve/{bookingId}/{carId}")
    public ResponseEntity<String> approveBooking(@PathVariable Integer bookingId, @PathVariable Integer carId) {
        bookingService.approveBooking(bookingId, carId);
        return ResponseEntity.ok("Booking approved and car rented successfully!");
    }

    @PutMapping("/reject/{bookingId}/{carId}")
    public ResponseEntity<String> rejectBooking(@PathVariable Integer bookingId, @PathVariable Integer carId) {
        bookingService.rejectBooking(bookingId, carId);
        return ResponseEntity.ok("Booking rejected and car released back to catalog.");
    }

    @PutMapping("/cancel/{bookingId}/{carId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Integer bookingId, @PathVariable Integer carId) {
        try {
            String result = bookingService.cancelBooking(bookingId, carId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred while cancelling.");
        }
    }

    @PutMapping("/return/{bookingId}/{carId}")
    public ResponseEntity<String> returnVehicle(
            @PathVariable Integer bookingId,
            @PathVariable Integer carId,
            @RequestParam String carStatus) {
        try {
            String result = bookingService.completeReturn(bookingId, carId, carStatus);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process return: " + e.getMessage());
        }
    }

    @GetMapping("/approved")
    public List<Map<String, Object>> getApprovedBookings() {
        return bookingService.getApprovedBookings();
    }
}