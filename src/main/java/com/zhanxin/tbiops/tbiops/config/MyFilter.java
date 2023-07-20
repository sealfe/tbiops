package com.zhanxin.tbiops.tbiops.config;

import com.zhanxin.tbiops.tbiops.dto.JsonException;
import com.zhanxin.tbiops.tbiops.repository.TokenCookie;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class MyFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String requestURI = httpServletRequest.getRequestURI();
            if (requestURI.contains("login")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            String token = httpServletRequest.getParameter("token");
            if (token == null) {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is null.");
                return;
            }
            Boolean cookieValid = TokenCookie.cookieValid(token);
            if (!cookieValid) {
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}