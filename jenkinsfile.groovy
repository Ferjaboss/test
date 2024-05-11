pipeline {
    agent any
    
    // Define tools used in the pipeline
    tools {
        // Define Maven tool with version 3.6.3
        maven 'Maven'
        nodejs 'NodeJS'
        jdk 'Java'
    }
    
    environment {
        scannerHome = tool 'Java'
    }
    
    stages {
               
        stage('Run Backend Unit Tests') {
            steps {
                dir('spring-blog-backend') {
                    sh 'mvn clean test'
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('spring-blog-backend') {
                    sh 'mvn clean install'
                }
            }
        }
        
        stage('Build Frontend') {
            steps {
                dir('spring-blog-client') {
                    sh 'npm install'
                    sh 'npm run build -- --configuration=production'
                }
            }
        }
        
        stage('Run Frontend Unit Tests') {
            steps {
                dir('spring-blog-client') {
                    sh 'npm run test'
                }
            }
        }
        
        stage('Code Analysis') {
            steps {
                dir('spring-blog-backend') {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.23:sonar -Dsonar.host.url=http://192.168.74.139:9010/'
                    }
                }
                dir('spring-blog-client') {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh "${env.scannerHome}/bin/java -jar ${env.scannerHome}/lib/sonar-scanner-cli-4.4.0.2170-linux/sonar-scanner-cli-4.4.0.2170-linux/sonar-scanner-4.4.0.2170-linux/bin/sonar-scanner -Dsonar.host.url=http://192.168.74.139:9010/"
                    }
                }
            }
        }
        
        stage('Deploy Artifacts to Nexus') {
            steps {
                dir('spring-blog-backend/target') {
                    //  Publish backend artifact to Nexus
                    sh 'curl -v -u admin:nexus --upload-file spring-blog-backend-0.0.1-SNAPSHOT.jar http://192.168.74.134:8081/repository/maven-releases/'
                }
                
                dir('spring-blog-client') {
                    // Publish frontend artifact to Nexus (assuming Angular produces static files)
                    sh 'curl -v -u admin:nexus --upload-file * http://192.168.74.134:8081/repository/npm-releases/'
                }
            }
        }
    }
}
