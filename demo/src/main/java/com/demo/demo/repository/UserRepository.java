package com.demo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.demo.models.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
