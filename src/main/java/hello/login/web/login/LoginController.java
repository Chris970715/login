package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpResponse;

@Slf4j
@Controller
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @Autowired
    public LoginController(LoginService loginService, SessionManager sessionManager) {
        this.loginService = loginService;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletResponse httpResponse) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        // Id and Password is not found
        if(loginMember == null) {
            bindingResult.reject("loginFail", "Id or Password is incorrect");
            return "login/loginForm";
        }

        //ToDo logic to manage login by using Cookie

        // a pair of key : value -- key is memberId and value = LoginId
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));

        // Adding Cookie to response (If no information about expire time is given, then, Cookie will be gone as client closes browser)
        httpResponse.addCookie(idCookie);

        //

        return "redirect:/";
    }

    // Adding Session
    //@PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletResponse httpResponse) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        // Id and Password is not found
        if(loginMember == null) {
            bindingResult.reject("loginFail", "Id or Password is incorrect");
            return "login/loginForm";
        }

        //ToDo logic to manage login by using Cookie

        // Create a session and add it on session
        sessionManager.createSession(loginMember, httpResponse);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletRequest request) {

        if(bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        // Id and Password is not found
        if(loginMember == null) {
            bindingResult.reject("loginFail", "Id or Password is incorrect");
            return "login/loginForm";
        }

        //ToDo logic to manage login by using Cookie
        // If session already exist, it will just return the existing one. If it does not, It will just create one
        HttpSession session =  request.getSession();
        // Add user's info in session
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logout(HttpServletResponse httpResponse) {
        // remove a cookie by removing time
        expireCookie(httpResponse, "memberId");
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest httpRequest) {
        // remove a cookie by removing time
        //expireCookie(httpResponse, "memberId");

        sessionManager.expire(httpRequest);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest httpRequest) {
        // remove a cookie by removing time
        //expireCookie(httpResponse, "memberId");

        // Get session //true -> creates new session
        HttpSession session = httpRequest.getSession(false);

        if(session != null) {
            // invalidate deletes session
            session.invalidate();
        }

        return "redirect:/";
    }


    private static void expireCookie(HttpServletResponse httpResponse, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);
    }
}
