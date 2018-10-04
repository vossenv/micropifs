





pipeline {
    agent any
    environment {
        jar_file = 'micropifs-1.0.jar'
    }
    stages {
        stage('Stage 1') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble'
                dir("build/libs") {
                    stash includes: jar_file, name: 'micropifs'
                    archiveArtifacts artifacts: '**', fingerprint: true
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'master') {
                        sh 'chmod +x deploy-artifacts.sh'
                        sh './deploy-artifacts.sh'
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
