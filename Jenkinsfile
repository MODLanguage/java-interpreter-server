def SHORT_COMMIT
def SCM_REPO

podTemplate(
    imagePullSecrets: ['dockerhub-imagepull'],
    containers: [
        containerTemplate(
            name: 'build-tools',
            image: 'numtechnology/build-tools:gradle-5.1.1-jdk8-docker',
            ttyEnabled: true,
            command: 'cat',
            resourceRequestCpu: '512m',
            resourceLimitCpu: '512m',
            resourceRequestMemory: '512M',
            resourceLimitMemory: '2G')
        ],
    volumes: [
        hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
    ]) {


    node(POD_LABEL) {

        SCM_REPO = checkout scm
        SHORT_COMMIT = "${SCM_REPO.GIT_COMMIT[0..6]}"

        try {
            stage('Test & Build') {
                container('build-tools') {
                    sh "./gradlew test"
                }
            }

            stage('Build Container') {
                container('build-tools') {
                    sh "./gradlew bootJarRename"
                    sh "docker build --network=host --build-arg JAR_FILE=build/libs/ -t modl-java-interpreter ."
                }
            }

            stage('Tag Latest') {
                container('build-tools') {
                   sh "docker tag modl-java-interpreter:latest numtechnology/modl-java-interpreter:${env.BRANCH_NAME}-${SHORT_COMMIT}"
                   sh "docker tag modl-java-interpreter:latest numtechnology/modl-java-interpreter:latest"
                }
            }

            stage('Push Container') {
                container('build-tools') {
                    script {
                        withDockerRegistry([credentialsId: "docker", url: ""]) {
                            sh "docker push numtechnology/modl-java-interpreter:${env.BRANCH_NAME}-${SHORT_COMMIT}"
                            if(env.BRANCH_NAME == 'master') {
                                sh "docker push numtechnology/modl-java-interpreter:latest"
                            }
                        }
                    }
                }
            }
        } catch (e) {
            slackSend (
                color: '#FF0000',
                message: '`java-interpreter-server` build has failed on branch `' + env.BRANCH_NAME + '`\n' + \
                         'more info on ' + env.BUILD_URL,
                channel: '#modl')
            throw e
        }
    }
}
