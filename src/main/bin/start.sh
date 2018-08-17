#/bin/bash

gunlocation_id=`ps -ef | grep gunLocationMqServer-0.0.1-SNAPSHOT.jar | grep -v "grep" | awk '{print $2}'`
echo $gunlocation_id

for id in $gunlocation_id
do
    kill -9 $id  
    echo "killed $id"  
done

nohup java -jar gunLocationMqServer-0.0.1-SNAPSHOT.jar >./log/first.log & 
