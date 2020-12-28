package com.sam.dataviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.dataviewer.domain.Member;
import com.sam.dataviewer.domain.Order;
import com.sam.dataviewer.dto.MemberDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.repository.OrderRepository;
import com.sam.dataviewer.service.MemberService;
import com.sam.dataviewer.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("의뢰 상세보기")
    @WithMockUser(roles="USER")
    public void orderDetailTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member);
        mockMvc.perform(get("/order/orderDetail/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderDetail"))
                .andExpect(model().attributeExists("orderDto", "fileDtos"));
    }

    @Test
    @DisplayName("의뢰 수정하기")
    @WithMockUser(username = "kim", password = "1234", roles = "USER")
    public void updateOrderTest() throws Exception {
        Member member = getMember();
        Order order = getOrder(member);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setTitle("order2");
        orderDto.setContent("content2");
        mockMvc.perform(post("/order/update").with(csrf())
                .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk());
    }

    private Member getMember() {
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername("kim");
        memberDto.setPassword("1234");
        return memberService.join(memberDto);
    }

    private Order getOrder(Member member) {
        OrderDto orderDto = new OrderDto();
        orderDto.setTitle("order");
        orderDto.setContent("content");
        Long id = orderService.order(member.getUsername(), orderDto);
        return orderRepository.getOne(id);
    }

    private MockMultipartFile getMultipartFile(String originalFileName) {
        return new MockMultipartFile(
                "data", originalFileName,
                "text/plain", originalFileName.getBytes());
    }

}