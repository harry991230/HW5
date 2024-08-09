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
public class AccessControlFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    	String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(true);

        // 放行靜態資源
        if (requestURI.startsWith("/css/") || requestURI.startsWith("/js/") || requestURI.startsWith("/images/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 若使用者訪問登入頁面 "/", 且使用者狀態為已登入, 重導向 /home/
        if (requestURI.equals("/") && session != null && session.getAttribute("user") != null) {
            response.sendRedirect("/home/");
            return;
        }

        // 放行登入頁面
        if (requestURI.equals("/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 放行登入請求路徑, 且 Request Method 為 POST
        if (requestURI.equals("/auth/login") && request.getMethod().equalsIgnoreCase("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 未登入, 重導向登入頁面
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/");
            return;
        }

        // 已登入, 放行
        filterChain.doFilter(request, response);
    }
}