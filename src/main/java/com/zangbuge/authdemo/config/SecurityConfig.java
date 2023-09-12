package com.zangbuge.authdemo.config;

import com.zangbuge.authdemo.security.AccessDeniedHandlerImpl;
import com.zangbuge.authdemo.security.TokenFilter;
import com.zangbuge.authdemo.security.UnauthorizedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author: lhm
 * @date: 2023/8/24
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private TokenFilter tokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 禁用 CSRF
        http.csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/userLogin", "/api/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 添加过滤器
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 开启跨域
        http.cors();
        // 禁用缓存
        http.headers().cacheControl();
        // 异常处理器
        http.exceptionHandling()
                // 认证失败
                .authenticationEntryPoint(new UnauthorizedHandler())
                // 权限失败
                .accessDeniedHandler(new AccessDeniedHandlerImpl());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 也可以自定义加密方式, new一个匿名类 PasswordEncoder, 重写加密方法
        return new BCryptPasswordEncoder();
    }

}
