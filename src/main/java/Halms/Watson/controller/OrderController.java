package Halms.Watson.controller;

import Halms.Watson.model.dto.Clients;
import Halms.Watson.model.dto.OrderDTO;
import Halms.Watson.model.entity.Orders;
import Halms.Watson.model.entity.Profile;
import Halms.Watson.model.entity.Service;
import Halms.Watson.model.entity.Users;
import Halms.Watson.repository.ServiceRepository;
import Halms.Watson.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// имя клиента, описание заказа, стоимость, дата создания, дата выполнения, имя сотрудника
@Controller
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ServiceRepository serviceRepository;

    @RequestMapping(value = "/order-submit-action", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String CreateOrder (@ModelAttribute("client") Clients client, BindingResult bindingResult, ModelMap model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "order-submit";
        }
        orderService.createOrder(client);
        model.clear();
        return "redirect:client-order";
    }

    @RequestMapping("/order-submit")
    public String getOrderSubmitPage(Model model) {
        model.addAttribute("client", new Clients());
        List<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "order-submit";
    }

    @RequestMapping("/orders-employee")
    public String ordersEmployee(Model model){
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders-employee";
    }

    @RequestMapping("/client-order")
    String getClientOrders(Model model ) {
        List<OrderDTO> allOrders = orderService.getAllOrdersByUser();
        model.addAttribute("orders", allOrders);
        return "client-order";
    }

    @PostMapping("/order/{id}/deletion")
    String deleteOrder(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.deleteByIdAndUserId(id, user.getUsername());
        return "redirect:../../client-order";
    }
    @PostMapping("/orders-employee/{id}/deletion")
    String employeeDeleteOrder(@PathVariable Long id){
        orderService.deleteById(id);
        return "redirect:../../orders-employee";
    }


    @GetMapping
    List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    OrderDTO getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping("/{id}/approve")
    String approveOrder(@PathVariable Long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.approveOrder(id, principal.getUsername());
        return "redirect:../orders-employee";
    }
    @PostMapping("/{id}/process")
    String processOrder(@PathVariable Long id){
        Optional<Orders> byId = orderService.findById(id);
        return "";
    }

    @PutMapping("/{id}/complete")
    void completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
    }

    @GetMapping("/{id}/photo")
    ResponseEntity<Resource> getOrderConfirmationPhoto(@PathVariable Long id) {
        Optional<Orders> order = orderService.findById(id);
        if (order.isEmpty()) {
            return null;
        }
        Orders profile = order.get();
        if (Objects.isNull(profile.getConfirmationPhoto())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(profile.getConfirmationPhotoContentType()))
                .body(new ByteArrayResource(profile.getConfirmationPhoto()));
    }
}