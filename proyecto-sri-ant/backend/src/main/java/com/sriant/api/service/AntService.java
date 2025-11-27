package com.sriant.api.service;

import com.sriant.api.model.AntCache;
import com.sriant.api.repository.AntCacheRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class AntService {

    private final AntCacheRepository repository;
    private final RestTemplate restTemplate;
    // TTL for cache in seconds (example: 24 hours)
    private final Duration ttl = Duration.ofHours(24);

    public AntService(AntCacheRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public String obtenerPuntosLicencia(String tipoIdentificacion, String identificacion, String placa) {
        // Check cache first
        Optional<AntCache> maybe = repository.findById(identificacion);
        if (maybe.isPresent()) {
            AntCache cache = maybe.get();
            if (cache.getFetchedAt() != null && Instant.now().minus(ttl).isBefore(cache.getFetchedAt())) {
                return cache.getDatosJson(); // return cached value
            }
        }

        // If not cached or expired, call ANT site
        String url = String.format("https://consultaweb.ant.gob.ec/PortalWEB/paginas/clientes/clp_grid_citaciones.jsp?ps_tipo_identificacion=%s&ps_identificacion=%s&ps_placa=%s",
                tipoIdentificacion, identificacion, placa==null? "": placa);
        try {
            var res = restTemplate.getForEntity(url, String.class);
            String body = res.getBody() != null ? res.getBody() : "";

            // Save to cache (raw HTML/JSON depending on service)
            AntCache cache = new AntCache();
            cache.setIdentificacion(identificacion);
            cache.setDatosJson(body);
            cache.setFetchedAt(Instant.now());
            repository.save(cache);

            return body;
        } catch (Exception ex) {
            // If ANT is down and we had an expired cache return it anyway (best-effort)
            if (maybe.isPresent()) return maybe.get().getDatosJson();
            return "{"error":"ANT unavailable"}";
        }
    }
}
