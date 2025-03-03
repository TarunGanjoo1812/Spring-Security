package com.security.feature.services;

import com.security.feature.models.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserServiceInterface {
    public Users getUserByUsername(String username);
    public List<Users> getAllUsers();
    public Users createUser(Users user);
    public Boolean updateUserEmail(int id, String email);
    public void clearUserCache();
}
