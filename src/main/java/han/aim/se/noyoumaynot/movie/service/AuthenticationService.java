package han.aim.se.noyoumaynot.movie.service;

import han.aim.se.noyoumaynot.movie.domain.Role;
import han.aim.se.noyoumaynot.movie.domain.User;
import han.aim.se.noyoumaynot.movie.repository.UserToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService {
  private ArrayList<UserToken> userTokens = new ArrayList<>();
  private ArrayList<User> users = new ArrayList<>();  // Lijst om gebruikers op te slaan
  private Role adminRole;
  private Role userRole;

  public AuthenticationService() {
    adminRole = new Role("Admin", true);
    userRole = new Role("User", false);

    users.add(new User("caz", "wachtwoord", adminRole));  // Beheerder
    users.add(new User("user", "wachtwoord", userRole));  // Eindgebruiker
  }

  public UserToken login(User user) {
    for (User registeredUser : users) {
      if (registeredUser.getUsername().equals(user.getUsername()) &&
              registeredUser.getPassword().equals(user.getPassword())) {
        UserToken userToken = new UserToken(registeredUser.getUsername(), registeredUser.getRole());
        userTokens.add(userToken);
        return userToken;
      }
    }
    return null;
  }

  public boolean isValidToken(String token) {
    for (UserToken userToken : userTokens) {
      if (userToken.getToken().equals(token)) {
        return true;
      }
    }
    return false;
  }

  public String getUsername(String token) {
    for (UserToken userToken : userTokens) {
      if (userToken.getToken().equals(token)) {
        return userToken.getUsername();
      }
    }
    return null;
  }

  public Role getUserRole(String username) {
    for (User registeredUser : users) {
      if (registeredUser.getUsername().equals(username)) {
        return registeredUser.getRole();
      }
    }
    return null;
  }

}
