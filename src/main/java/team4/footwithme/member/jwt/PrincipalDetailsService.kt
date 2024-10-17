package team4.footwithme.member.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.repository.MemberRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일 입니다."));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getMemberRole().getText());

        PrincipalDetails userDetails = new PrincipalDetails(member, Collections.singleton(grantedAuthority));

        return userDetails;
    }
}
