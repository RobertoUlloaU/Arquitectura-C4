
Proyecto esqueleto para integrar SRI y ANT (skeleton)
Instrucciones rapidas:
 - Backend: carpeta backend (Maven + Spring Boot)
   - Ejecutar: mvn spring-boot:run (Java 17 requerido)
 - Frontend: carpeta frontend (React)
   - Ejecutar: npm install && npm start (Node 18+ recomendado)
Notas:
 - Las llamadas a SRI/ANT en este esqueleto usan RestTemplate, se debe implementar parsing y manejo específico.
 - ANT cache se implementa con una entidad JPA (AntCache) y un TTL simple (24h).
 - Debido a limitaciones del entorno, las llamadas a los servicios externos pueden fallar
