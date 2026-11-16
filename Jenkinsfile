
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {

        // Etape 1 : Recuperer le code depuis GitHub
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/guerchisafa-star/devops-task-manager.git'
            }
        }

        // Etape 2 : Compiler le projet et executer les tests
        stage('Build et Tests') {
            steps {
                dir('task-manager') {
                    bat 'mvn clean test package'
                }
            }
        }

        // Etape 3 : Archiver le fichier JAR genere
        stage('Archive JAR') {
            steps {
                dir('task-manager') {
                    archiveArtifacts artifacts: 'target/*.jar',
                                     fingerprint: true
                }
            }
        }

        // Etape 4 : Construire l'image Docker
        stage('Docker Build') {
            steps {
                dir('task-manager') {
                    bat '"C:\\Users\\safag\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t task-manager:1.0 .'
                }
            }
        }
    }
}
