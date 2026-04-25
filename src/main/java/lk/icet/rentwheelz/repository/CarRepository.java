package lk.icet.rentwheelz.repository;

import lk.icet.rentwheelz.model.Car;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository {
    List<Car> findAll();
    int save(Car car);
    int update(Car car);
    int delete(Integer carId);
    List<Car> findByBrand(String brand);
    List<Car> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<Car> searchCars(String brand, String fuelType, String search, BigDecimal minPrice, BigDecimal maxPrice);
    List<String> getAllBrands();
    Car getCarById(Integer id);
}
