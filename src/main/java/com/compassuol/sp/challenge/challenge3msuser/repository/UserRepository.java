package com.compassuol.sp.challenge.challenge3msuser.repository;

import com.compassuol.sp.challenge.challenge3msuser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
    @Query("select u.role from User u where u.email like :username")
    User.Role findRoleByUsername(String username);

}