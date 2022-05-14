package ru.gb.thymeleafshop.config;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class myTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession();
        SessionScopeBean attribute = (SessionScopeBean) session.getAttribute("scopedTarget.sessionScopeBean");
        if (attribute!=null ){
            response.addHeader("Authorization","Bearer "+attribute.getToken());
            request.setAttribute("Authorization","Bearer "+attribute.getToken());
        }

        filterChain.doFilter(request,response);
    }
}

