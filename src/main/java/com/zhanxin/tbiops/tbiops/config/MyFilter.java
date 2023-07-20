package com.zhanxin.tbiops.tbiops.config;

import com.zhanxin.tbiops.tbiops.dto.JsonException;
import com.zhanxin.tbiops.tbiops.http.acl.BkTokenService;
import com.zhanxin.tbiops.tbiops.repository.TokenCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class MyFilter implements Filter {


    @Autowired
    private BkTokenService bkTokenService;

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
                throw new JsonException("10023", "token is null");
            }
            TokenCookie.checkCookie(token);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}