package ge.edu.ibsu.lms.services;

import ge.edu.ibsu.lms.dto.AddUser;
import ge.edu.ibsu.lms.dto.AuthenticationRequest;
import ge.edu.ibsu.lms.dto.AuthenticationResponse;
import ge.edu.ibsu.lms.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(AddUser request) throws Exception {
        try {
            userService.getByEmail(request.email());
            throw new Exception("User already exists");
        } catch (UsernameNotFoundException e) {
            User user = new User();
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            userService.save(user);
            var jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(jwtToken);
        }
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        var user = userService.getByEmail(request.email());
        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new UsernameNotFoundException("No user found");
    }
}