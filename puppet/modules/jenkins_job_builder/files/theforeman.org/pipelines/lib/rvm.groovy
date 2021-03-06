def gemset(name = null) {

    def base_name = "${JOB_NAME}-${BUILD_ID}"

    if (name) {
        base_name = base_name + '-' + name
    }

    base_name
}

def configureRVM(ruby = '2.0', name = '') {
    emptyGemset(ruby, name)
    withRVM(["gem install bundler -v '< 2.0'"], ruby, name)
}

def emptyGemset(ruby = '2.0', name = '') {
    withRVM(["rvm gemset empty ${gemset(name)} --force"], ruby, name)
}

def cleanupRVM(ruby = '2.0', name = '') {
    withRVM(["rvm gemset delete ${gemset(name)} --force"], ruby, name)
}

def withRVM(commands, ruby = '2.0', name = '') {

    commands = commands.join("\n")
    echo commands.toString()

    sh """#!/bin/bash -l
        set +e
        rvm use ruby-${ruby}@${gemset(name)} --create
        ${commands}
    """
}
