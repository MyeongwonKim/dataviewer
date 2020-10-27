package com.sam.dataviewer.form;

import com.sam.dataviewer.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class OrderForm {

    private Long id;

    @NotEmpty(message = "제목을 기입해주세요.")
    private String title;

    @NotEmpty(message = "분석 의뢰 사항을 기입해주세요.")
    private String content;

    private LocalDateTime createdAt;

    private String file;

    private OrderStatus status;

}
