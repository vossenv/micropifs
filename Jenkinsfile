pipeline {
	environment {

	}
    stages {
        stage('Build') {
            steps {
				echo 'Creating version stamp...'
				}
            }
        }
	post {
        always {
            deleteDir() /* clean up our workspace */
        }
    }
}