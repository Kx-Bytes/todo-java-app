pipeline {
    agent any

    tools {
        maven 'maven3'
        jdk 'jdk17'
    }

    environment {
        IMAGE_NAME = 'kixan123/todo-java-app'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git 'https://github.com/Kx-Bytes/todo-java-app.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Run JUnit Tests') {
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
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME
                    '''
                }
            }
        }

        stage('Deploy with Ansible') {
            steps {
                script {
                    sh 'ansible-playbook -i ansible-deploy/inventory ansible-deploy/deploy.yml'
                }
            }
        }
        stage('Notify Monitoring') {
		    steps {
		        script {
		            sh '''
		                echo "todoapp.deployments 1 $(date +%s)" | nc localhost 2003 || true
		            '''
		        }
		     }
		 }
    }
}