package com.apiintegration.hngstage1profileaggregator.data.model;

import com.fasterxml.uuid.Generators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {
    @Id
    private UUID id;

        private String name;

        private String gender;


        private float genderProbability;

        private Integer age;

        private String ageGroup;

        private String countryId;
        private String countryName;
        private float countryProbability;
        @CreationTimestamp
        private String createdAt;

    @PrePersist
    public void ensureId() {
        if (this.id == null) {
            this.id = Generators.timeBasedGenerator().generate();
        }
    }
}
