package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        log.info("LogFilter doFilter");

        // ServletRequest is the parent class of HttpServletRequest. ServletRequest does not contain as many features as HttpServlet...
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String requestURI = httpRequest.getRequestURI();

        String uuid  = UUID.randomUUID().toString();

        try {
            log.info("Request [{}] [{}]", uuid, requestURI);

            // Calling next filters
            chain.doFilter(servletRequest, servletResponse);


        } catch (Exception e) {
            throw e;
        } finally {
            log.info("Response [{}] [{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("LogFilter destroy");
    }
}
