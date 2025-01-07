package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/member/add", "/login", "/logout", "/css/*"};

    // Since there is default keyword used, No need to override init and destroy methods
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
         HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
         String requestURI = httpRequest.getRequestURI();

         HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

         try {
           log.info("requestURI: {}", requestURI);
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    // If url is matched with any of urls in the whitelist, 

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
