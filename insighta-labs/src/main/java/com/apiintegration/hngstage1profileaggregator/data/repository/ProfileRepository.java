package com.apiintegration.hngstage1profileaggregator.data.repository;

import com.apiintegration.hngstage1profileaggregator.data.model.Profile;
import com.apiintegration.hngstage1profileaggregator.dtos.response.Summary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile,UUID>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByName(String name);

    Optional<Profile> getProfilesById(UUID id);
    boolean existsById(UUID id);


}
