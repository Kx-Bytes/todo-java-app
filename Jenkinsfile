pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds') // Jenkins credential ID
        IMAGE_NAME = 'kixan123/todo-java-app'
    }

    tools {
        maven 'Maven 3'
        jdk 'JDK 17'
    }

    stages {
        stage('Clone Code') {
            steps {
                git 'https://github.com/Kx-Bytes/todo-java-app.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withDockerRegistry([ credentialsId: "$DOCKERHUB_CREDENTIALS", url: "" ]) {
                    sh 'docker push $IMAGE_NAME'
                }
            }
        }
    }
}