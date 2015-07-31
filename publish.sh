
## mvn -DaltDeploymentRepository=tagged-artifactory-release::default::http://artifactory.tagged.com:8081/artifactory/libs-snapshot-local  -DskipTests=true clean package  deploy
 mvn -DaltDeploymentRepository=tagged-artifactory-release::default::http://artifactory.tagged.com:8081/artifactory/libs-snapshot-local  -DskipTests=true clean package  install
