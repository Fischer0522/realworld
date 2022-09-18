package com.fischer.config;

import com.fischer.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private TokenFilter tokenFilter;
    private AccessDeniedHandler accessDeniedHandler;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public SecurityConfig (
            TokenFilter tokenFilter,
            AccessDeniedHandler accessDeniedHandler,
            AuthenticationEntryPoint authenticationEntryPoint){
        this.accessDeniedHandler=accessDeniedHandler;
        this.authenticationEntryPoint=authenticationEntryPoint;
        this.tokenFilter=tokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/users/register").permitAll()
                .antMatchers("/articles/exact").permitAll()
                .antMatchers("/articles/fuzzy").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/articles/{slug}/comments").permitAll()
                .antMatchers("/tags").permitAll()
                .antMatchers("/email").permitAll()
                .antMatchers("/profiles/{username}").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);




        //配置异常处理器
        http.exceptionHandling()
                //配置认证失败处理器
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/img/**");
    }




    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }


}
