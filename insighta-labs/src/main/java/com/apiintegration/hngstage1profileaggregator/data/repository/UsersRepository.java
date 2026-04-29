package com.apiintegration.hngstage1profileaggregator.data.repository;

import com.apiintegration.hngstage1profileaggregator.data.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
    Optional<Users> findUsersByGithubId(Integer githubId);
}
