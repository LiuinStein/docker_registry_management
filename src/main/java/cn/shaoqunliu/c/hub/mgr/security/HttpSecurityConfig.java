package cn.shaoqunliu.c.hub.mgr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Collections;

@Configuration
public class HttpSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MgrAuthenticationProvider mgrAuthenticationProvider;
    private final MgrAuthenticationEntryPoint mgrAuthenticationEntryPoint;

    @Autowired
    public HttpSecurityConfig(MgrAuthenticationProvider mgrAuthenticationProvider, MgrAuthenticationEntryPoint mgrAuthenticationEntryPoint) {
        this.mgrAuthenticationProvider = mgrAuthenticationProvider;
        this.mgrAuthenticationEntryPoint = mgrAuthenticationEntryPoint;
    }

    @Override
    public ProviderManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(
                mgrAuthenticationProvider
        ));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1/user").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(
                        new MgrHttpBasicAuthenticationFilter(
                                authenticationManager(), mgrAuthenticationEntryPoint),
                        BasicAuthenticationFilter.class
                ).exceptionHandling()
                .authenticationEntryPoint(mgrAuthenticationEntryPoint);
    }
}
