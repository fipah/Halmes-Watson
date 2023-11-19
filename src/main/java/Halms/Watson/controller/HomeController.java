package Halms.Watson.controller;

import Halms.Watson.model.Role;
import Halms.Watson.model.entity.SecretAnswer;
import Halms.Watson.model.entity.Users;
import Halms.Watson.repository.SecretAnswerRepository;
import Halms.Watson.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
import java.util.Optional;

// контроллеры для фронта
// контроллер заявок
// контроллер сотрудников
// контроллер для зп

@Controller
@RequiredArgsConstructor
public class HomeController {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SecretAnswerRepository secretAnswerRepository;

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

    @GetMapping("/recoverypage")
    public String recoveryPage() {
        return "recoverypage";
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
    @Transactional
    public String addUser(@RequestParam Map<String, String> body) {
        if (!body.get("password").equals(body.get("confirmPassword"))) {
            return "register";
        }
        if (userRepository.findByUsername(body.get("username")).isEmpty()) {
            var user = new Users();
            user.setUsername(body.get("username"));
            user.setPassword(passwordEncoder.encode(body.get("password")));
            user.setRole(Role.ROLE_USER);
            SecretAnswer secretAnswer = new SecretAnswer();
            secretAnswer.setAnswer(body.get("secret"));
            user.setAnswer(secretAnswer);
            user.setName(body.get("fullName"));
            Users save = userRepository.save(user);
            secretAnswer.setUser(save);
            secretAnswerRepository.save(secretAnswer);
        }

        return "home";
    }

    @PostMapping(
            value = "/recoverypage",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    @Transactional
    public String recoverPassword(@RequestParam Map<String, String> body) {
        Optional<Users> usernameOpt = userRepository.findByUsername(body.get("username"));
        if (!body.get("password").equals(body.get("confirmPassword"))) {
            return "recoverypage";
        }
        if (usernameOpt.isPresent()) {
            Users user = usernameOpt.get();
            SecretAnswer answer = user.getAnswer();
            if (answer.getAnswer().equals(body.get("secret"))) {
                String password = body.get("password");
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return "login";
            }
            return "recoverypage";
        }
        return "recoverypage";
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