#!/bin/bash


# Path to local JAR file
LOCAL_JAR=build/libs/tbiops-0.0.1-SNAPSHOT.jar

# Remote server info
REMOTE_USER=root
REMOTE_HOST=10.60.1.206
REMOTE_PATH=~/

# Upload JAR file
scp $LOCAL_JAR $REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH

# SSH to remote server
ssh $REMOTE_USER@$REMOTE_HOST << EOF

# Kill any running process for this JAR
kill -9 $(ps -ef | pgrep -f "tbiops-0.0.1-SNAPSHOT.jar")

# Start the new JAR in the background
nohup java -jar tbiops-0.0.1-SNAPSHOT.jar >java.log &

EOF



