package com.tweetapp.controller;

import com.tweetapp.config.JwtTokenUtil;
import com.tweetapp.exception.UserAlreadyExists;
import com.tweetapp.model.JwtRequest;
import com.tweetapp.model.JwtResponse;
import com.tweetapp.model.TweetUser;
import com.tweetapp.service.TweetUserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/users")
@Slf4j
@SecurityRequirement(name = "TweetApp")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TweetUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody TweetUser tweetUser) throws UserAlreadyExists {
        return ResponseEntity.ok(userDetailsService.save(tweetUser));
    }

    @PostMapping("/{username}/forgot")
    public ResponseEntity<?> updateUserCredentials(@RequestBody JwtRequest jwtRequest) throws Exception {
        return ResponseEntity.ok(userDetailsService.update(jwtRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<?> allUsers() throws Exception {
        return ResponseEntity.ok(userDetailsService.allUsers());
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> allUsersByUsername(@PathVariable String username) throws Exception {
        return ResponseEntity.ok(userDetailsService.allUsersByUsername(username));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            log.info("Authenticating user : {}",username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
