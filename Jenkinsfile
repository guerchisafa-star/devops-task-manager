pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/guerchisafa-star/devops-task-manager.git'
            }
        }

        stage('Build et Tests') {
            steps {
                dir('task-manager') {
                    bat 'mvn clean test package'
                }
            }
        }

        stage('Archive JAR') {
            steps {
                dir('task-manager') {
                    archiveArtifacts artifacts: 'target/*.jar',
                                     fingerprint: true
                }
            }
        }
    }
}