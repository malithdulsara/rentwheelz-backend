package lk.icet.rentwheelz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    private Integer bookingId;
    private Integer userId;
    private Integer carId;
    private Date pickupDate;
    private Date returnDate;
    private Double totalAmount;
}
