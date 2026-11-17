
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    environment {
        DOCKER_HOST = 'npipe:////./pipe/dockerDesktopLinuxEngine'
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
                    bat '"C:\\Users\\safag\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t safagr/task-manager:1.0 .'
                }
            }
        }

        // Etape 5 : Publier l'image sur Docker Hub
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    bat '''
                        @echo off
                        echo %DOCKERHUB_TOKEN% | "C:\\Users\\safag\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" login -u "%DOCKERHUB_USER%" --password-stdin

                        "C:\\Users\\safag\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" push safagr/task-manager:1.0
                    '''
                }
            }
        }
    }
}
