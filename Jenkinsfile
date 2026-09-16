pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -DskipTests clean package'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Integration Tests') {
            steps {
                sh 'mvn verify -DskipTests=false'
            }
        }

        stage('Coverage') {
            steps {
                sh 'mvn jacoco:report jacoco:check'
            }
        }

        stage('System Tests') {
            steps {
                sh 'mvn verify -Psystem-tests'
            }
        }

        stage('Archive Results') {
            steps {
                junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml,target/failsafe-reports/*.xml'
                archiveArtifacts allowEmptyArchive: true, artifacts: 'target/site/jacoco/**'
            }
        }
    }
}
