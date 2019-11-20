import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.google.common.base.Preconditions;


public class Startec2 {
	public void startInstances(List<String> instanceIds,AmazonEC2 ec2Client) throws InterruptedException, IOException {
		// CONNECT TO EC2
				InputStream credentialsAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("AwsCredentials.properties");
				Preconditions.checkNotNull(credentialsAsStream,	"File 'AwsCredentials' NOT found in the classpath");
				AWSCredentials credentials = new PropertiesCredentials(credentialsAsStream);
				ec2Client = new AmazonEC2Client(credentials);
		 StartInstancesRequest startRequest=new StartInstancesRequest(instanceIds);
		  ec2Client.startInstances(startRequest);
		  for (  String instanceId : instanceIds) {
		    waitForState(instanceId,"running",8,ec2Client);
		  }
		}
	private void waitForState(String instanceId, String string, int i,
			AmazonEC2 ec2Client) {
		// TODO Auto-generated method stub	
		
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		Startec2 st = new Startec2();
		List<String> instanceIds = Arrays.asList("i-67014d97");
		AmazonEC2 ec2Client = null;
		st.startInstances(instanceIds, ec2Client);
	}

}
