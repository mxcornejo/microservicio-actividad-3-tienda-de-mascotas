# Partimos de una imagen de Java 17 con Alpine (más ligera)
FROM openjdk:17-jdk-slim 
 
 
# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app
 
 
# Establecemos el directorio donde se montará la wallet de Oracle dentro del contenedor
ENV ORACLE_WALLET_DIR=/app/Wallet_BDFORO
 
 
# Crea un directorio en el contenedor para la wallet
RUN mkdir -p $ORACLE_WALLET_DIR
 
 
# Copia los archivos de la wallet (tnsnames.ora, sqlnet.ora, etc.) al contenedor
COPY Wallet_BDFORO/ $ORACLE_WALLET_DIR/

ENV TNS_ADMIN=/app/Wallet_BDFORO


# Copiamos el JAR generado en el contenedor
COPY target/tiendamascotas-0.0.1-SNAPSHOT.jar app.jar

# Copiar entrypoint y conceder permisos de ejecución
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

# Exponemos el puerto 8080 (el que usa Spring Boot por defecto)
EXPOSE 8080

# Usar entrypoint script para garantizar que TNS_ADMIN esté visible y registrado
ENTRYPOINT ["/app/docker-entrypoint.sh"]

# Heathcheck simple (opcional): intenta obtener el root path
HEALTHCHECK --interval=15s --timeout=3s --start-period=10s --retries=3 \
    CMD wget -qO- http://localhost:8080/ || exit 1