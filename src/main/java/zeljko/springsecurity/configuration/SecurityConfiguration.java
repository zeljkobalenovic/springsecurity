package zeljko.springsecurity.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityConfiguration
 */

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // posto za jpa + mysql nema gotove varijante treba nam userDetailsService ( napravimo njegovu implementaciju sa @Service
        // anotacijom) pa ce auth njega koristiti za pribavljanje usera iz baze. Deklarisemo ga i @Autowired gore
        // u service folderu sad pravim moju MyUserDetailsService.java koja OBAVEZNO MORA da implementira UserDetailsService
        // takodje je VAZNO da bude samo jedna implementacija tog servisa, ili ako ih ima vise moraju se obelezavati specijalno 
        // (ima varijanta i za to ), tj. spring MORA znati koju implementaciju da autoviruje

        auth.userDetailsService(userDetailsService);
        
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.authorizeRequests()
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers("/user").hasAnyRole("USER","ADMIN")
            .antMatchers("/").permitAll()      
            .and().formLogin();

    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    
    
}