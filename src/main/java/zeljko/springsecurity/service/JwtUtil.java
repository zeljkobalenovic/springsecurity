package zeljko.springsecurity.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JwtUtil
 */

@Service
 public class JwtUtil {

    // secret rec za kriptovanje jwt tokena , u praksi nikad ovako hardcodovano ima java klase za secret key
    
    private String SECRET_KEY = "secret" ;

    // prve dve metode za pravljenje jwt tokena (jedna poziva drugu , moze se ubaciti i nesto druge osim username)
    
    public String generateToken (UserDetails userDetails) {        
        Map<String,Object> claims = new HashMap<>(); 
        // claims je prazna mapa , ali ako osim username zelimo da ubacimo jos nesto tu ide 
        // moze biti bilo koji par (naziv-vrednost) koji zelimo da ubacimo u jwt token    
        // VAZNO !!! ova je public metoda i tu sve nastimavamo pa ona poziva private metodu create nju nediramo           
        return createToken (claims,userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    // metode za citanje jwt tokena - razne metode za citanje zapisanog i verifikaciju ispravnosti
    // posto je napravljen builderom svaki set u bilderu mozemo izvuci sa extract
    
    public <T> T extractClaim ( String token , Function <Claims , T> claimsresolver ) {
        final Claims claims = extractAllClaims (token);
        return claimsresolver.apply(claims);
    }

    private Claims extractAllClaims (String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration (String token) { 
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername (String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean validateToken( String token , UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }







    
}