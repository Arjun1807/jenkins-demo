pipeline {

    agent any

    tools {
        jdk 'JDK-25'
        maven 'Maven-3.9'
    }

    environment {
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')

        APP_PORT = '9782'
        APP_JAR = 'target\\new-project-devops-deploy-0.0.1-SNAPSHOT.jar'
        LOG_FILE = 'C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\jenkins-crud-pipeline\\app.log'
    }

    stages {

        stage('Checkout Source Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Arjun1807/jenkins-demo.git'
            }
        }


        stage('Stop Existing Application') {
            steps {
                bat '''
                @echo off

                echo Checking existing application on port %APP_PORT%...

                for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%APP_PORT%') do (
                    echo Stopping PID %%a
                    taskkill /PID %%a /F
                )

                exit /b 0
                '''
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

                if not exist "%APP_JAR%" (
                    echo ERROR: JAR file not found
                    exit /b 1
                )

                echo JAR found successfully
                '''
            }
        }


        stage('Deploy Application') {
            steps {
                bat '''
                @echo off

                echo Starting Spring Boot Application...

                if exist "%LOG_FILE%" (
                    del "%LOG_FILE%"
                )


                set JENKINS_NODE_COOKIE=dontKillMe


                start "SpringBootApp" /B cmd /c "java -jar %APP_JAR% > %LOG_FILE% 2>&1"


                echo Waiting for application startup...

                timeout /t 15 > nul


                echo ===== Application Logs =====

                type "%LOG_FILE%"


                echo =============================


                echo Checking application port...

                netstat -ano | findstr :%APP_PORT%


                echo Application Started Successfully.

                '''
            }
        }


        stage('Verify Application') {
            steps {
                bat '''
                @echo off

                echo ===== Running Java Processes =====

                tasklist | findstr java


                echo ===== Port Check =====

                netstat -ano | findstr :%APP_PORT%

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