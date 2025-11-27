package com.sriant.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class AntCache {
    @Id
    private String identificacion; // cedula
    private String datosJson;
    private Instant fetchedAt;
}
