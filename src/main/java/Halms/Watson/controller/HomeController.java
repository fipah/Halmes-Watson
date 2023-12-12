package Halms.Watson.controller;

import Halms.Watson.constants.ContentTypeConsts;
import Halms.Watson.model.Role;
import Halms.Watson.model.dto.ProfileDto;
import Halms.Watson.model.entity.OrderStatus;
import Halms.Watson.model.entity.Profile;
import Halms.Watson.model.entity.SecretAnswer;
import Halms.Watson.model.entity.Users;
import Halms.Watson.model.enums.OrderStatusEnum;
import Halms.Watson.repository.OrderRepository;
import Halms.Watson.repository.ProfileRepository;
import Halms.Watson.repository.SecretAnswerRepository;
import Halms.Watson.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
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
    private final ProfileRepository profileRepository;
    private final OrderRepository orderRepository;

    @GetMapping
    public String openIndex() {
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

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("orders", orderRepository.getByStatus(OrderStatusEnum.PROCESSING));
        return "admin";
    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Profile users = profileRepository.findByUsername(username).get();
        model.addAttribute("user", users);
        return "edit-profile";
    }

    @PostMapping(value = "/edit-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editProfile(ProfileDto profile) throws IOException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Profile users = profileRepository.findByUsername(username).get();
        users.setCity(profile.getCity());
        users.setAddress(profile.getAddress());
        users.setPhoneNumber(profile.getPhoneNumber());
        users.setFullName(profile.getFullName());
        MultipartFile avatar = profile.getAvatar();
        if (Objects.nonNull(avatar)) {
            String contentType = avatar.getContentType();
            if (ContentTypeConsts.allowedContentTypes.contains(contentType)) {
                users.setAvatar(avatar.getBytes());
                users.setAvatarContentType(contentType);
            }

        }
        profileRepository.save(users);
        return "redirect:profile";
    }


    @GetMapping("/profile")
    public String profile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Profile users = profileRepository.findByUsername(username).get();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_EMPLOYEE"))) {
            Long salary = orderRepository.countPaymentForLastSevenDaysWithBonuses(username);
            model.addAttribute("salary", salary);
            Long inProgressCount = orderRepository.getCountByStatusAndEmployeeUsername(OrderStatusEnum.IN_PROGRESS.toString(), username);
            model.addAttribute("inProgressCount", inProgressCount.toString());
        }
        model.addAttribute("user", users);
        return "profile";
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
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    @Transactional
    public String addUser(@RequestParam Map<String, String> body, @RequestParam String username, Model model) {

        if (!body.get("password").equals(body.get("confirmPassword"))) {
            return "register";
        }
        Optional<Users> username1 = userRepository.findByUsername(body.get("username"));
        if (username1.isEmpty()) {
            Profile profile = new Profile();
            profile.setUsername(body.get("username"));
            profileRepository.save(profile);
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
            return "login";
        }
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register";

    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", "Неправильный логин или пароль");
        return "login";
    }

    @PostMapping(
            value = "/recoverypage",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
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


    @PostMapping("/changepassword")
    public String changePassword(@RequestParam Map<String, String> body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Optional<Users> byUsername = userRepository.findByUsername(username);
        if (!body.get("password").equals(body.get("confirmPassword"))) {
            return "redirect:/user-recovery-password";
        }
        Users users = byUsername.get();
        SecretAnswer answer = users.getAnswer();
        if (answer.getAnswer().equals(body.get("secret")));
        String password = body.get("password");
        users.setPassword(passwordEncoder.encode(password));
        userRepository.save(users);
        return "redirect:/profile";
    }

    @PostMapping(
            value = "/register/employee",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
            MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public void addEmployee(@RequestParam Map<String, String> body) {
        if (userRepository.findByUsername(body.get("username")).isEmpty()) {
            Profile profile = new Profile();
            profile.setUsername(body.get("username"));
            profileRepository.save(profile);
            var user = new Users();
            user.setUsername(body.get("username"));
            user.setPassword(passwordEncoder.encode(body.get("password")));
            user.setRole(Role.ROLE_EMPLOYEE);
            userRepository.save(user);
        }
    }

//    private String getErrorMessage(HttpServletRequest request, String key) {
//        Exception exception = (Exception) request.getSession().getAttribute(key);
//        String error = "Неправильный логин или пароль";
//        if (exception instanceof BadCredentialsException) {
//            error = "Invalid username and password!";
//        } else if (exception instanceof LockedException) {
//            error = exception.getMessage();
//        } else {
//            error = "Invalid username and password!";
//        }
//        return error;
//    }

}