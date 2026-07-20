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
                git branch: 'main',
                    url: 'https://github.com/Arjun1807/jenkins-demo.git'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Verify Build') {
            steps {
                bat '''
                @echo off
                echo ===== Target Folder =====
                dir target
                '''
            }
        }

        stage('Stop Existing Application') {
            steps {
                bat '''
                @echo off

                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :9782') do (
                    echo Stopping process %%a
                    taskkill /PID %%a /F
                )

                exit /b 0
                '''
            }
        }

        stage('Deploy Application') {
            steps {
                bat '''
                @echo off
                echo Starting Spring Boot Application...

                for %%f in (target\\*.jar) do (
                    echo Deploying %%f
                    start "SpringBootApp" cmd /c java -jar "%%f"
                    goto :started
                )

                echo ERROR: No JAR file found in target folder.
                exit /b 1

                :started
                timeout /t 10

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

        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }
}