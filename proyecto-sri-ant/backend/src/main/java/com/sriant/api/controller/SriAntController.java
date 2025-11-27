package com.sriant.api.controller;

import com.sriant.api.model.Contribuyente;
import com.sriant.api.model.Vehiculo;
import com.sriant.api.service.AntService;
import com.sriant.api.service.SriService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class SriAntController {

    private final SriService sriService;
    private final AntService antService;

    public SriAntController(SriService sriService, AntService antService) {
        this.sriService = sriService;
        this.antService = antService;
    }

    @GetMapping("/existe-ruc/{ruc}")
    public ResponseEntity<?> existeRuc(@PathVariable String ruc) {
        boolean existe = sriService.existePorRuc(ruc);
        return ResponseEntity.ok(Map.of("existe", existe));
    }

    @GetMapping("/contribuyente/{ruc}")
    public ResponseEntity<?> obtenerContribuyente(@PathVariable String ruc) {
        Contribuyente c = sriService.obtenerContribuyente(ruc);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<?> obtenerVehiculo(@PathVariable String placa) {
        Vehiculo v = sriService.obtenerVehiculoPorPlaca(placa);
        if (v == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(v);
    }

    @GetMapping("/ant/puntos")
    public ResponseEntity<?> puntosAnt(@RequestParam String tipoIdentificacion, @RequestParam String identificacion, @RequestParam(required=false) String placa) {
        String resp = antService.obtenerPuntosLicencia(tipoIdentificacion, identificacion, placa);
        return ResponseEntity.ok(Map.of("raw", resp));
    }
}
