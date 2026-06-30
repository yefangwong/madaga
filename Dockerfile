FROM eclipse-temurin:17-jre-jammy

ENV APP_HOME=/app
WORKDIR $APP_HOME

EXPOSE 8080

# Copy the compiled war file from the flat build context
COPY csp-portal-web-*.war app.war

# Copy the SQLite database to the expected directory structure inside the container
COPY dhf.sqlite /app/csp/csp-domain-jpa/src/main/resources/database/dhf/dhf.sqlite

# Run Spring Boot application
ENTRYPOINT ["java", "-Dmadaga.home=/app", "-jar", "app.war"]
