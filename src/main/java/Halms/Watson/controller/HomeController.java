package Halms.Watson.controller;

import Halms.Watson.model.Role;
import Halms.Watson.model.entity.Users;
import Halms.Watson.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

// контроллеры для фронта
// контроллер заявок
// контроллер сотрудников
// контроллер для зп

@Controller
@RequiredArgsConstructor
public class HomeController {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping
    public String openIndex(){
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

//    @GetMapping("/orders")
//    public String orders() {
//        return "client-order";
//    }

//    @GetMapping("/order-submit")
//    public String orderSubmit() {
//        return "order-submit";
//    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public void addUser(@RequestParam Map<String, String> body) {
        if (userRepository.findByUsername(body.get("username")).isEmpty()) {
            var user = new Users();
            user.setUsername(body.get("username"));
            user.setPassword(passwordEncoder.encode(body.get("password")));
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }
    }


    @PostMapping(
            value = "/register/employee",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public void addEmployee(@RequestParam Map<String, String> body) {
        if (userRepository.findByUsername(body.get("username")).isEmpty()) {
            var user = new Users();
            user.setUsername(body.get("username"));
            user.setPassword(passwordEncoder.encode(body.get("password")));
            user.setRole(Role.ROLE_EMPLOYEE);
            userRepository.save(user);
        }
    }
    private String getErrorMessage(HttpServletRequest request, String key) {
        Exception exception = (Exception) request.getSession().getAttribute(key);
        String error = "";
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username and password!";
        } else if (exception instanceof LockedException) {
            error = exception.getMessage();
        } else {
            error = "Invalid username and password!";
        }
        return error;
    }

}