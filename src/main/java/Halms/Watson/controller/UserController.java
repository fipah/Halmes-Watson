package Halms.Watson.controller;

import Halms.Watson.model.Role;
import Halms.Watson.model.entity.Profile;
import Halms.Watson.model.entity.Users;
import Halms.Watson.repository.ProfileRepository;
import Halms.Watson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;


    @PostMapping("/")
    void createEmployee() {

    }

    @GetMapping("/avatar")
    ResponseEntity<Resource> getCurrentUserAvatar() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Profile> byUsername = profileRepository.findByUsername(principal.getUsername());
        if (byUsername.isEmpty()) {
            return null;
        }
        Profile profile = byUsername.get();
        if (Objects.isNull(profile.getAvatar())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(profile.getAvatarContentType()))
                        .body(new ByteArrayResource(profile.getAvatar()));
    }



    @GetMapping("/create-users")
    void createUsers() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            Profile profile = new Profile();
            profile.setUsername("admin");
            profileRepository.save(profile);
            var user = new Users();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("secret"));
            user.setRole(Role.ROLE_ADMIN);
            user.setName("");
            userRepository.save(user);

        }

        if (userRepository.findByUsername("user").isEmpty()) {
            Profile profile = new Profile();
            profile.setUsername("user");
            profile.setCity("Almaty");
            profile.setAddress("Dostyk");
            profile.setPhoneNumber("88005553535");
            profileRepository.save(profile);
            var user = new Users();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("secret"));
            user.setRole(Role.ROLE_USER);
            user.setName("");
            userRepository.save(user);
        }
    }

}
