package com.techexpo.springboot.amazon.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

public class AmazonS3ClientUtil {
	private static final String SUFFIX = "/";
	private static final String ACCESS_KEY = "AKIAJEAWQ7HS4664ZXSQ";
	private static final String SECRET_ACCESS_KEY = "swZJmeG53csbBvbkgInTSZYGCiNjWjcPN/KajYmW";
	private static String bucketName = "otus-example-bucket"; 
	private static String key        = "foo.txt";    
	
	
	public static void main(String ... args) throws IOException {
		AmazonS3ClientUtil util = new AmazonS3ClientUtil();
		//util.createBucket();
		util.readObjectFromS3();
		//util.deleteFolder();
	}
	
	
	private void createBucket() {
		AmazonS3 s3Client = getS3Client();
        s3Client.setRegion(Region.getRegion(Regions.US_WEST_1));

		// create bucket - name must be unique for all S3 users
		String bucket = "otus-example-bucket";				
		s3Client.createBucket(bucket);
	}
	
	private void readObjectFromS3() throws IOException {
		AmazonS3 s3Client = getS3Client();
		s3Client.setRegion(Region.getRegion(Regions.US_WEST_1));
		try {
			System.out.println("Downloading object from S3 bucket");
            S3Object s3object = s3Client.getObject(new GetObjectRequest(
            		bucketName, key));
            
            System.out.println("Content-Type: "  + 
            		s3object.getObjectMetadata().getContentType());
            
            displayTextInputStream(s3object.getObjectContent());
            
            // Get a range of bytes from an object.
 /*           GetObjectRequest rangeObjectRequest = new GetObjectRequest(
            		bucketName, key);
            rangeObjectRequest.setRange(0, 10);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            
            System.out.println("Printing bytes retrieved.");
            displayTextInputStream(objectPortion.getObjectContent());*/

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
	}
	
	/**
	 * This method first deletes all the files in given folder and than the
	 * folder itself
	 */
	private void deleteFolder() {
		AmazonS3 s3Client = getS3Client();
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
	
	private static void displayTextInputStream(InputStream input) throws IOException {
    	// Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }
	private AmazonS3 getS3Client() {
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for 
		// this example to work
		AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_ACCESS_KEY);
		
		// create a client connection based on credentials
		AmazonS3 s3client = new AmazonS3Client(credentials);      
		//AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
		return s3client;
		
	}
}
