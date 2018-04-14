#!/bin/bash


MICROSERVICE=(otus-ui service-aggregate service-discovery)
NANOSERVICE=(service-a service-b service-c service-d service-e service-f service-g service-h service-i)



for SERVICENAME in "${MICROSERVICE[@]}"
do

USERDATA=$"#!/bin/bash 
sudo yum install java-1.8.0 -y
sudo yum remove java-1.7.0-openjdk -y
cd /tmp  
touch user-data-working   
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/$SERVICENAME/$SERVICENAME-1.0-SNAPSHOT.jar 
nohup java -jar $SERVICENAME-1.0-SNAPSHOT.jar & "

AWSCOMMAND="aws ec2 run-instances --image-id  ami-25615740 --count 1 --instance-type t2.micro --key-name otus \
--security-group-ids sg-de978eb7 --subnet-id subnet-1e69b753 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=$SERVICENAME},{Key=Project,Value=otus}]' \
--user-data \"$USERDATA\""

#echo $SERVICENAME
#echo "$USERDATA"
echo "$AWSCOMMAND"

eval "$AWSCOMMAND"

done

for SERVICENAME in "${NANOSERVICE[@]}"
do

USERDATA=$"#!/bin/bash 
sudo yum install java-1.8.0 -y
sudo yum remove java-1.7.0-openjdk -y
cd /tmp  
touch user-data-working  
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/$SERVICENAME/$SERVICENAME-1.0-SNAPSHOT.jar   
nohup java -jar $SERVICENAME-1.0-SNAPSHOT.jar & "

AWSCOMMAND="aws ec2 run-instances --image-id  ami-25615740 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-de978eb7 --subnet-id subnet-1e69b753 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=$SERVICENAME},{Key=Project,Value=otus}]' \
--user-data \"$USERDATA\""

#echo $SERVICENAME
#echo "$USERDATA"
echo "$AWSCOMMAND"

eval "$AWSCOMMAND"

done

#aws ec2 associate-address --instance-id `aws ec2 describe-instances  --output text --filters "Name=tag:Name,Values=service-discovery" "Name=instance-state-name,Values=running" --query 'Reservations[].Instances[].InstanceId'` --public-ip 18.220.162.231


#aws ec2 associate-address --instance-id `aws ec2 describe-instances  --output text --filters "Name=tag:Name,Values=otus-ui" "Name=instance-state-name,Values=running" --query 'Reservations[].Instances[].InstanceId'` --public-ip 18.188.102.66


