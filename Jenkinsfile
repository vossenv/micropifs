





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
                    sh 'scp -P 2302 build/libs/microcam-1.0.jar pi@192.165.50.80:/usr/springboot/micropifs/micropifs.jar'
                    sh 'ssh -p 2302 pi@192.165.50.80 sudo service micropifs restart'
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