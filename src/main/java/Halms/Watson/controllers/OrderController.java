package Halms.Watson.controllers;

import Halms.Watson.model.Clients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// имя клиента, описание заказа, стоимость, дата создания, дата выполнения, имя сотрудника
@Controller
@RequestMapping(value = "/v1/orders")
public class OrderController {
    @PostMapping
    void CreateOrder (@RequestBody Clients client){
        return;
    }
    @GetMapping

}