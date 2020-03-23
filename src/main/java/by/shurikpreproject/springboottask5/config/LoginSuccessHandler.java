package by.shurikpreproject.springboottask5.config;

import by.shurikpreproject.springboottask5.model.Role;
import by.shurikpreproject.springboottask5.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        Set<Role> roles = authUser.getRoles();
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");
        if (roles.contains(adminRole)) {
            httpServletResponse.sendRedirect("/admin/welcome");
        } else if (roles.contains(userRole)) {
            httpServletResponse.sendRedirect("/user");
        } else httpServletResponse.sendRedirect("/test");
    }
}
