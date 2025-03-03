package com.security.feature.services;

import com.security.feature.config.UserPrincipal;
import com.security.feature.models.Users;
import com.security.feature.repositories.UsersRepository;
import com.security.feature.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsersRepository repo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2user = super.loadUser(userRequest);
        String email = oauth2user.getAttribute("email");
        String name = oauth2user.getAttribute("name");

        Users user = repo.findByEmail(email);
        if(user == null){
            user = new Users();
            user.setEmail(email);
            user.setAuthProvider("Google");
            user.setRole("USER");
            user.setPassword("N/A");
            user.setUsername(name);
            repo.save(user);
        }
        else{
            if(!"Google".equals(user.getAuthProvider())){
                user.setAuthProvider("Google");
                repo.save(user);
            }
        }

        String jwtToken = jwtUtil.getToken(user.getUsername());
        long new_user_id = (long) user.getId();
        String refreshToken = refreshTokenService.createRefreshToken(new_user_id);

        Map<String, Object> attributes = new HashMap<>(oauth2user.getAttributes());
        attributes.put("jwtToken", jwtToken);
        attributes.put("refreshToken", refreshToken);

        if(!user.isEmailVerified()){
            String emailVerificationToken = jwtUtil.getToken(user.getUsername());
            emailService.sendVerificationEmail(user.getEmail(), emailVerificationToken);
        }

        return (OAuth2User) new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())),
                attributes
        );
    }

}
