package com.sam.dataviewer.service;

import com.sam.dataviewer.domain.Dashboard;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.DashboardDto;
import com.sam.dataviewer.repository.DashboardRepository;
import com.sam.dataviewer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Long create(Long orderId, DashboardDto dto) {
        Order order = orderRepository.getOne(orderId);
        Dashboard dashboard = Dashboard.createDashboard(
                order, dto.getTitle(), dto.getContent()
        );
        dashboardRepository.save(dashboard);
        return dashboard.getId();
    }

    public List<DashboardDto> findAll() {
        List<Dashboard> dashboards = dashboardRepository.findByOrderByIdDesc();
        List<DashboardDto> dashboardDtos = new ArrayList<>();
        for (Dashboard dashboard: dashboards) {
            dashboardDtos.add(dashboard.toDto());
        }
        return dashboardDtos;
    }
}
