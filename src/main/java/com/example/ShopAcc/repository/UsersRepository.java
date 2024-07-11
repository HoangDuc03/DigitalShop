package com.example.ShopAcc.repository;

import com.example.ShopAcc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> findByUserName(String userName);
}

