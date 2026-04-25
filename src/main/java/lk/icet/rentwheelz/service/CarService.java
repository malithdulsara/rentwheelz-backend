package lk.icet.rentwheelz.service;

import lk.icet.rentwheelz.model.Car;

import java.math.BigDecimal;
import java.util.List;

public interface CarService {
    List<Car> getAllCars();
    String addCar(Car car);
    String updateCar(Car car);
    String deleteCar(Integer carId);
    List<Car> getCarsByBrand(String brand);
    List<Car> getCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Car> searchCars(String brand, String fuelType, String search,BigDecimal minPrice, BigDecimal maxPrice);
    List<String> getAllBrands();
    Car getCarById(Integer id);
}
