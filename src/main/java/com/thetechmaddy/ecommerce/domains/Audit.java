package com.thetechmaddy.ecommerce.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Setter
@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Audit {

    @TimeZoneStorage(value = TimeZoneStorageType.NORMALIZE)
    @CreationTimestamp
    @Column(name = "created_at")
    @JsonIgnore
    private OffsetDateTime createdAt;

    @TimeZoneStorage(value = TimeZoneStorageType.NORMALIZE)
    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonIgnore
    private OffsetDateTime updatedAt;
}

