package com.user.management.repository;

import java.util.UUID;

import com.user.management.Models.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, UUID> {
}