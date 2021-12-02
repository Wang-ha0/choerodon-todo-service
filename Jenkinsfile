pipeline {
  agent any
  stages {
    stage('stage1') {
      parallel {
        stage('stage1') {
          steps {
            echo 'hello'
            echo 'hello2'
          }
        }

        stage('stage2') {
          steps {
            echo 'hello3'
          }
        }

      }
    }

    stage('stage3') {
      steps {
        echo 'stage3'
      }
    }

  }
  environment {
    test = 'test'
  }
}