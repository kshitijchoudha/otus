import boto3
import os
import sys
     
s3_client = boto3.client('s3')
     
     
def handler(event, context):
    for record in event['Records']:
        bucket = record['s3']['bucket']['name']
        key = record['s3']['object']['key'] 
        file_path = '/tmp/otusVpCFlowLog'
        
        s3_client.download_file(bucket, key, file_path)
       
        s3_client.upload_file(file_path, bucket, 'otusVpCFlowLog')