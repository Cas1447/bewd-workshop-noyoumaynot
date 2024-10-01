package han.aim.se.noyoumaynot.movie.repository;

import han.aim.se.noyoumaynot.movie.domain.Role;

import java.util.UUID;

public class UserToken {
    private final int TOKENVALIDTIME = 60*10;
    private String token;
    private String username;
    private Role role;
    private long expiresIn;

    public UserToken(String username, Role role){
        this.username = username;
        this.expiresIn = TOKENVALIDTIME;
        this.token = UUID.randomUUID().toString();
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }
}
