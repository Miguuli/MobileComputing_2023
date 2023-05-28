# syntax=docker/dockerfile:1
   
FROM ubuntu:focal-20230412

ENV DEBIAN_FRONTEND=noninteractive


RUN apt-get -qqy update && \
	apt dist-upgrade -y && \
	apt-get -qqy install --no-install-recommends \
    ca-certificates \
#   curl \
#    gnupg \
#    libgconf-2-4 \
    openjdk-17-jre-headless \	
    sudo \
#    tzdata \
    unzip \
    wget \
#    xvfb \
    zip 
#  && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64" \
    PATH=$PATH:$JAVA_HOME/bin

#===============
# Create a user
#===============
ARG USER_PASS=secret
RUN groupadd androidusr \
         --gid 1301 \
  && useradd androidusr \
         --uid 1300 \
         --gid 1301 \
         --create-home \
         --shell /bin/bash \
  && usermod -aG sudo androidusr \
  && echo androidusr:${USER_PASS} | chpasswd \
  && echo 'androidusr ALL=(ALL) NOPASSWD: ALL' >> /etc/sudoers

WORKDIR /home/androidusr

#=====================
# Install Android SDK
#=====================
ENV SDK_VERSION=commandlinetools-linux-8512546_latest
ENV ANDROID_BUILD_TOOLS_VERSION=33.0.0
ENV ANDROID_FOLDER_NAME=cmdline-tools
ENV ANDROID_DOWNLOAD_PATH=/home/androidusr/${ANDROID_FOLDER_NAME} \
    ANDROID_HOME=/opt/android \
    ANDROID_TOOL_HOME=/opt/android/${ANDROID_FOLDER_NAME}

RUN wget -O tools.zip https://dl.google.com/android/repository/${SDK_VERSION}.zip && \
    unzip tools.zip && rm tools.zip && \
    chmod a+x -R ${ANDROID_DOWNLOAD_PATH} && \
    chown -R 1300:1301 ${ANDROID_DOWNLOAD_PATH} && \
    mkdir -p ${ANDROID_TOOL_HOME} && \
    mv ${ANDROID_DOWNLOAD_PATH} ${ANDROID_TOOL_HOME}/tools
ENV PATH=$PATH:${ANDROID_TOOL_HOME}/tools:${ANDROID_TOOL_HOME}/tools/bin

# https://askubuntu.com/questions/885658/android-sdk-repositories-cfg-could-not-be-loaded
RUN mkdir -p ~/.android && \
    touch ~/.android/repositories.cfg && \
    echo y | sdkmanager "platform-tools" && \
    echo y | sdkmanager "build-tools;$ANDROID_BUILD_TOOLS_VERSION" && \
    mv ~/.android .android && \
    chown -R 1300:1301 .android
ENV PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools