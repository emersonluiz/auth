package br.com.emersonluiz.auth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;

public class AuthenticationSession implements Serializable{

    private static final long serialVersionUID = 3052547482371634181L;
    private List<String> authorities = new ArrayList<String>();
    private String username;
    private UUID userid;

    public AuthenticationSession(UUID userid, String username) {
        this.userid =  userid;
        this.username = username;
    }

    public AuthenticationSession() {
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthenticationSession other = (AuthenticationSession) obj;

        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;

        if (authorities == null) {
            if (other.authorities != null)
                return false;
        } else if (!CollectionUtils.isEqualCollection(authorities, other.authorities))
            return false;

        return true;
    }

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }
}
