package lk.icet.rentwheelz.model;

import lk.icet.rentwheelz.util.CarStatus;
import lk.icet.rentwheelz.util.FuelType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private Integer carId;
    private String brand;
    private String model;
    private FuelType fuelType;
    private Integer seatingCapacity;
    private BigDecimal pricePerDay;
    private CarStatus status;
    private String imageUrl;
}
