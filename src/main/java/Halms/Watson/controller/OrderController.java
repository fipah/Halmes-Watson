package Halms.Watson.controller;

import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.Users;
import Halms.Watson.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// имя клиента, описание заказа, стоимость, дата создания, дата выполнения, имя сотрудника
@Controller
@RequestMapping(value = "/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    void CreateOrder (@RequestBody Clients client){
        orderService.createOrder(client);
    }


    @GetMapping
    List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    OrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PutMapping("/{id}/approve")
    void approveOrder(@PathVariable Long id) {
        Users principal = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.approveOrder(id, principal.getId());
    }

    @PutMapping("/{id}/complete")
    void completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
    }


}