package com.security.feature.services;
import com.security.feature.models.Users;
import com.security.feature.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    private UsersRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users getUserByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    @Cacheable(value = "userCache")
    public List<Users> getAllUsers() {
        System.out.println("Fetching data from db");
        return repo.findAll();
    }

    @CacheEvict(value = "userCache", allEntries = true)
    public void clearUserCache(){
        System.out.println("Clearing users cache...");
    }

    @Override
    public Users createUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @Override
    public Boolean updateUserEmail(int id, String email) {
        Users user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User id not Found"));

        user.setEmail(email);
        repo.save(user);

        return true;
    }
}
