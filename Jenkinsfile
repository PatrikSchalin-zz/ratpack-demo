pipeline {
    agent { 
        docker {
            image 'gradle:4.7-jdk8'
        }
    }

    stages {
        stage('Build') {
            steps {
                sh "gradle build --info"
            }
        }
    }
}