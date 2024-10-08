package team4.footwithme.security;

import org.springframework.security.test.context.support.WithSecurityContext;
import team4.footwithme.member.domain.MemberRole;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithMockPrincipalDetail {
    String email();
    MemberRole role() default MemberRole.USER;
    String password() default "12345678a!";

}
