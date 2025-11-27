package com.sriant.api.service;

import com.sriant.api.model.Contribuyente;
import com.sriant.api.model.Vehiculo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SriService {

    private final RestTemplate restTemplate;

    public SriService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Verifica si existe por RUC (usa la API pública del enunciado)
    public boolean existePorRuc(String ruc) {
        String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/existePorNumeroRuc?numeroRuc=" + ruc;
        try {
            // Simple call; la respuesta real depende del SRI. Aquí devolvemos true si responde 200.
            var res = restTemplate.getForEntity(url, String.class);
            return res.getStatusCode().is2xxSuccessful();
        } catch (Exception ex) {
            return false;
        }
    }

    public Contribuyente obtenerContribuyente(String ruc) {
        String url = "https://srienlinea.sri.gob.ec/sri-catastro-sujeto-servicio-internet/rest/ConsolidadoContribuyente/obtenerPorNumerosRuc?&ruc=" + ruc;
        try {
            var res = restTemplate.getForEntity(url, String.class);
            // En un proyecto real aquí parsearíamos JSON y mapearíamos a Contribuyente.
            Contribuyente c = new Contribuyente();
            c.setRuc(ruc);
            c.setRazonSocial(res.getBody()!=null ? res.getBody().substring(0, Math.min(100, res.getBody().length())) : "INFO_RAW");
            c.setTipoPersona("NATURAL");
            return c;
        } catch (Exception ex) {
            return null;
        }
    }

    public Vehiculo obtenerVehiculoPorPlaca(String placa) {
        String url = "https://srienlinea.sri.gob.ec/sri-matriculacion-vehicular-recaudacion-servicio-internet/rest/BaseVehiculo/obtenerPorNumeroPlacaOPorNumeroCampvOPorNumeroCpn?numeroPlacaCampvCpn=" + placa;
        try {
            var res = restTemplate.getForEntity(url, String.class);
            Vehiculo v = new Vehiculo();
            v.setPlaca(placa);
            v.setMarca("DESCONOCIDA");
            v.setModelo("DESCONOCIDO");
            v.setColor("DESCONOCIDO");
            return v;
        } catch (Exception ex) {
            return null;
        }
    }
}
