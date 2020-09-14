package com.lwg.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lwg.vhr.service.HrService;
import com.lwg.vhr.service.impl.HrServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    HrServiceImpl hrService;

    @Autowired
    UrlFilterInvocationSecurityMetadataSource metadataSource;

    @Autowired
    UrlAccessDecisionMannger urlAccessDecisionMannger;

    @Autowired
    AuthenticationAccessDeniedHandler deniedHandler;


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/index.html", "/static/**","/doLogin");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(urlAccessDecisionMannger);
                        return o;
                    }
                })
                .and()
                .formLogin().loginPage("/doLogin").loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .failureHandler((req, resp, e) -> {

                    resp.setContentType("application/json;charset=utf-8");
                    RespBean respBean = null;
                    if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
                        respBean = RespBean.error("账户名或者密码输入错误");
                    } else if (e instanceof LockedException) {
                        respBean = RespBean.error("账户被锁定,请联系管理员");
                    } else if (e instanceof BadCredentialsException) {
                        respBean = RespBean.error("密码过期,请联系管理员");
                    } else if (e instanceof AccountExpiredException) {
                        respBean = RespBean.error("账户过期,请联系管理员");
                    } else if (e instanceof DisabledException) {
                        respBean = RespBean.error("账户被禁用,请联系管理员");
                    } else {
                        respBean = RespBean.error("登录失败");
                    }
                    resp.setStatus(401);
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(respBean));
                    out.flush();
                    out.close();
                })
                .successHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    RespBean respBean = RespBean.ok("登录成功", HrUtils.getCurrentHr());
                    PrintWriter out = resp.getWriter();
                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(respBean));
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .logout().permitAll()
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(deniedHandler);
    }
}
