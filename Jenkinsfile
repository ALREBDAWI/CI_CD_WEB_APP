pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDS_ID = 'dockerhub_token'
        SONAR_TOKEN_ID      = 'sonar_token'
        TEAMS_WEBHOOK_ID    = 'teams_webhook'

        DOCKER_USER = 'reb'
        APP_NAME    = 'CI_CD_WEB_APP'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                echo 'Building using Maven...'
                sh './mvnw clean package'
            }
        }

        stage('Sonar Analysis') {
            steps {
                echo 'Starting SonarCloud Analysis...'
                withCredentials([string(credentialsId: "${SONAR_TOKEN_ID}", variable: 'SONAR_VAR')]) {
                    sh """
                    ./mvnw sonar:sonar \
                    -Dsonar.token=${SONAR_VAR} \
                    -Dsonar.host.url=https://sonarcloud.io \
                    -Dsonar.organization=alrebdawi \
                    -Dsonar.projectKey=ALREBDAWI_CI_CD_WEB_APP
                    """
                }
            }
}

        stage('Docker Build and Push') {
            steps {
                script {
                    echo 'Building and Pushing Docker Image...'
                    docker.withRegistry('', "${DOCKER_HUB_CREDS_ID}") {
                        def appImage = docker.build("${DOCKER_USER}/${APP_NAME}:${env.BUILD_NUMBER}")
                        appImage.push()
                        appImage.push('latest')
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying App Container...'
                sh "docker rm -f ${APP_NAME} || true"
                sh "docker run -d --name ${APP_NAME} -p 8080:8080 ${DOCKER_USER}/${APP_NAME}:latest"
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: "${TEAMS_WEBHOOK_ID}", variable: 'TEAMS_URL')]) {
                office365ConnectorSend(
                    message: "Built and deployed with success! Build number: ${env.BUILD_NUMBER}",
                    status: "Success",
                    webhookUrl: "${TEAMS_URL}"
                )
            }
        }
        failure {
            withCredentials([string(credentialsId: "${TEAMS_WEBHOOK_ID}", variable: 'TEAMS_URL')]) {
                office365ConnectorSend(
                    message: "Jenkins error while trying to build. Check the logs!",
                    status: "Failure",
                    webhookUrl: "${TEAMS_URL}"
                )
            }
        }
    }
}