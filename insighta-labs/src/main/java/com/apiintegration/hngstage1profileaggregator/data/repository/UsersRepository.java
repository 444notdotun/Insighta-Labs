package com.apiintegration.hngstage1profileaggregator.data.repository;

import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
    Users findByUsername(String id);
}
