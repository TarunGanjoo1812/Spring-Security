package com.security.feature.repositories;

import com.security.feature.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    public Users findByUsername(String username);
    public Users findByEmail(String email);
}
