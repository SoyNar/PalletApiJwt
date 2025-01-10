package com.example.SecuritySpring.Repository;

import com.example.SecuritySpring.Application.Model.Role;
import com.example.SecuritySpring.Application.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByRole(Role role);
    List<User> findAllByRole(Role role);
    Optional<User> findById(Long id);
}
