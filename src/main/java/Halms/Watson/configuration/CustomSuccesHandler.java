package Halms.Watson.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomSuccesHandler implements AuthenticationSuccessHandler {

    SimpleUrlAuthenticationSuccessHandler userSuccessHandler =
            new SimpleUrlAuthenticationSuccessHandler("/");
    SimpleUrlAuthenticationSuccessHandler adminSuccessHandler =
            new SimpleUrlAuthenticationSuccessHandler("/admin");
    SimpleUrlAuthenticationSuccessHandler employeeSuccessHandler =
            new SimpleUrlAuthenticationSuccessHandler("/orders/orders-employee");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (authorityName.equals("ROLE_ADMIN")) {
                // if the user is an ADMIN delegate to the adminSuccessHandler
                this.adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
            if(authorityName.equals("ROLE_EMPLOYEE")){
                this.employeeSuccessHandler.onAuthenticationSuccess(request, response, authentication);
            }
        }
        // if the user is not an admin delegate to the userSuccessHandler
        this.userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
