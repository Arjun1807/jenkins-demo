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
		        powershell '''
		        $jar = Get-ChildItem target\\*.jar | Select-Object -First 1
		
		        if ($null -eq $jar) {
		            Write-Error "No JAR file found."
		            exit 1
		        }
		
		        Write-Host "Starting $($jar.Name)..."
		
		        Start-Process java -ArgumentList "-jar `"$($jar.FullName)`"" -WindowStyle Hidden
		
		        Start-Sleep -Seconds 10
		
		        Write-Host "Application Started Successfully."
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