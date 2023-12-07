package Halms.Watson.configuration;

import Halms.Watson.security.HolmesUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final HolmesUserDetailsService holmesUserDetailsService;
    private final DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/register/employee").hasAnyRole("admin")
                                .requestMatchers("/content/**").permitAll()
                                .requestMatchers("/recoverypage").permitAll()
                                .requestMatchers("/v1/users/create-users").permitAll()
                                .requestMatchers("/login-error").permitAll()
                                .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login-error")
                )
                .logout(logout -> {
                    logout.logoutSuccessUrl("/").permitAll();
                })
                .build();

    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(holmesUserDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authProvider;
    }

//    @Bean UserDetailsManager userDetailsService() {
//        UserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        try {
//            Users users = new Users();
//            users.setUsername("admin");
//            users.setPassword(bCryptPasswordEncoder().encode("agoga1322"));
//            users.setRole(Role.ROLE_ADMIN);
//            HolmesUserDetails holmesUserDetails = new HolmesUserDetails(users);
//            userDetailsManager.createUser(holmesUserDetails);
//    }
//        catch (Exception e) {
//            System.out.println(e);
//        }
//        return userDetailsManager;
//    }

    public void configureGlobal(AuthenticationManagerBuilder auth, HolmesUserDetailsService userService, PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

//    @Bean
//    public void configureGlobal(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
