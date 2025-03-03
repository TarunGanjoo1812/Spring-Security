package com.security.feature.controller;

import com.security.feature.models.*;
import com.security.feature.repositories.UsersRepository;
import com.security.feature.services.MyUserDetailService;
import com.security.feature.services.RefreshTokenService;
import com.security.feature.services.UserServiceInterface;
import com.security.feature.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserServiceInterface service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UsersRepository repo;

    @Autowired
    private MyUserDetailService userDetailService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequestDTO user) throws Exception{
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        String token = "";
        if(authentication.isAuthenticated()) token = jwtUtil.getToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @GetMapping("/verify")
    public void verifyEmail(HttpServletResponse response, @RequestParam String token) throws IOException {

        // 1. get the user out from the token
        // 2. verify the token and check if its expired or not

        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        String res = "Email successfully verified. You can now log in!";
        response.setContentType("application/json");

        if(!jwtUtil.validateToken(token, userDetails)){
            res = "Verification token is expired or invalid.";

            response.getWriter().write("{\"Message\": \"" + res);
            return;
        }

        Users user = repo.findByUsername(username);
        if(user == null) throw new RuntimeException("User for email verification not found");
        user.setEmailVerified(true);
        repo.save(user);
        service.clearUserCache();

        response.getWriter().write("{\"Message\": \"" + res + "\"}");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request){
        String token = request.getRefreshToken();
        String userId = refreshTokenService.validateRefreshToken(token);
        Users user = repo.findById(Integer.valueOf(userId)).orElseThrow(()->new RuntimeException("Refresh-Token-Error: No Userid found"));
        String newJwtToken = jwtUtil.getToken(user.getUsername());
        return ResponseEntity.ok(new AccessTokenResponse(newJwtToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request){
        String token = request.getRefreshToken();
        refreshTokenService.deleteRefreshToken(token);
        return ResponseEntity.ok("Logged Out Successfully");
    }
}
