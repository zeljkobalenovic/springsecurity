package zeljko.springsecurity.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import zeljko.springsecurity.service.JwtFilter;

/**
 * SecurityConfiguration
 */

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // posto za jpa + mysql nema gotove varijante treba nam userDetailsService (
        // napravimo njegovu implementaciju sa @Service
        // anotacijom) pa ce auth njega koristiti za pribavljanje usera iz baze.
        // Deklarisemo ga i @Autowired gore
        // u service folderu sad pravim moju MyUserDetailsService.java koja OBAVEZNO
        // MORA da implementira UserDetailsService
        // takodje je VAZNO da bude samo jedna implementacija tog servisa, ili ako ih
        // ima vise moraju se obelezavati specijalno
        // (ima varijanta i za to ), tj. spring MORA znati koju implementaciju da
        // autoviruje

        // auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder()); // kad treba password encoder
        auth.userDetailsService(userDetailsService); // radi i ovo zato sto nas passwordencoder neradi nista pa moze bez njega


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests().antMatchers("/admin").hasRole("ADMIN").antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/", "/auth").permitAll().anyRequest().authenticated()
                // dovde je dosta za autentikaciju i vracanje jwt token korisniku, da bi korisnik stvarno mogao
                // da pristupi zasticenim resursima treba spring security da zna sta da radi sa jwt tokenom koji je 
                // korisnik poslao u authorization headeru - zato se pravi filter sa ( u nasem slucaju jwtfilte)
                // cija je uloga objasnjena u njemu
                .and().sessionManagement()
                // spring security nikad nece kreirati http sesiju i nece koristiti vec kreirane ako ih ima - mora ovako jer nije default
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // .and().formLogin();
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

   
    // mora se i ovo dodati ako se negde autovaruje authentication manager (treba za jwt auth)
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    
    
}