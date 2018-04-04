
aws --output table  ec2 describe-instances --filters "Name=instance-state-name,Values=running" --query 'Reservations[].Instances[].[Tags[?Key==`Name`] | [0].Value,PublicIpAddress,InstanceType]'

aws ec2 describe-instances  --output text --filters "Name=tag:Name,Values=service-discovery" "Name=instance-state-name,Values=running" --query 'Reservations[].Instances[].InstanceId'


aws ec2 associate-address --instance-id `aws ec2 describe-instances  --output text --filters "Name=tag:Name,Values=service-discovery" "Name=instance-state-name,Values=running" --query 'Reservations[].Instances[].InstanceId'` --public-ip 18.220.162.231



==============================
aws ec2 run-instances --image-id  ami-ea87a78f --count 1 --instance-type t2.micro --key-name otus --security-group-ids sg-de978eb7 --subnet-id subnet-54b3fd3d --user-data file://home/kchoudha/work/otus/aws-bootstrap.sh

aws ec2 terminate-instances --instance-ids 

aws ec2 describe-instance-attribute --instance-id i-051b3528f7c8084df --attribute userData --output text --query "UserData.Value" | base64 --decode

for ((i=1;i<=100;i++)); do   curl -v --header "Connection: keep-alive" "http://18.220.6.97:8080/";sleep 5; done

=======================

aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.micro --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=name,Value=otus-ui},{Key=project,Value=otus}]' \
--user-data file:///home/kchoudha/work/otus/aws-bootstrap.sh

aws ec2 terminate-instances --instance-ids 

aws ec2 describe-instance-attribute --instance-id i-051b3528f7c8084df --attribute userData --output text --query "UserData.Value" | base64 --decode

for ((i=1;i<=100;i++)); do   curl -v --header "Connection: keep-alive" "http://107.23.83.235:8080/";sleep 5; done


## otus-ui
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.micro --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=otus-ui},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/otus-ui/otus-ui-1.0-SNAPSHOT.jar 
nohup java -jar otus-ui-1.0-SNAPSHOT.jar &'

##service-discovery
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.micro --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-discovery},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-discovery/service-discovery-1.0-SNAPSHOT.jar 
nohup java -jar service-discovery-1.0-SNAPSHOT.jar &'

## service-aggregate
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.micro --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-aggregate},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-aggregate/service-aggregate-1.0-SNAPSHOT.jar 
nohup java -jar service-aggregate-1.0-SNAPSHOT.jar &'

## service-a
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-a},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-a/service-a-1.0-SNAPSHOT.jar 
nohup java -jar service-a-1.0-SNAPSHOT.jar &'

## service-b
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-b},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-b/service-b-1.0-SNAPSHOT.jar 
nohup java -jar service-b-1.0-SNAPSHOT.jar &'


## service-c
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-c},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-c/service-c-1.0-SNAPSHOT.jar 
nohup java -jar service-c-1.0-SNAPSHOT.jar &'


## service-d
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-d},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-d/service-d-1.0-SNAPSHOT.jar 
nohup java -jar service-d-1.0-SNAPSHOT.jar &'

## service-e
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-e},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-e/service-e-1.0-SNAPSHOT.jar 
nohup java -jar service-e-1.0-SNAPSHOT.jar &'

## service-f
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-f},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-f/service-f-1.0-SNAPSHOT.jar 
nohup java -jar service-f-1.0-SNAPSHOT.jar &'

## service-g
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-g},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-g/service-g-1.0-SNAPSHOT.jar 
nohup java -jar service-g-1.0-SNAPSHOT.jar &'

## service-h
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-h},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-h/service-h-1.0-SNAPSHOT.jar 
nohup java -jar service-h-1.0-SNAPSHOT.jar &'

## service-i
aws ec2 run-instances --image-id  ami-4fffc834 --count 1 --instance-type t2.nano --key-name otus \
--security-group-ids sg-5a65fa2b --subnet-id subnet-77f5e912 \
--tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=service-i},{Key=Project,Value=otus}]' \
--user-data '#!/bin/bash 
cd /tmp 
touch user-data-working 
wget https://s3.us-east-2.amazonaws.com/otus-201804/lib/service-i/service-i-1.0-SNAPSHOT.jar 
nohup java -jar service-i-1.0-SNAPSHOT.jar &'



