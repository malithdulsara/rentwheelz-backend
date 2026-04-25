package lk.icet.rentwheelz.service.impl;

import lk.icet.rentwheelz.model.Car;
import lk.icet.rentwheelz.repository.CarRepository;
import lk.icet.rentwheelz.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
   private final CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public String addCar(Car car) {
        int result = carRepository.save(car);
        if (result > 0) {
            return "Car saved successfully!";
        } else {
            return "Failed to save car.";
        }
    }

    @Override
    public String updateCar(Car car) {
        Car existingCar = carRepository.getCarById(car.getCarId());
        if (existingCar == null) {
            throw new RuntimeException("Car not found!");
        }
        String status = existingCar.getStatus().toString();
        if ("RENTED".equalsIgnoreCase(status) || "ON_HOLD".equalsIgnoreCase(status)) {
            throw new RuntimeException("Cannot edit a vehicle that is currently " + status + ".");
        }

        return carRepository.update(car) > 0 ? "Car updated successfully!" : "Failed to update car.";
    }

    @Override
    public String deleteCar(Integer carId) {
        Car existingCar = carRepository.getCarById(carId);
        if (existingCar == null) {
            throw new RuntimeException("Car not found!");
        }

        String status = existingCar.getStatus().toString();
        if ("RENTED".equalsIgnoreCase(status) || "ON_HOLD".equalsIgnoreCase(status)) {
            throw new RuntimeException("Cannot delete a vehicle that is currently " + status + ". Please reject the pending booking first.");
        }

        return carRepository.delete(carId) > 0 ? "Car deleted successfully!" : "Failed to delete car.";
    }

    @Override
    public List<Car> getCarsByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }

    @Override
    public List<Car> getCarsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return carRepository.findByPriceRange(minPrice, maxPrice);
    }
    @Override
    public List<Car> searchCars(String brand, String fuelType, String search,BigDecimal minPrice, BigDecimal maxPrice) {
        return carRepository.searchCars(brand, fuelType, search,minPrice, maxPrice);
    }

    @Override
    public List<String> getAllBrands() {
        return carRepository.getAllBrands();
    }

    @Override
    public Car getCarById(Integer id) {
        return carRepository.getCarById(id);
    }
}
