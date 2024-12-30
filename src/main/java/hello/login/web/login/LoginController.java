package hello.login.web.login;


import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
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
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

@Slf4j
@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
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

    @PostMapping("/logout")
    public String logout(HttpServletResponse httpResponse) {
        // remove a cookie by removing time
        expireCookie(httpResponse, "memberId");
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse httpResponse, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        httpResponse.addCookie(cookie);
    }
}
