mvn clean install -DskipTests || true
MADAGA_HOME=$(pwd)
cd csp/csp-portal-web
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dmadaga.home=$MADAGA_HOME"
