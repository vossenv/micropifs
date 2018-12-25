





pipeline {
    agent any
    environment {
        jar_file = 'micropifs.jar'
    }
    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble'
                dir("build/libs") {
                    archiveArtifacts artifacts: '**', fingerprint: true
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh 'echo $PWD'
                    sh 'chmod +x deploy-artifacts.sh'
                    sh './deploy-artifacts.sh'
                }
            }
        }
    }
    post {
          always {
            deleteDir()
        }
    }
}
