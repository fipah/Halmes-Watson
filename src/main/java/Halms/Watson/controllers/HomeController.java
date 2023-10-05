package Halms.Watson.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// контроллеры для фронта
// контроллер заявок
// контроллер сотрудников
// контроллер для зп

@Controller
@RequestMapping(value = "/home")
public class HomeController {
    @GetMapping
    public String openIndex(){
        return "home";
    }

}