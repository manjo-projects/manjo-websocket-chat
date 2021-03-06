package io.manjo.chat.configuration;

import io.manjo.chat.property.WebSecurityProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import static java.lang.String.format;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
public class WebSecurityConfigurtion extends WebSecurityConfigurerAdapter {

    @Autowired
    private WebSecurityProperty properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        System.out.println("security");
        System.out.println(properties);
        System.out.println("--------");

        http
                // global configuration
                .authorizeRequests()
                .antMatchers(format("%s/**", properties.getPathPrefix())).permitAll()
                .anyRequest().authenticated()
                // log-in configuration
                .and()
                .formLogin().loginPage(properties.getLoginPage()).permitAll()
                .usernameParameter(properties.getUsernameParameter()).passwordParameter(properties.getPasswordParameter())
                .loginProcessingUrl(properties.getLoginProcessingUrl())
                // remember-me configuration
                .and()
                .rememberMe()
                .rememberMeParameter(properties.getRememberMe().getParameterName())
                .rememberMeCookieName(properties.getRememberMe().getCookieName())
                .key(properties.getRememberMe().getSecretKey())
                .tokenValiditySeconds(properties.getRememberMe().getValiditySeconds())
                // log-out configuration
                .and()
                .logout().logoutUrl(properties.getLogoutPage()).deleteCookies(properties.getSessionCookie());
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = createDelegatingPasswordEncoder();

        auth
                .inMemoryAuthentication()
                .withUser("user")
                .password(encoder.encode("password"))
                .roles("USER");

        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN");
    }

}
