package br.com.emersonluiz.auth.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import br.com.emersonluiz.auth.model.AuthenticationSession;

public class PreAuthenticatedUser extends User {

    private static final long serialVersionUID = 722309505318792319L;
    private String plainUserName;

    private PreAuthenticatedUser(UUID userid, String username, Collection<? extends GrantedAuthority> authorities) {
        super(userid.toString(), "N/A", true, true, true, true, authorities);
        this.plainUserName = username;
    }

    public static PreAuthenticatedUser getFromAuthenticationSession(AuthenticationSession authenticationSession) {
        // session authorities to granted authorities
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String authority : authenticationSession.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        // create and return user
        return new PreAuthenticatedUser(authenticationSession.getUserid(), authenticationSession.getUsername(), authorities);
    }

    public String getPlainUserName() {
        return plainUserName;
    }

    public void setPlainUserName(String plainUserName) {
        this.plainUserName = plainUserName;
    }

    @Override
    public String toString() {
        return "PreAuthenticatedUser [plainUserName=" + plainUserName + ", getAuthorities()=" + getAuthorities()
                + ", getPassword()=" + getPassword() + ", getUsername()=" + getUsername() + ", isEnabled()=" + isEnabled()
                + ", isAccountNonExpired()=" + isAccountNonExpired() + ", isAccountNonLocked()=" + isAccountNonLocked()
                + ", isCredentialsNonExpired()=" + isCredentialsNonExpired() + "]";
    }
}
