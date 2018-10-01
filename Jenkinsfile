





pipeline {
    agent any
    stages {
        stage('Stage 1') {
            steps {
                echo 'Hello world!'
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
        }
    }
    post {
          always {
            deleteDir() /* clean up our workspace */
        }
    }
}