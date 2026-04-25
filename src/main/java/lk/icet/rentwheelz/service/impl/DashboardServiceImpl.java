package lk.icet.rentwheelz.service.impl;

import lk.icet.rentwheelz.repository.DashboardRepository;
import lk.icet.rentwheelz.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalCustomers", dashboardRepository.getTotalCustomers());
        stats.put("totalManagers", dashboardRepository.getTotalManagers());
        stats.put("totalCars", dashboardRepository.getTotalCars());
        stats.put("activeBookings", dashboardRepository.getActiveBookings());
        stats.put("totalRevenue", dashboardRepository.getTotalRevenue());
        stats.put("monthlyRevenue", dashboardRepository.getMonthlyRevenue());
        stats.put("recentBookings", dashboardRepository.getRecentBookings());

        return stats;
    }
}
