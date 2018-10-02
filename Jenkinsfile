





pipeline {
    agent any
    environment {
        jar_file = 'microcam-1.0.jar'
    }
    stages {
        stage('Stage 1') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble'
                dir("build/libs") {
                    stash includes: jar_file, name: 'micropicam'
                    archiveArtifacts artifacts: '**', fingerprint: true
                }
            }


        }
        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'master') {
                        sh 'chmod +x install-microservice.sh'
                        sh './install-microservice.sh -h 192.168.50.80'
                    } else {
                        echo 'Not on master branch; Skipping Deploy.'
                    }
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