package com.sam.dataviewer.domain;

import com.sam.dataviewer.dto.EstimateDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EstimateTest {

    @Test
    public void createEstimate() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);

        String title = "견적";
        Long price = 150000L;

        //when
        Estimate estimate = Estimate.createEstimate(
                order, title, null,
                price, null
        );

        //then
        assertThat(order).isEqualTo(estimate.getOrder());
        assertThat(title).isEqualTo(estimate.getTitle());
        assertThat(price).isEqualTo(estimate.getPrice());
    }

    @Test
    public void toDto() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);

        Estimate estimate = Estimate.createEstimate(
                order, "견적", "내용",
                null, "15일"
        );

        //when
        EstimateDto dto = estimate.toDto();

        //then
        assertThat(estimate.getTitle()).isEqualTo(dto.getTitle());
        assertThat(estimate.getContent()).isEqualTo(dto.getContent());
        assertThat(estimate.getDuration()).isEqualTo(dto.getDuration());
    }

    @Test
    public void update() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Estimate estimate = Estimate.createEstimate(
                order, "견적", null,
                10000L, null
        );

        String newTitle = "수정된 견적";
        Long newPrice = 50000L;

        //when
        estimate.update(
                newTitle, null,
                newPrice, null
        );

        //then
        assertThat(newTitle).isEqualTo(estimate.getTitle());
        assertThat(newPrice).isEqualTo(estimate.getPrice());
    }

    @Test
    public void cancel() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Estimate estimate = Estimate.createEstimate(
                order, "견적", null,
                null, null
        );

        //when
        estimate.cancel();

        //then
        assertThat(EstimateStatus.CANCEL).isEqualTo(estimate.getStatus());
    }

    @Test
    public void accept() throws Exception {
        //given
        Member member = getMember();
        Order order = getOrder(member);
        Estimate estimate = Estimate.createEstimate(
                order, "견적", null,
                null, null
        );

        //when
        estimate.accept();

        //then
        assertThat(EstimateStatus.ACCEPT).isEqualTo(estimate.getStatus());
    }

    private Member getMember() {
        Member member = Member.createMember(
                "kim", null,
                null, null, null,
                null, null
        );
        return member;
    }

    private Order getOrder(Member member) {
        Order order = Order.createOrder(member, "의뢰", "내용");
        return order;
    }

}