package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

           if(isLoginCheckPath(requestURI)) {
               log.info("logic of identifying {}", requestURI);
               HttpSession session =  httpRequest.getSession(false);

               if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null)
               {
                   log.info("No session found {}", requestURI);

                   //redirect to webpage where client wants to go to originally
                   httpResponse.sendRedirect("/login?redirectURL=" + requestURI);

                   return;
               }

           }
            //  To call next filter
           chain.doFilter(servletRequest, servletResponse);

         } catch (Exception e) {
             throw e;
         } finally {
             log.info("end filter {}", requestURI);
         }
    }

    // If url is matched with any of urls in the whitelist, 

    private boolean isLoginCheckPath(String requestURL){
        return !PatternMatchUtils.simpleMatch(whiteList, requestURL);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

