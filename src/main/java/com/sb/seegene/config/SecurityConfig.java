package com.sb.seegene.config;

import com.sb.seegene.config.jwt.JwtAuthenticationFilter;
import com.sb.seegene.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] PERMIT_ALL_PATTERNS = new String[] {
        "/api/login",
        "/api/login/*",
        "api/member/singup",
        "api/member/singup/*",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic().disable()
                .csrf().disable()
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                // 해당 API에 대해서는 모든 요청을 허가
//                .requestMatchers(PERMIT_ALL_PATTERNS).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/login/jwt/sign-in")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/member/signup")).permitAll()
                // USER 권한이 있어야 요청할 수 있음
//                .requestMatchers(new AntPathRequestMatcher("/api/login/test")).hasRole("USER")
                // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                .anyRequest().authenticated()
                .and()
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }

}
