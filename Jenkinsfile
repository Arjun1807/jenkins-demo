pipeline {
    agent any

    tools {
        jdk 'JDK-25'
        maven 'Maven-3.9'
    }

    environment {
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                git branch: 'main', url: 'https://github.com/Arjun1807/jenkins-demo.git'
            }
        }

        stage('Stop Existing Application') {
            steps {
                bat '''
                @echo off
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr /R /C:":9782 .*LISTENING"') do (
                    echo Stopping existing application PID %%a...
                    taskkill /PID %%a /F
                )
                exit /b 0
                '''
            }
        }

        stage('Compile') {
            steps {
                bat 'mvn clean compile'
            }
        }

        stage('Package Application') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }

        stage('Deploy Application') {
            steps {
                bat '''
                @echo off
                echo Starting Spring Boot Application...

                set JENKINS_NODE_COOKIE=dontKillMe

                start "SpringBootApp" /B cmd /c "java -jar target\\new-project-devops-deploy-0.0.1-SNAPSHOT.jar > app.log 2>&1"

                ping 127.0.0.1 -n 11 > nul

                echo Application Started Successfully.
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}