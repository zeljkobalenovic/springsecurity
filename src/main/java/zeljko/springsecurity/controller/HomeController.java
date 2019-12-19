package zeljko.springsecurity.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import zeljko.springsecurity.model.AuthenticationRequest;
import zeljko.springsecurity.model.AuthenticationResponse;
import zeljko.springsecurity.service.JwtUtil;

/*
 * HomeController
 */
@RestController
 public class HomeController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // kad hocemo samo obican string da vratimo korisniku na browser
    
    @GetMapping("/")                    // da bude svima dostupno ( unauthenticated )
    public String home() {                
        return "Welcome to App home";
    }

    @GetMapping("/user")                // da je dostupno onim userima sa rolama user i admin
    public String user() {                
        return "Welcome to user";
    }

    @GetMapping("/admin")               // da je dostupno samo userima sa rolom admin
    public String admin() {                
        return "Welcome to admin";
    }

    // za jwt dodajemo authentication endpoint



    @PostMapping("/auth")       // da bude svima dostupno
    public ResponseEntity <?> authenticate(@RequestBody final AuthenticationRequest authenticationRequest) throws Exception {               

     //   sa ova dva reda sam probao hardcode , sta god stigne iz postmana meni je radio kao da je korisnik baki koji ima sve privilegije   
     //   UsernamePasswordAuthenticationToken uToken = new UsernamePasswordAuthenticationToken("baki", "baki");
     //   final UserDetails userDetails = userDetailsService.loadUserByUsername("baki");
        
     try {
            authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
        );
        } catch (final BadCredentialsException exception) {
            throw new Exception("incorect username or password" , exception);  // korisnik nije autentikovan
        }

        // sada je korisnik autentikovan ( nije bacen exception )
        // VAZNO posle autentikacije setuje se principal ( logovani user ) . objekt principal zavisi od tipa autentikacije
        // kod koriscenja gornje ( autentikacija sa username i password) principal sadrzi samo username logovanog korisnika
      //  String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // kod nekih drugih implementacija autentikacije principal moze biti bogatiji , obicno onda sadrzi sve to jest principal bude tipa userdetails
        // posto kod nas nemamo userdetails ( principal je samo string sa username ) , a treba nam za pravljenje jwt moramo opet da pozivamo bazu po userdetails . ovo bi se moglo izbeci drugacijim generate jwt tokenom , a mozda postoji i varijanta da se userdetails negde sacuva pri autentikaciji , ali to sad necemo 

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

        
        


    }
}