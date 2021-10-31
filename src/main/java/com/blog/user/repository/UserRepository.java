package com.blog.user.repository;

import com.blog.user.database.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
  Optional<Users> findByUsername(String username);
}
