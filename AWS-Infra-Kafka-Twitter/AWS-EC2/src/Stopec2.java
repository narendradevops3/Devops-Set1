import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.google.common.base.Preconditions;


public class Stopec2 {
	public void stopinstances(List<String> instanceIds,AmazonEC2 ec2Client) throws InterruptedException, IOException {
		// CONNECT TO EC2
				InputStream credentialsAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties");
				Preconditions.checkNotNull(credentialsAsStream,	"File 'AwsCredentials' NOT found in the classpath");
				AWSCredentials credentials = new PropertiesCredentials(credentialsAsStream);
				ec2Client = new AmazonEC2Client(credentials);
		 StopInstancesRequest stopRequest=new StopInstancesRequest(instanceIds);
		  ec2Client.stopInstances(stopRequest);
		  for (  String instanceId : instanceIds) {
		    waitForState(instanceId,"stopping",8,ec2Client);
		  }
		}
	private void waitForState(String instanceId, String string, int i,
			AmazonEC2 ec2Client) {
		// TODO Auto-generated method stub	
		
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		Stopec2 stopec2 = new Stopec2();
		List<String> instanceIds = Arrays.asList("i-67014d97");
		AmazonEC2 ec2Client = null;
		stopec2.stopinstances(instanceIds, ec2Client);
	}

}
