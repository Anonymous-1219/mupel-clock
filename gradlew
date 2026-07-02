#!/bin/sh

APP_HOME=$(cd "$(dirname "$0")" && pwd)
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$CLASSPATH" ]; then
    mkdir -p "$APP_HOME/gradle/wrapper"
    GRADLE_VERSION="8.1.1"
    GRADLE_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
    
    echo "Downloading Gradle..."
    cd /tmp
    wget -q "$GRADLE_URL" -O gradle.zip
    unzip -q gradle.zip
    cp "gradle-${GRADLE_VERSION}/lib/gradle-${GRADLE_VERSION}.jar" "$CLASSPATH"
    cd "$APP_HOME"
fi

java -cp "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
