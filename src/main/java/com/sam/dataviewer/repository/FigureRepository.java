package com.sam.dataviewer.repository;

import com.sam.dataviewer.domain.Figure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Long> {

    @Query("SELECT f FROM Figure as f" +
            " join f.dashboard as d" +
            " GROUP BY d.id" +
            " ORDER BY d.id DESC")
    List<Figure> findGroupByDashboard();
}
