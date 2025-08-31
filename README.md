# Microservicio Actividad 3 Tienda de mascotas

Microservicio solicitado para tienda de mascotas, actividad fullstack 1 Duoc.

## Curls solicitados

### Debe listar tus ventas semilladas

curl -s http://localhost:8080/api/sales | jq

### Rango de fechas

curl -s "http://localhost:8080/api/sales/by-range?from=2025-08-25&to=2025-08-30" | jq

### Ganancia diaria

curl -s "http://localhost:8080/api/sales/profit/daily?date=2025-08-30" | jq

### Conteo por categoría (unidades)

curl -s "http://localhost:8080/api/sales/stats/category-count?from=2025-08-01&to=2025-08-30" | jq
