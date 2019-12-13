package zeljko.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityConfiguration
 */

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // konfigurisanje autentifikacije

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // sad podesavamo auth objekat koji je tipa AuthenticationManagerBuilder koristi
        // se builder za gradjenje auth objekta
        // biramo inmemory authentikaciju i unosimo dva usera sa username i password
        // (role zasad nisu bitne one su za autorizaciju)

        auth.inMemoryAuthentication().withUser("test").password("test").roles("USER").and().withUser("baki")
                .password("baki").roles("ADMIN");
    }

    /*
     * obavezan u spring security je i password encoder jer se passwordi nikad
     * necuvaju kao obican text - za potrebe ove vezbe izabrao sam ovaj koji ustvari
     * nista neradi sa passwordom , ali je potreban da konfiguracija prodje (
     * precrtan je jer je deprecated)
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // konfigurisanje autorizacije
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        /* primer kako je bilo cemu zabranjen pristup osim ako korisnik nema rolu admin (formlogin odmah dodaje i logout)

        http.authorizeRequests()
            .antMatchers("/**").hasRole("ADMIN")
            .and()
            .formLogin();  // moramo ovo da bi nam dao formu za login na /login , ujedno dodaje i logout na /logout
            
        */

        // sad nas zahtevani primer

        http.authorizeRequests()
            .antMatchers("/admin").hasRole("ADMIN")
            .antMatchers("/user").hasAnyRole("USER","ADMIN")
            .antMatchers("/").permitAll()       // ovako kad svima treba da bude dozvoljeno
            .and().formLogin();
        
    }
    
     

    
}