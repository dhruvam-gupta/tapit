# ═══════════════════════════════════════════════════════════════
#  STAGE 1 — "sdk"
#  Contains only JDK 17 + Android SDK.
#  No project code is copied in.
#
#  Use this stage to build with a volume-mounted project:
#    docker build --platform linux/amd64 --target sdk -t tapit-sdk .
#    docker run --platform linux/amd64 --rm -v "$(pwd)":/project -w /project tapit-sdk \
#        ./gradlew clean assembleDebug --no-daemon
# ═══════════════════════════════════════════════════════════════
FROM --platform=linux/amd64 eclipse-temurin:17-jdk-jammy AS sdk

# ---------- Android SDK env ----------
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH="${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools"

# SDK version vars — keep in sync with app/build.gradle
ENV COMPILE_SDK=35
ENV BUILD_TOOLS_VERSION=35.0.0

# System deps
RUN apt-get update && apt-get install -y --no-install-recommends \
        wget \
        unzip \
        curl \
        git \
    && rm -rf /var/lib/apt/lists/*

# Download & install Android command-line tools
RUN mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && wget -q "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip" \
         -O /tmp/cmdline-tools.zip \
    && unzip -q /tmp/cmdline-tools.zip -d /tmp/cmdline-tools \
    && mv /tmp/cmdline-tools/cmdline-tools "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm /tmp/cmdline-tools.zip

# Accept licences & install required SDK packages
RUN yes | sdkmanager --licenses > /dev/null 2>&1 \
    && sdkmanager \
        "platform-tools" \
        "platforms;android-${COMPILE_SDK}" \
        "build-tools;${BUILD_TOOLS_VERSION}"

# Default working directory (will be overridden by -w flag when using volumes)
WORKDIR /project

# ═══════════════════════════════════════════════════════════════
#  STAGE 2 — "build"
#  Extends the sdk stage by copying the project and building.
#  Use this for a fully self-contained one-shot build:
#    docker build --platform linux/amd64 -t tapit-builder .
#    docker create --name tmp tapit-builder
#    docker cp tmp:/project/app/build/outputs/apk/debug/Tap_and_Learn.apk .
#    docker rm tmp
# ═══════════════════════════════════════════════════════════════
FROM sdk AS build

# Copy the entire project into the image
COPY . .

# Ensure gradlew is executable
RUN chmod +x gradlew

# Build debug APK + release AAB
RUN ./gradlew clean assembleDebug bundleRelease --no-daemon --stacktrace

# Rename APK to a friendly name
RUN mv app/build/outputs/apk/debug/app-debug.apk \
       app/build/outputs/apk/debug/Tap_and_Learn.apk

# ─────────────────────────────────────────────
#  Outputs (extract with docker cp):
#    app/build/outputs/apk/debug/Tap_and_Learn.apk
#    app/build/outputs/bundle/release/app-release.aab
# ─────────────────────────────────────────────
