package zeljko.springsecurity.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SecurityConfiguration
 */

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // konfigurisanje autentifikacije

    
    // potrebno samo za varijante jdbc autentikacije - za inmemory zakomentarisati
    @Autowired
    private DataSource dataSource;      

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // sad podesavamo auth objekat koji je tipa AuthenticationManagerBuilder koristi
        // se builder za gradjenje auth objekta
        // biramo inmemory authentikaciju i unosimo dva usera sa username i password
        // (role zasad nisu bitne one su za autorizaciju)

        // primer za inmemory 
        auth.inMemoryAuthentication().withUser("test").password("test").roles("USER").and().withUser("baki")
                .password("baki").roles("ADMIN");
    

        // primer za jdbc autentikaciju (za ovo sam dodao jdbc api u pom.xml)

        /* prva varijanta sve default (default datasource je h2 baza , pa sam i nju dodao u pom.xml)

        auth.jdbcAuthentication()
            .dataSource(dataSource)             // koristice h2 default , ako hocemo drugu bazu dodamo podrsku u pom.xml i podesimo                                     // u application.properties datasource.url , username i password 
            .withDefaultSchema()                // napravice default bazu ako je nema 2 tabele user i autoriti , ako hocemo                                             // drugacije pravimo schema.sql gde definisemo tabele mi
            .withUser(                          // default , ako napravimo sa schema.sql nasu semu , onda pravimo i data.sql gde                                        // unosimo nase usere u user tabelu i nase autoritije u tu tabelu
                User.withUsername("test")
                    .password("test")
                    .roles("USER")
            )
            .withUser(
                User.withUsername("baki")
                    .password("baki")
                    .roles("ADMIN")
            );
        */ 
        
        /* druga varijanta isto h2 , ali sami pravimo bazu i popunjavamo userima

        auth.jdbcAuthentication()
            .dataSource(dataSource); // i dalje je default h2 - nista nemenjamo u application properties
            // razlika u odnosu na prethodni je sto sad koristimo nasu schemu iz schema.sql i nase usere iz data.sql
        */  
        
        /* treca varijanta ako imamo neku bazu sa nekim tabelama i to hocemo da koristimo

        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select username,password,enabled"
                                     + "from users"
                                     + "where username = ?")
            .authoritiesByUsernameQuery("select username,authority"
                                    + "from authorities"
                                     + "where username = ?");

            // sad mozemo sve da prilagodimo npr. ako baza nije zeljena onda u application.properties dodamo nasu
            spring.datasource.url=
            spring.datasource.username=
            spring.datasource.password=
            ako nije h2 nego npr oracle ili mysql dodamo i to u pom.xml

            u gornja dva querija prilagodimo nama nazive tabela i/ili kolona kakve su u datoj bazi 
        
        */
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