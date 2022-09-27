pipeline {
  agent any
  stages {
    stage('stage1') {
      environment {
        va1 = 'va1'
      }
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
      environment {
        va2 = 'va2'
      }
      steps {
        echo 'stage3'
      }
    }

  }
  environment {
    test = 'test'
  }
}