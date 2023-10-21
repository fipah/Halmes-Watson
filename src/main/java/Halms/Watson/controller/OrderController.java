package Halms.Watson.controller;

import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.Users;
import Halms.Watson.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// имя клиента, описание заказа, стоимость, дата создания, дата выполнения, имя сотрудника
@Controller
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @RequestMapping(value = "/order-submit-action")
    public String CreateOrder ( @ModelAttribute("client") Clients client, BindingResult bindingResult, ModelMap model){
        if (bindingResult.hasErrors()) {
            return "order-submit";
        }
        orderService.createOrder(client);
        model.clear();
        return "redirect:client-order";
    }

    @RequestMapping("/order-submit")
    public String getOrderSubmitPage(ModelMap model) {
        model.addAttribute("client", new Clients());
        return "order-submit";
    }


    @RequestMapping("/client-order")
    String getClientOrders(Model model ) {
        List<OrderDTO> allOrders = orderService.getAllOrdersByUser();
        model.addAttribute("orders", allOrders);
        return "client-order";
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