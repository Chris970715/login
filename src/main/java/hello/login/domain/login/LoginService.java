package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    private final MemberRepository memberRepository;

    @Autowired
    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member login(String loginId, String password) {
        /*
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member =  findMemberOptional.get();

        if(member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }*/
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }


}
