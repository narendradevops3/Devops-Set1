import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CancelSpotInstanceRequestsRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.google.common.base.Preconditions;


public class Terminate {	
	
	public void cleanup() throws IOException{
		  
		// CONNECT TO EC2
			InputStream credentialsAsStream = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream("AwsCredentials.properties");
			Preconditions.checkNotNull(credentialsAsStream,
					"File 'AwsCredentials' NOT found in the classpath");
			AWSCredentials credentials = new PropertiesCredentials(
					credentialsAsStream);

			AmazonEC2 ec2 = new AmazonEC2Client(credentials);
			ec2.setEndpoint("https://us-east-1.ec2.amazonaws.com");
			List<String> spotInstanceRequestIds = null;
		try {
		    System.out.println("Cancelling requests.");
		    CancelSpotInstanceRequestsRequest cancelRequest=new CancelSpotInstanceRequestsRequest(spotInstanceRequestIds);
		    ec2.cancelSpotInstanceRequests(cancelRequest);
		  }
		 catch (  AmazonServiceException e) {
		    System.out.println("Error cancelling instances");
		    System.out.println("Caught Exception: " + e.getMessage());
		    System.out.println("Reponse Status Code: " + e.getStatusCode());
		    System.out.println("Error Code: " + e.getErrorCode());
		    System.out.println("Request ID: " + e.getRequestId());
		  }
		//please specify your instance id' based on launching code instance.getInstanceId()
		  List<String> instanceIds = Arrays.asList("i-0f0136df");
		try {
		    System.out.println("Terminate instances");
		    TerminateInstancesRequest terminateRequest=new TerminateInstancesRequest(instanceIds);
		    ec2.terminateInstances(terminateRequest);
		  }
		 catch (  AmazonServiceException e) {
		    System.out.println("Error terminating instances");
		    System.out.println("Caught Exception: " + e.getMessage());
		    System.out.println("Reponse Status Code: " + e.getStatusCode());
		    System.out.println("Error Code: " + e.getErrorCode());
		    System.out.println("Request ID: " + e.getRequestId());
		  }
		  instanceIds.clear();
		  spotInstanceRequestIds.clear();
		}	
	public static void main(String[] args) throws IOException {
		
		Terminate tr = new Terminate();
		tr.cleanup();
		
	}
	
}
