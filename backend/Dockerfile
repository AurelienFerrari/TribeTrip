# Build context is the repo root (see render.yaml: dockerContext: .), because the backend
# is a subproject of the root Gradle build (rootProject "TribeTrip", see settings.gradle.kts)
# and needs the root wrapper, gradle/libs.versions.toml, and settings.gradle.kts to build.
#
# --configure-on-demand keeps this build from ever evaluating the :composeApp subproject
# (Android/Compose Multiplatform), which this image has no SDK for and doesn't need — :backend
# has no dependency on it, so Gradle skips configuring it entirely.

FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY backend/build.gradle.kts backend/build.gradle.kts
COPY composeApp/build.gradle.kts composeApp/build.gradle.kts

RUN chmod +x gradlew

COPY backend/src backend/src

RUN ./gradlew :backend:bootJar --configure-on-demand --no-daemon -x test

FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

COPY --from=build /workspace/backend/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
