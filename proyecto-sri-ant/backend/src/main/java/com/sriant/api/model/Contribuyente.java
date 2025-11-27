package com.sriant.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Contribuyente {
    private String ruc;
    private String razonSocial;
    private String tipoPersona; // NATURAL | JURIDICA
    private String direccion;
}
