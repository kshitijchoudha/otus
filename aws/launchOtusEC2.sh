#!/bin/bash


MICROSERVICE=(otus-ui service-aggregate service-discovery)
NANOSERVICE=(service-a service-b service-c service-d service-e service-f service-g service-h service-i)



for SERVICENAME in "${MICROSERVICE[@]}"
do

USERDATA=$"#!/bin/bash 
sudo yum install java-1.8.0
sudo yum remove java-1.7.0-openjdk
cd /tmp  
touch user-data-working   
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/$SERVICENAME/$SERVICENAME-1.0-SNAPSHOT.jar 
nohup java -jar $SERVICENAME-1.0-SNAPSHOT.jar & "

AWSCOMMAND="aws ec2 run-instances --image-id  ami-25615740 --count 1 --instance-type t2.micro --key-name otus \
--security-group-ids sg-de978eb7 --subnet-id subnet-1e69b753 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=$SERVICENAME},{Key=Project,Value=otus}]' \
--user-data \"$USERDATA\""

echo $SERVICENAME
echo "$USERDATA"
echo "$AWSCOMMAND"

eval "$AWSCOMMAND"

done

for SERVICENAME in "${NANOSERVICE[@]}"
do

USERDATA=$"#!/bin/bash 
cd /tmp  
touch user-data-working  
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/$SERVICENAME/$SERVICENAME-1.0-SNAPSHOT.jar   
nohup java -jar $SERVICENAME-1.0-SNAPSHOT.jar & "

AWSCOMMAND="aws ec2 run-instances --image-id  ami-25615740 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-de978eb7 --subnet-id subnet-1e69b753 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=$SERVICENAME},{Key=Project,Value=otus}]' \
--user-data \"$USERDATA\""

echo $SERVICENAME
echo "$USERDATA"
echo "$AWSCOMMAND"

eval "$AWSCOMMAND"

done
