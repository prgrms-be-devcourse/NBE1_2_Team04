package team4.footwithme.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import team4.footwithme.member.domain.Gender;
import team4.footwithme.member.domain.Member;
import team4.footwithme.member.domain.TermsAgreed;
import team4.footwithme.member.jwt.PrincipalDetails;

import java.util.Collection;
import java.util.List;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithMockPrincipalDetail> {


    @Override
    public SecurityContext createSecurityContext(WithMockPrincipalDetail annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();


        Member member = Member.create(annotation.email(), annotation.password(), "이름 1", "010-1234-5678", null, null, Gender.FEMALE, annotation.role(), TermsAgreed.AGREE);
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getMemberRole().text));
        PrincipalDetails principal = new PrincipalDetails(member, authorities);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
