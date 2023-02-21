pipeline {
  agent none
  stages {
    stage('stage1') {
      agent {
        docker {
          image 'registry.hand-china.com/library/sonar-scanner:4.6'
          args '-v /root/.m2:/root/.m2 -v /usr/bin/docker:/usr/bin/docker -v /var/run/docker.sock:/var/run/docker.sock -u 0'
        }

      }
      steps {
        sh 'export JAVA_HOME=/opt/java/openjdk8 && mvn clean package  -Dmaven.test.failure.ignore=true -DskipTests=true'
        sh 'export JAVA_HOME=/opt/java/openjdk && mvn sonar:sonar -Dsonar.host.url=${SONAR_URL} -Dsonar.login=${SONAR_LOGIN}  -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.qualitygate.wait=false'
      }
    }

  }
  environment {
    test = 'test'
  }
}