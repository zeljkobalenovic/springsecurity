package zeljko.springsecurity.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import zeljko.springsecurity.model.MyUserDetails;
import zeljko.springsecurity.model.User;
import zeljko.springsecurity.repository.UserRepository;

/**
 * MyUserDetailsService
 */

// implementiramo userdetailsservice tj. overajdujemo jedinu metodu loaduserbyusername
// metoda dobija kao parametar username od authenticationmanagera , a vraca mu userdetails iz baze ako ga nadje ili baca
// exception ako ga nenadje. ako ga nadje userdetails sadrzi sve potrebne podatke o useru ( ukupno 7)
// UserDetails je interfejs pa nemozemo direktno da ga vratimo nego ga implementiramo pa vracamo nasu implementaciju

@Service
 public class MyUserDetailsService implements UserDetailsService {
    
    
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        // prava jpa - mysql varijanta
        // prvo za trazenog username vracamo naseg komplet usera iz baze - ili bacamo exception ako ga nema
        Optional <User> user = userRepository.findByUsername(username);

        // kad smo dobili naseg usera prosledjujemo ga u MyUserDetails koji pravi usera kojeg spring security ocekuje ( tipa UserDetails)
        // posto je optional , ako ga nema bacamo gresku
        user.orElseThrow(()->new UsernameNotFoundException("User not found"));

        // ako ga ima moramo da ga pretvorimo u obicnog usera pa da napravimo novi myuserdetails sa parametrom user
        
        // User us = user.get();    
        // return new MyUserDetails(userakogaima);

        // lepse napisano gornje (uzmi usera pa ga prosledi u parametar sa pozivom new myuserdetails prosledjen moj user , sto vraca
        // usera tipa userdetails koji nama vraca , pa ga sa get izvlacimo i vracamo kroz return - tako se jedan objekat mapira u drugi)
       
        return user.map(MyUserDetails::new).get();
        
    }

    
}