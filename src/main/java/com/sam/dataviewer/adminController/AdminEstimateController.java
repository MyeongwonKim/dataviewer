package com.sam.dataviewer.adminController;

import com.sam.dataviewer.dto.EstimateDto;
import com.sam.dataviewer.dto.OrderDto;
import com.sam.dataviewer.service.EstimateService;
import com.sam.dataviewer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminEstimateController {

    private final EstimateService estimateService;
    private final OrderService orderService;

    @GetMapping("/estimate/newWithOrderId/{orderId}")
    public String createForm(@PathVariable Long orderId, Model model) {
        OrderDto orderDto = orderService.findOne(orderId);
        model.addAttribute("order", orderDto);
        model.addAttribute("estimateDto", new EstimateDto());
        return "admin/estimate/createEstimateForm";
    }

    @GetMapping("/estimate/new")
    public String createForm(Model model) {
        List<OrderDto> orderDtos = orderService.findAll();
        model.addAttribute("orders", orderDtos);
        model.addAttribute("estimateDto", new EstimateDto());
        return "admin/estimate/createEstimateForm";
    }

    @PostMapping("/estimate/new")
    public String createEstimate(
            @RequestParam("orderId") Long orderId,
            @Valid EstimateDto estimateDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "admin/estimate/createEstimateForm";
        }
        estimateService.request(orderId, estimateDto);
        return "redirect:/admin/estimates";
    }

    @GetMapping("/estimates")
    public String estimateList(Model model) {
        List<EstimateDto> estimateDtos = estimateService.findAll();
        model.addAttribute("estimateDtos", estimateDtos);
        return "admin/estimate/estimateList";
    }

    @GetMapping("/estimate/estimateDetail/{id}")
    public String estimateDetail(@PathVariable Long id, Model model){
        OrderDto orderDto = orderService.findOrderByEstimateId(id);
        EstimateDto estimateDto = estimateService.findOne(id);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("estimateDto", estimateDto);
        return "admin/estimate/estimateDetail";
    }

    @PostMapping("/estimate/update")
    public String updateEstimate(@Valid EstimateDto estimateDto) {
        estimateService.updateEstimate(estimateDto);
        return "redirect:/admin/estimates";
    }

    @GetMapping("/estimate/cancel/{id}")
    public String cancelEstimate(@PathVariable Long id) {
        estimateService.cancelEstimate(id);
        return "redirect:/admin/estimates";
    }

}
