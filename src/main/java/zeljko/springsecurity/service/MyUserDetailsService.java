package zeljko.springsecurity.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import zeljko.springsecurity.model.MyUserDetails;

/**
 * MyUserDetailsService
 */

// implementiramo userdetailsservice tj. overajdujemo jedinu metodu loaduserbyusername
// metoda dobija kao parametar username od authenticationmanagera , a vraca mu userdetails iz baze ako ga nadje ili baca
// exception ako ga nenadje. ako ga nadje userdetails sadrzi sve potrebne podatke o useru ( ukupno 7)
// UserDetails je interfejs pa nemozemo direktno da ga vratimo nego ga implementiramo pa vracamo nasu implementaciju

@Service
 public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        // prvo hardcode tj. kad kucamo login prihvatice bilo koji username jer ce taj biti vracen a password je pass
        return new MyUserDetails(username);

    }

    
}