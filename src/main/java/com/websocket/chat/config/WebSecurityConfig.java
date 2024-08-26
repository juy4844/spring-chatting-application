package com.websocket.chat.config;

import com.websocket.chat.service.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Web Security 설정
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private PrincipalDetailService principalDetailService;

    @Autowired
    public WebSecurityConfig(PrincipalDetailService principalDetailService) {
        this.principalDetailService = principalDetailService;
    }

    @Bean
    public BCryptPasswordEncoder encodePWD(){ //비밀번호 암호화를 위해 사용 시큐리티는 비밀번호가 암호화 되있어야 사용가능하다
        return new BCryptPasswordEncoder();   //회원가입할때 쓰면된다.
    }

    // "/auth/**","/js/**","/css/**","/image/**",
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
                .authorizeRequests() //인가 요청이 오면
                .antMatchers("/", "/join", "/login").permitAll() //접근을 허용한다.
                .antMatchers("/chat/**").hasRole("USER") // chat으로 시작하는 리소스에 대한 접근 권한 설정
                .anyRequest().authenticated() //인증이 되야 들어갈 수 있다.// 기본값이 on인 csrf 취약점 보안을 해제한다. on으로 설정해도 되나 설정할경우 웹페이지에서 추가처리가 필요함.

            .and()
                .formLogin(); // 권한없이 페이지 접근하면 로그인 페이지로 이동한다.


        http.sessionManagement()
                .maximumSessions(1) //세션 최대 허용 수
                .maxSessionsPreventsLogin(false); // false이면 중복 로그인하면 이전 로그인이 풀린다.
    }

    /**
     * 테스트를 위해 In-Memory에 계정을 임의로 생성한다.
     * 서비스에 사용시에는 DB데이터를 이용하도록 수정이 필요하다.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }
}
