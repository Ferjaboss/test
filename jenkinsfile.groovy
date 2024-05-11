pipeline {
    agent any
    
    environment {
        scannerHome = tool 'Sonar';
    }
    
    stages {

        stage('Code Analysis') {
            steps {
                script {
                        sh ' ${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=tunartisan_client_front -Dsonar.java.binaries=. -Dsonar.host.url=http://master:9000 -Dsonar.login=squ_a918939eb419f31cbdd274d2d03830fa34d3ee8c'
                    }
                }
            }
        }
        

    }
}
