#!/bin/bash

# Path to local JAR file
scp -r build/libs/tbiops-0.0.1-SNAPSHOT.jar root@10.60.1.206:~/


ssh  root@10.60.1.206 "sh ~/start.sh"



