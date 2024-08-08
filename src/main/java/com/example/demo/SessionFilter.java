package com.example.demo;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/*" })
@Order(0)
@Component
public class SessionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    	String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(true);
        System.out.println(requestURI);
        if (requestURI.startsWith("/css/") || requestURI.startsWith("/js/") || requestURI.startsWith("/images/") || requestURI.equals("/") || requestURI.equals("/auth/login")) {

            if (requestURI.equals("/") && session != null && session.getAttribute("uName") != null) {
                filterChain.doFilter(request, response);
                response.sendRedirect("/home/");
                return;
            }

            filterChain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("uName") == null) {
            response.sendRedirect("/");
            return;
        }

        filterChain.doFilter(request, response);
    }
}