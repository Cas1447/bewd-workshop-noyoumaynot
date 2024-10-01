package han.aim.se.noyoumaynot.movie.controller;

import han.aim.se.noyoumaynot.movie.domain.Movie;
import han.aim.se.noyoumaynot.movie.domain.Role;
import han.aim.se.noyoumaynot.movie.domain.User;
import han.aim.se.noyoumaynot.movie.repository.UserToken;
import han.aim.se.noyoumaynot.movie.service.AuthenticationService;
import han.aim.se.noyoumaynot.movie.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.ArrayList;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;
    private final AuthenticationService authenticationService;

    private String user = "caz";
    private String password = "wachtwoord";
    public String token;

    @Autowired
    public MovieController(MovieService movieService, AuthenticationService authenticationService) {
        this.movieService = movieService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        UserToken userToken = authenticationService.login(user);
        if (userToken != null) {
            this.token = userToken.getToken();
            return ResponseEntity.ok("Login successful, token: " + token);
        }
        return ResponseEntity.status(401).body("Login failed");
    }


    @GetMapping
    public ArrayList<Movie> getAllMovies(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        authenticate(authorization);
        return movieService.getMovieList();
    }

    @GetMapping("/show")
    public Movie getMovieById(@RequestParam("id") String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        authenticate(authorization);
        return movieService.getMovieById(id);
    }

    @PostMapping("/add")
    public Movie addMovie(@RequestBody Movie movie, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        String username = authenticate(authorization);  
        Role userRole = authenticationService.getUserRole(username);

        if (userRole == null || !userRole.isAdmin()) {
            throw new SecurityException("User is not authorized to add movies.");
        }

        movieService.insertMovie(movie);
        return movie;
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable("id") String id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        authenticate(authorization);
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    private String authenticate(String token) throws Exception {
        if (authenticationService.isValidToken(token)){
            return authenticationService.getUsername(token);
        } else {
            throw new AuthenticationException("Invalid token");
        }
    }
}
