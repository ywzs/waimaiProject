package com.ywzs.controller;

import com.ywzs.common.R;
import com.ywzs.entity.Orders;
import com.ywzs.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrdersService ordersService;
    @PostMapping("/submit")
    public R<Orders> createOrders(@RequestBody Orders orders){
        return ordersService.submit(orders);
    }
}
