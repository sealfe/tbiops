#!/bin/bash
scp -r build/libs/tbiops-0.0.1-SNAPSHOT.jar root@10.60.1.206:~/
expect "password"
send "Tobizit@2023!\r"

send "kill -9 $(ps -ef | pgrep -f "java")"
send "nohup java -jar tbiops-0.0.1-SNAPSHOT.jar >java.log & "
send "exit\r"
echo "deploye success!"






