package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.EstimateDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate {

    @Id
    @Column(name = "estimate_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String title;

    private String content;

    private Long price;

    private String duration;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private EstimateStatus status;  // OFFER, ACCEPT, PAID, CANCEL

    /* 연관관계 메서드 */
    public void setOrder(Order order) {
        this.order = order;
        order.getEstimates().add(this);
    }

    /* 생성 메서드 */
    public static Estimate createEstimate(
            Order order, String title, String content,
            Long price, String duration
    ) {
        Estimate estimate = new Estimate();
        estimate.setOrder(order);
        estimate.title = title;
        estimate.content = content;
        estimate.price = price;
        estimate.duration = duration;
        estimate.status = EstimateStatus.OFFER;
        return estimate;
    }

    /* dto Object로 변환 */
    public EstimateDto toDto() {
        EstimateDto dto = new EstimateDto();
        dto.setId(this.getId());
        dto.setTitle(this.getTitle());
        dto.setContent(this.getContent());
        dto.setPrice(this.getPrice());
        dto.setDuration(this.getDuration());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setStatus(this.getStatus());
        return dto;
    }

    /* 견적 수정 */
    public void update(String title, String content, Long price, String duration) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.duration = duration;
    }

    /* 견적 취소 */
    public void cancel() {
        this.status = EstimateStatus.CANCEL;
    }

    /* 견적 승인 */
    public void accept() {
        this.status = EstimateStatus.ACCEPT;
    }

    /* 결제 완료 */
    public void paid() {
        this.status = EstimateStatus.PAID;
    }
}
