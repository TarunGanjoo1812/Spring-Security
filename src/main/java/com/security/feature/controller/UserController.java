package com.security.feature.controller;

import com.security.feature.models.Users;
import com.security.feature.services.RateLimitService;
import com.security.feature.services.UserServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceInterface service;

    @Autowired
    private RateLimitService rateLimitService;

    @GetMapping("/user/{username}")
    public Users getUserByUsername(@PathVariable String username) {
        return service.getUserByUsername(username);
    }

    @PostMapping("/admin/add")
    public String addUser(@RequestBody Users user) {
        service.clearUserCache();
        Users users = service.createUser(user);
        return (users == null) ? "User Not added !!" : "User added successfully!";
    }

//    @GetMapping("/all")
//    public List<Users> getAllUsers(@RequestHeader(value = "X-Forwarded-For", required = false) String clientIp) {
//        String key = "rate_limit:" + (clientIp != null ? clientIp : "unknown");
//        if(!rateLimitService.isAllowed(key, 10, 60)){
//            throw new RuntimeException("Too many requests. Please try again after sometime");
//        }
//        return service.getAllUsers();
//    }


    @GetMapping("/all")
    public List<Users> getAllUsers(HttpServletRequest request) {
        String key = "rate_limit:" + request.getRemoteAddr();
        if(!rateLimitService.isAllowed(key, 10, 60)){
            throw new RuntimeException("Too many requests. Please try again after sometime");
        }
        return service.getAllUsers();
    }

    @PutMapping("/admin/update")
    public Boolean updateUserEmail(@RequestBody Users user){
        return service.updateUserEmail(user.getId(), user.getEmail());
    }
}
