package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
*  To manage sessions
* */

@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /*
    *  Create session
    * */

    public void createSession(Object value, HttpServletResponse response) {
        // Create Id on Sessions and save in under sessions

        // Create an unpredictable random
        String sessionId =  UUID.randomUUID().toString();
        // Add a session on session storage
        sessionStore.put(sessionId, value);

        // Create a cookie
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        // Add a cookie to response
        response.addCookie(mySessionCookie);
    }

    // Find Session

    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);

        if(sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    // Expire sessions

    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }

    }
}
