build() {
    cd ~/IdeaProjects/crossworld/rest/
    gradle build
    cd ~/IdeaProjects/crossworld/scheduler/
    gradle build
    cd ~/IdeaProjects/crossworld/auth-service/
    gradle build
    cd ~/IdeaProjects/crossworld/core/
    gradle build
}

build
