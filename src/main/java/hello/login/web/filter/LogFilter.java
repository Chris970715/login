package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("LogFilter doFilter");

        // ServletRequest is the parent class of HttpServletRequest. ServletRequest does not contain as many features as HttpServlet...
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

    }

    @Override
    public void destroy() {
        log.info("LogFilter destroy");
    }
}
