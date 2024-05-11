pipeline {
    agent any
    
    environment {
        scannerHome = tool 'Sonar';
    }
    
    stages {

        stage('Code Analysis') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'sonar', variable: 'SONAR_LOGIN')]) {
                            sh """
                                ${scannerHome}/bin/sonar-scanner \
                                -Dsonar.projectKey=fatma \
                                -Dsonar.java.binaries=. \
                                -Dsonar.host.url=http://master:9000
                                -Dsonar.login=${SONAR_LOGIN}
                            """
                }
                }
            }
        }
        } 
    }

