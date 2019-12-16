package zeljko.springsecurity.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {

    // sad nam treba konstruktor koji pozivamo u MyUserDetailsService znaci sa
    // parametrom User i + treba implementirati metode

    /**
     *
     */
    
     private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    
    // 
    
    
    public MyUserDetails(User user) {
        this.username=user.getUsername();
        this.password=user.getPassword();
        this.active=user.isActive();
        
        // String rola = user.getRoles();
        // String[] roleniz = rola.split(",");
        // List<String> rolelista = Arrays.asList(roleniz);
        
        // List<GrantedAuthority> galista = new ArrayList<GrantedAuthority>();
        
        // rolelista.forEach(item -> {
        //     GrantedAuthority ga = new SimpleGrantedAuthority(item);
        //     galista.add(ga);
        // });
                
        // this.authorities=galista;
        
        // krace napisana gornja varijanta isto kao i u myuserdetailservice samo sto se ovde niz objekata jedne vrste prebacuje u listu
        // objekata druge vrste

        this.authorities=Arrays.stream(user.getRoles().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
        }                        

    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }




}   