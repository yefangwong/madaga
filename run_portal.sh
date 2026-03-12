mvn clean install -DskipTests -DdependencyCheck.skip=true -Dproguard.skip=true || true
MADAGA_HOME=$(pwd)
cd csp/csp-portal-web
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dmadaga.home=$MADAGA_HOME" -DdependencyCheck.skip=true -Dproguard.skip=true
