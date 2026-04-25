package lk.icet.rentwheelz.controller;

import lk.icet.rentwheelz.model.Car;
import lk.icet.rentwheelz.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public List<Car> getAllCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        List<Car> cars = carService.searchCars(brand, fuelType, search,minPrice, maxPrice);
        return cars;
    }

    @PostMapping
    public String saveCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Integer id, @RequestBody Car car) {
        try {
            car.setCarId(id);
            String result = carService.updateCar(car);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Integer id) {
        try {
            String result = carService.deleteCar(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/brand/{brand}")
    public List<Car> getCarsByBrand(@PathVariable String brand) {
        return carService.getCarsByBrand(brand);
    }

    @GetMapping("/price")
    public List<Car> getCarsByPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return carService.getCarsByPriceRange(min, max);
    }

    @GetMapping("/brands")
    public List<String> getAllBrands() {
        List<String> brands = carService.getAllBrands();
        return brands;
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Integer id) {
        return carService.getCarById(id);
    }
}
