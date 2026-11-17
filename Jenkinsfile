
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
                    credentialsId: 'dockerhub-jenkins-new',
                    usernameVariable: 'DOCKERHUB_USER',
                    passwordVariable: 'DOCKERHUB_TOKEN'
                )]) {
                    bat '''
                        @echo off

                        powershell.exe -NoProfile -NonInteractive -ExecutionPolicy Bypass -Command "$docker = 'C:\\Users\\safag\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe'; $env:DOCKERHUB_TOKEN | & $docker login -u $env:DOCKERHUB_USER --password-stdin; if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }; & $docker push safagr/task-manager:1.0; exit $LASTEXITCODE"
                    '''
                }
            }
        }
    }
}
