package com.techexpo.springboot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.techexpo.springboot.model.VPCFlowLogResponse;

public class AmazonS3ClientUtil {
	private static final String SUFFIX = "/";
	private static String US_ACCESS_KEY = "";
	private static String SECRET_US_ACCESS_KEY = "";
	private static String bucketName = "otus-201708"; 
	private static String key        = "otusVpCFlowLog";    
	
	
//	public static void main(String ... args) throws IOException {
//		AmazonS3ClientUtil util = new AmazonS3ClientUtil();
//		//util.createBucket();
//		System.out.println("=========args: " + args[0]);
//		accessKey = args[0];
//		secretAccessKey = args[1];
//		util.readObjectFromS3(US_ACCESS_KEY, SECRET_US_ACCESS_KEY);
//		//util.deleteFolder();
//	}
	
	
	private void createBucket(String accessKey, String secretAccessKey) {
		AmazonS3 s3Client = getS3Client(accessKey, secretAccessKey);
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_2));

		// create bucket - name must be unique for all S3 users
		String bucket = "otus-example-bucket";				
		s3Client.createBucket(bucket);
	}
	
	
	
	public List<VPCFlowLogResponse> readObjectFromS3(String accessKey, String secretAccessKey) throws IOException {
		AmazonS3 s3Client = getS3Client(accessKey, secretAccessKey);
		s3Client.setRegion(Region.getRegion(Regions.US_EAST_2));
		try {
			System.out.println("Downloading object from S3 bucket");
            S3Object s3object = s3Client.getObject(new GetObjectRequest(
            		bucketName, key));
            
            System.out.println("Content-Type: "  + 
            		s3object.getObjectMetadata().getContentType());
            
            return displayTextInputStream(s3object.getObjectContent());

            

		} catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
		return null;
	}
	
	/**
	 * This method first deletes all the files in given folder and than the
	 * folder itself
	 */
	private void deleteFolder() {
		AmazonS3 s3Client = getS3Client(US_ACCESS_KEY, SECRET_US_ACCESS_KEY);
		s3Client.setRegion(Region.getRegion(Regions.US_WEST_1));
		ObjectListing object = s3Client.listObjects(bucketName);
		System.out.println(object.toString());
		List<S3ObjectSummary> files =  s3Client.listObjects(bucketName).getObjectSummaries();
		
		System.out.println("===============File size:" + files.size());
		
		for(S3ObjectSummary file: files) {
			System.out.println("File Key..." + file.getKey());
			s3Client.deleteObject(bucketName, file.getKey());
		}
	}
	
	private static List<VPCFlowLogResponse> displayTextInputStream(InputStream input) throws IOException {
		List<VPCFlowLogResponse> vpcLogList = new ArrayList<VPCFlowLogResponse>();
		VPCFlowLogResponse vpcLog = new VPCFlowLogResponse();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            vpcLog = lineBreak(line);
            vpcLogList.add(vpcLog);
//            System.out.println("    " + lineBreak(line));
        }
//        System.out.println();
        return vpcLogList;
    }
	
	private static VPCFlowLogResponse lineBreak(String line) {
		VPCFlowLogResponse resp = new VPCFlowLogResponse();
		String[] output = line.split(" ");
		resp.setVersion(output[0]);
		resp.setAccointId(output[1]);
		resp.setInterfaceId(output[2]);
		resp.setSourceAddress(output[3]);
		resp.setDestinationAddress(output[4]);
		resp.setSourcePort(output[5]);
		resp.setDestinationPort(output[6]);
		resp.setProtocol(output[7]);
		resp.setPackets(output[8]);
		resp.setBytes(output[9]);
		resp.setStartTime(output[10]);
		resp.setEndTime(output[11]);
		resp.setAction(output[12]);
		resp.setLogStatus(output[13]);
		System.out.println(resp.toString());
		return resp;
	}
	private AmazonS3 getS3Client(String accessKey, String secretAccessKey) {
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for 
		// this example to work
		
		System.out.println("=========args: " + accessKey);
		System.out.println("=========args: " + secretAccessKey);

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretAccessKey);
		
		// create a client connection based on credentials
		AmazonS3 s3client = new AmazonS3Client(credentials);      
		//AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		return s3client;
		
	}
}
