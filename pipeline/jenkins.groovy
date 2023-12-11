pipline {
    
    agent any
    
    environment {
        REPO = 'https://github.com/ikoshak/kbot'
        BRANCH = '${branch}'
    }
    
    parameters {
        choice(name: 'branch', choices: ['develop', 'main', 'qa'], description: 'Branch for Image build')
        choice(name: 'arch', choices: ['arm64', 'amd64', 'macOS'], description: 'Architecture for Image build')
        choice(name: 'os', choices: ['linux', 'windows', 'darwin'], description: 'OS for Image build')
    }
    
    stage("Checkout") {
            steps {
                echo 'CLONE REPOSITORY'
                  git branch: '${BRANCH}', url: '${REPO}'
            }
        }
        
    stage('test') {
            steps {
                echo 'TEST EXECUTION STARTED'
                sh 'make test'
            }
        }
        
    stage('build') {
            steps {
                echo 'BUILD EXECUTION STARTED'
                sh 'make build'
            }
        }
        
    stage('image') {
            steps {
                echo 'IMAGE EXECUTION STARTED'
                sh 'make image'
            }
        }
        
    
    stage('push') {
            steps {
                script {
                    docker.withRegistry( '681118b3-b0df-4023-9594-e63856ab1725', 'dockerhub' ) {
                    sh 'make push'
                }
            }
        }
    }
}