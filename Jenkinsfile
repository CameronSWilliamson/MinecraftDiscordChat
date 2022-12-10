pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew shadowJar'
                sh './gradlew javadoc'
            }
        }
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                sh './scripts/setup_env.sh'
                echo 'hello world'
            }
        }
    }
}