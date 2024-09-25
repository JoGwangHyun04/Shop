package com.example.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin((formLogin)->formLogin.loginPage("/members/login") //로그인 페이지 url
                        .defaultSuccessUrl("/") //로그인 성공시 이동할 url
                        .usernameParameter("email") // 로그인시 사용할 파라미터 이름으로 email 지정
                        .failureUrl("/members/login/error")) // 로그인 실패시 이동할 url
                .logout((logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url
                        .logoutSuccessUrl("/") // 로그아웃 성공시 이동할 url
                        .invalidateHttpSession(true)); // 로그아웃시 생성된 사용자 세션 삭제

        http.authorizeHttpRequests((authorizeHttpRequests)-> //접근권한설정
                authorizeHttpRequests.requestMatchers("/","/members/**","/item/**","/images/**").permitAll() // 모든사용자가 인증(로그인)없이 접속가능
                        .requestMatchers("/admin/**").hasRole("ADMIN") // /admin으로 시작시 관리자만 접속가능
                        .anyRequest().authenticated()); // 위의 경로를 제외한 나머지경로들은 모두 인증을 요구하도록 설정

        http.exceptionHandling((exceptionHandling)->
                exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())); // 인증되지 않은 사용자가 리소스접근시 수행되는 핸들러 등록


        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ // static 디렉터리 하위 파일은 인증을 무시함
        return (web)-> web.ignoring().requestMatchers("/css/**","/js/**","/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 패스워드를 암호화하여 db에저장
    }

}
