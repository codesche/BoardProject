package project.board.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.board.member.service.MemberService;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserAuthenticationFailureHandler getFailureHandler() {
        return new UserAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
            .antMatchers("/"
                , "/member/register"
                , "/member/email-auth"
                , "/member/find/password"
                , "/member/reset/password"
            ).permitAll();

        http.authorizeRequests()
                .antMatchers("/admin/**")
                .hasAuthority("ROLE_ADMIN");

//        http.authorizeRequests()
//                .mvcMatchers(
//                    "/css/**",
//                    "/scripts/**",
//                    "/plugin/**",
//                    "/fonts/**",
//                    "/docs/**",
//                    "/webjars/**",
//                    "/custom.css",
//                    "/starter-template.css").permitAll();

        http.authorizeRequests()
            .antMatchers(
                "/css/**",
                "/scripts/**",
                "/plugin/**",
                "/fonts/**",
                "/docs/**",
                "/webjars/**",
                "/custom.css",
                "/starter-template.css").permitAll();


        http.formLogin()
            .loginPage("/member/login")
            .failureHandler(getFailureHandler())
            .permitAll();

        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true);

        http.exceptionHandling()
            .accessDeniedPage("/error/denied");

        super.configure(http);

    }

    // css 적용
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "/resources/**");

        // 로그인 안 해도 메인화면 슬라이드 이미지 출력되도록 수정
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

        super.configure(web);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(memberService)
            .passwordEncoder(getPasswordEncoder());

        super.configure(auth);
    }

}
