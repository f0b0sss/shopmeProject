package com.shopme.admin.order.controller;

import com.shopme.admin.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

    @Autowired
    private OrderService service;

    @PostMapping("/orders_shipper/update/{orderId}/{status}")
    public Response updateOrderStatus(@PathVariable Long orderId, @PathVariable("status") String status) {
        service.updateStatus(orderId, status);
        return new Response(orderId, status);
    }
}

class Response {
    private Long orderId;
    private String status;

    public Response(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
