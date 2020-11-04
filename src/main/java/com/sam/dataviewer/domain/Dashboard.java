package com.sam.dataviewer.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Dashboard {

    @Id
    @Column(name = "dashboard_id")
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "dashboard")
    private List<Figure> figures = new ArrayList<>();

    /* 생성 메서드 */
    public static Dashboard createDashboard(
            String title, String content
    ) {
        Dashboard dashboard = new Dashboard();
        dashboard.title = title;
        dashboard.content = content;
        return dashboard;
    }
}
