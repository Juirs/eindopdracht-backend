package com.example.eindopdrachtbackend.repositories;

import com.example.eindopdrachtbackend.models.Role;
import com.example.eindopdrachtbackend.models.RoleKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleKey> {
}
