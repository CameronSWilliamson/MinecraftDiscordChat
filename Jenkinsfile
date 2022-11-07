pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew compile'
                sh './gradlew javadoc'
            }
        }
        stage('Execute') {
            steps {
                sh './scripts/run_server.sh'
                echo 'hello world'
            }
        }
    }
}