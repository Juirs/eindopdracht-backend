package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
