package com.sam.dataviewer.dto;

import com.sam.dataviewer.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class OrderDto {

    private Long id;

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "분석 의뢰 사항을 기입해주세요.")
    private String content;

    private LocalDateTime createdAt;

    private OrderStatus status;

}
