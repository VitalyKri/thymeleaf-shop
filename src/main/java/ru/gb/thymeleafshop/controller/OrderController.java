package ru.gb.thymeleafshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.order.api.OrderGateway;
import ru.gb.gbapi.order.dto.OrderDto;


@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderGateway orderGateway;

    @GetMapping("/all")
    public String getProductList(Model model) {
        model.addAttribute("orders", orderGateway.getOrderList());
        return "order-list";
    }

    @GetMapping("/{orderId}")
    public String info(Model model, @PathVariable(name = "productId") Long id) {
        OrderDto orderDto;
        if (id != null) {
            orderDto = orderGateway.getOrder(id).getBody();
        } else {
            return "redirect:/order/all";
        }
        model.addAttribute("product", orderDto);
        return "order-form";
    }

    @GetMapping
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        OrderDto orderDto;

        if (id != null) {
            orderDto = orderGateway.getOrder(id).getBody();
        } else {
            orderDto = new OrderDto();
        }
        model.addAttribute("order", orderDto);
        return "order-form";
    }

    @PostMapping
    public String saveProduct(OrderDto orderDto) {
        orderGateway.handlePost(orderDto);
        return "redirect:/order/all";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        orderGateway.deleteById(id);
        return "redirect:/order/all";
    }

}
