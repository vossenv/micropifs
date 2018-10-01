





pipeline {
    agent any
    environment {
        jar_file = 'microcam-1.0.jar'
    }
    stages {
        stage('Stage 1') {
            steps {
                echo 'Hello world!'
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
                dir("build/libs") {
                    stash includes: jar_file, name: 'micropicam'
                    archiveArtifacts artifacts: '**', fingerprint: true
                }
            }


        }
    }
    post {
          always {
            deleteDir() /* clean up our workspace */
        }
    }
}