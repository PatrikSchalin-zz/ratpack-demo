#!/usr/bin/env groovy

pipeline {
    agent none
    stages {
        stage('Gradle Build') {
            agent {
                docker {
                    image 'gradle:4.10-jdk8-slim'
                }
            }
            steps {
                sh 'apt-get update && apt-get install -y git'
                sh 'echo $(git describe --first-parent --dirty --tags) > VERSION'
                sh 'echo Building version: $(cat VERSION)'
                sh 'gradle clean build --stacktrace'
            }
            post {
                success {
                    stash name: 'version', includes: 'VERSION'
                    stash name: 'distribution', includes: 'build/distributions/demo-app-shadow.zip'
                }
                cleanup {
                    deleteDir()
                }
            }
        }
        stage('Docker Build') {
            agent {
                label 'docker'
            }
            environment {
                ACR_LOGIN_SERVER = 'smjenkinsacr.azurecr.io'
                YAHTZEE_RESOURCE_GROUP = 'TA-SMARTSALES-RGP-DEV-001'
                APPLICATION_NAME = 'ratpack'
            }
            steps {
                sh 'docker info'
                unstash name: 'distribution'
                unstash name: 'version'
                withCredentials([azureServicePrincipal('azure_service_principal')]) {
                    sh 'docker login $ACR_LOGIN_SERVER -u $AZURE_CLIENT_ID -p $AZURE_CLIENT_SECRET'
                    sh 'docker build -t $ACR_LOGIN_SERVER/$APPLICATION_NAME:latest -t $ACR_LOGIN_SERVER/$APPLICATION_NAME:$(cat ./VERSION) .'
                    sh 'docker push $ACR_LOGIN_SERVER/$APPLICATION_NAME:latest'
                    sh 'docker push $ACR_LOGIN_SERVER/$APPLICATION_NAME:$(cat ./../VERSION)'
                }
            }
            post {
                cleanup {
                    deleteDir() /* clean up our workspace */
                }
            }
        }
        stage('Deploy') {
            agent {
                label 'docker'
            }
            environment {
                ACR_LOGIN_SERVER = 'smjenkinsacr.azurecr.io'
                YAHTZEE_RESOURCE_GROUP = 'TA-SMARTSALES-RGP-DEV-001'
                APPLICATION_NAME = 'ratpack'
            }
            steps {
                withCredentials([azureServicePrincipal('azure_service_principal')]) {
                    azureCLI commands: [[exportVariablesString: '', script: "az container create --resource-group ${YAHTZEE_RESOURCE_GROUP} --name smartsales-${APPLICATION_NAME} --image ${ACR_LOGIN_SERVER}/${APPLICATION_NAME}:latest --registry-username ${AZURE_CLIENT_ID} --registry-password ${AZURE_CLIENT_SECRET} --dns-name-label smartsales-${APPLICATION_NAME} --ports 8080"]], principalCredentialId: 'azure_service_principal'
                }
            }
            post {
                cleanup {
                    deleteDir() /* clean up our workspace */
                }
            }
        }
    }
}