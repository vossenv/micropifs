





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
        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'master') {
                        sh 'scp build/libs/microcam-1.0.jar pi@192.165.50.80:/usr/springboot/micropifs/micropifs.jar'
                        sh 'ssh pi@192.165.50.80 sudo service micropifs restart'
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