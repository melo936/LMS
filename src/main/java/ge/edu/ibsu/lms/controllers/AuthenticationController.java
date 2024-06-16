package ge.edu.ibsu.lms.controllers;

import ge.edu.ibsu.lms.dto.AddUser;
import ge.edu.ibsu.lms.dto.AuthenticationRequest;
import ge.edu.ibsu.lms.dto.AuthenticationResponse;
import ge.edu.ibsu.lms.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AddUser request
    ) throws Exception {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(value = "/signin", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}