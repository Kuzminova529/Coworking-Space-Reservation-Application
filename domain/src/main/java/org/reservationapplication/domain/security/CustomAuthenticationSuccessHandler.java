package org.reservationapplication.domain.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", userDetails.getUser());

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = request.getContextPath();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            redirectUrl += "/admin/home";
        } else {
            redirectUrl += "/customer/home";
        }

        response.sendRedirect(redirectUrl);
    }
}
