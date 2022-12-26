package com.example.sns.repository;

import com.example.sns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Integer id);
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
}
