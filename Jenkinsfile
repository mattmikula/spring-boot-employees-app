pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Test Input') {
            steps {
                input "Test question?"
            }
        }
        stage('Build') {
            steps {
                sh './mvnw -B -DskipTests clean package'
            }
        }
    }
}
