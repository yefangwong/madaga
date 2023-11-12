mvn clean verify sonar:sonar \
  -Dsonar.projectKey=madaga \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=f764eee5d32d2b6515e21ca2b4c928ef3d205671 \
  -Dmaven.test.skip=true
