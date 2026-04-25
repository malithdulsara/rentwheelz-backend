package lk.icet.rentwheelz.repository.impl;

import lk.icet.rentwheelz.model.Car;
import lk.icet.rentwheelz.repository.CarRepository;
import lk.icet.rentwheelz.util.CarStatus;
import lk.icet.rentwheelz.util.FuelType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CarRepositoryImpl implements CarRepository {
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Car> mapRowToCar() {
        return (rs, rowNum) -> {
            Car car = new Car();
            car.setCarId(rs.getInt("car_id"));
            car.setBrand(rs.getString("brand"));
            car.setModel(rs.getString("model"));
            String fuelTypeStr = rs.getString("fuel_type");
            if (fuelTypeStr != null) {
                car.setFuelType(FuelType.valueOf(fuelTypeStr));
            }
            car.setSeatingCapacity(rs.getInt("seating_capacity"));
            car.setPricePerDay(rs.getBigDecimal("price_per_day"));
            String statusStr = rs.getString("status");
            if (statusStr != null) {
                car.setStatus(CarStatus.valueOf(statusStr));
            }
            car.setImageUrl(rs.getString("image_url"));
            return car;
        };
    }

    public List<Car> findAll() {
        String sql = "SELECT * FROM cars";
        return jdbcTemplate.query(sql, mapRowToCar());
    }

    public int save(Car car) {
        String sql = "INSERT INTO cars (brand, model, fuel_type, seating_capacity, price_per_day, status, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                car.getBrand(),
                car.getModel(),
                car.getFuelType().name(),
                car.getSeatingCapacity(),
                car.getPricePerDay(),
                car.getStatus().name(),
                car.getImageUrl()
        );
    }

    @Override
    public int update(Car car) {
        String sql = "UPDATE cars SET brand=?, model=?, fuel_type=?, seating_capacity=?, price_per_day=?, status=?, image_url=? WHERE car_id=?";
        return jdbcTemplate.update(sql,
                car.getBrand(),
                car.getModel(),
                car.getFuelType().name(),
                car.getSeatingCapacity(),
                car.getPricePerDay(),
                car.getStatus().name(),
                car.getImageUrl(),
                car.getCarId()
        );
    }

    @Override
    public int delete(Integer carId) {
        String sql = "DELETE FROM cars WHERE car_id=?";
        return jdbcTemplate.update(sql, carId);
    }

    @Override
    public List<Car> findByBrand(String brand) {
        String sql = "SELECT * FROM cars WHERE brand = ?";
        return jdbcTemplate.query(sql, mapRowToCar(), brand);
    }

    @Override
    public List<Car> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String sql = "SELECT * FROM cars WHERE price_per_day BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, mapRowToCar(), minPrice, maxPrice);
    }

    @Override
    public List<Car> searchCars(String brand, String fuelType, String search, BigDecimal minPrice, BigDecimal maxPrice) {
        StringBuilder sql = new StringBuilder("SELECT * FROM cars WHERE 1=1");
        List<Object> args = new ArrayList<>();

        if (brand != null && !brand.trim().isEmpty()) {
            sql.append(" AND brand = ?");
            args.add(brand);
        }

        if (fuelType != null && !fuelType.trim().isEmpty()) {
            sql.append(" AND fuel_type = ?");
            args.add(fuelType);
        }

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (LOWER(brand) LIKE ? OR LOWER(model) LIKE ?)");
            String searchPattern = "%" + search.toLowerCase() + "%";
            args.add(searchPattern);
            args.add(searchPattern);
        }

        if (minPrice != null) {
            sql.append(" AND price_per_day >= ?");
            args.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price_per_day <= ?");
            args.add(maxPrice);
        }

        return jdbcTemplate.query(sql.toString(), mapRowToCar(), args.toArray());
    }

    @Override
    public List<String> getAllBrands() {
        String sql = "SELECT DISTINCT brand FROM cars WHERE brand IS NOT NULL ORDER BY brand ASC";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public Car getCarById(Integer id) {
        String sql = "SELECT * FROM cars WHERE car_id = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Car car = new Car();
                car.setCarId(rs.getInt("car_id"));
                car.setBrand(rs.getString("brand"));
                car.setModel(rs.getString("model"));
                car.setFuelType(FuelType.valueOf(rs.getString("fuel_type")));
                car.setSeatingCapacity(rs.getInt("seating_capacity"));
                car.setPricePerDay(rs.getBigDecimal("price_per_day"));
                car.setStatus(CarStatus.valueOf(rs.getString("status")));
                car.setImageUrl(rs.getString("image_url"));

                return car;
            }, id);

    }
}
