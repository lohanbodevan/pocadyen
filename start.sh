#!/bin/bash
PROJECT_PATH=$(dirname "$0")

installGradle() {
    cd "$PROJECT_PATH"
    printf "\n Installing gradle"
    wget https://services.gradle.org/distributions/gradle-2.4-all.zip
    unzip gradle-2.4-all.zip
    rm gradle-2.4-all.zip
}

run() {
    cd "$PROJECT_PATH"
    gradle-2.4/bin/gradle bootRun
}

installGradle
run