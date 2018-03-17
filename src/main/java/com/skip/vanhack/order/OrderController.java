package com.skip.vanhack.order;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public void createOrder(@RequestBody Order order) {

        // TODO Validate order and items

        order.setOrderStatus(OrderStatus.CREATED);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    @DeleteMapping("{orderId}")
    public void cancelOrder(@PathVariable("orderId") long orderId) {
        Order order = orderRepository.getOne(orderId);
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @GetMapping("{orderId}")
    public Order getOrder(@PathVariable("orderId") long orderId) {
        return orderRepository.getOne(orderId);
    }

    @GetMapping("{orderId}/status")
    public OrderStatus getOrderStatus(@PathVariable("orderId") long orderId) {
        return orderRepository.getOne(orderId).getOrderStatus();
    }
}
