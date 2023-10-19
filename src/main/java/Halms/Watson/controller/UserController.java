package Halms.Watson.controller;

import Halms.Watson.model.Role;
import Halms.Watson.model.entity.Users;
import Halms.Watson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/")
    void createEmployee() {

    }

//    @PostMapping("/agoga")
//    void createUser(@RequestB) {
//
//    }


    @GetMapping("/create-users")
    void createUsers() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            var user = new Users();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("secret"));
            user.setRole(Role.ROLE_ADMIN);
            userRepository.save(user);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            var user = new Users();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("secret"));
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
        }
    }
}
