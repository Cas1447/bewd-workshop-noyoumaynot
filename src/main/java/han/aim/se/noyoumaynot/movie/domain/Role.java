package han.aim.se.noyoumaynot.movie.domain;

public class Role {
    private String name;
    private boolean isAdmin;

    public Role(String name, boolean isAdmin) {
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
