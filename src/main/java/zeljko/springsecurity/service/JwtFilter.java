package zeljko.springsecurity.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties.Chain;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JwtFilter
 */

 // ovaj filter sluzi da presretne request , ako nadje validan jwt token u njegovom authorization headeru omoguci
 // izvrsavanje bez ponovne autentikacije tj. pusti request na zasticeni endpoint

 // obavezno extend onceperrequestfiltera i overajdovati dofilterinternal i posle baciti nazad u lanac gde springsecurity
 // izvrsava preostale poslove

@Component
 public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // prvo pokupimo token iz hedera
        String authorizationHeader = request.getHeader("Authorization");       
        
        // postavimo sve na null 
        String username = null;
        String jwt = null;
         // ispitamo heder  jer. moze token i da nebude u requestu kako treba
         // ako je token u hederu i pocinje sa bearer odsecemo vadimo jwt token i izvlacimo username iz njega   
        if ( authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }
        // ako je bilo uredu imamo username , ako nije username je ostao null
        // potrebno je i da je authentication objekat prazan , a prazan je jer necemo cuvati nista bice stateles
        if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
           
           // za validaciju nam kao i za kreiranje treba userdetails za primljeni username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            // validiramo token , da li je istekao i da li je odgovarajuci za usera
            if ( jwtUtil.validateToken(jwt, userDetails)) {

                // ako je jwt token validan stavlja userdetails u security context
                // isti objekat kao u home controleru , ali drugi konstruktor (ovaj prima principal, credentials, autoritije)
                // tamo je primao samo principal i credentials i koristio se kad korisnik NIJE autentikovan isAuthenticated false
                // ovde ispravnog korisnika smatramo da JESTE autentikovan isAuthenticated true i smestamo ga u security
                // context za dalje procesiranje ( npr. autorizacija u odnosu za role i sl.)
                
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                // ovaj red ??? zasto mu setujemo Details 
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // sad ga smesamo u security context za dalje 
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
      // ubacujemo ga u lanac filtera ostalih   
     filterChain.doFilter(request, response);
        
    }

    
}