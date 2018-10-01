





pipeline {
    agent any
    stages {
        stage('Stage 1') {
            steps {
                echo 'Hello world!'
            }
        }
    }
    post {
          always {
            deleteDir() /* clean up our workspace */
        }
    }
}