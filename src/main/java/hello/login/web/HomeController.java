package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    @Autowired
    public HomeController(MemberRepository memberRepository, SessionManager sessionManager) {
        this.memberRepository = memberRepository;
        this.sessionManager = sessionManager;
    }

    //@GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model){

        if(memberId == null){
            return "home";
        }

        //Login
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);

        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        // Search user's information in session
        Member member = (Member)sessionManager.getSession(request);

        //Login
        if(member == null){
            return "home";
        }

        model.addAttribute("member", member);

        return "loginHome";
    }

   //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){
        // by default, getSession creates new session. Therefore, false is recommanded
        HttpSession session = request.getSession(false);
        if(session == null) {
            return "home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        // If session does not
        if(loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model){
        // by default, getSession creates new session. Therefore, false is recommanded

        // If session does not
        if(loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}