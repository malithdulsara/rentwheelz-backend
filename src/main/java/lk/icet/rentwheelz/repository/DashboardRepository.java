package lk.icet.rentwheelz.repository;

import java.util.List;
import java.util.Map;

public interface DashboardRepository {
    int getTotalCustomers();
    int getTotalManagers();
    int getTotalCars();
    int getActiveBookings();
    double getTotalRevenue();
    List<Double> getMonthlyRevenue();
    List<Map<String, Object>> getRecentBookings();
}
