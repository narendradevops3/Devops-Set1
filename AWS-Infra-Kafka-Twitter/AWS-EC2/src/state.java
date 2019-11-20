import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.InstanceState;

public class state {
	public Integer getInstanceStatus(String instanceId) throws IOException {
		InputStream credentialsAsStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("AwsCredentials.properties");
		AWSCredentials credentials = new PropertiesCredentials(
				credentialsAsStream);
		AmazonEC2 ec2 = new AmazonEC2Client(credentials);
		DescribeInstancesRequest describeInstanceRequest = new DescribeInstancesRequest()
				.withInstanceIds("i-aa73a143");
		DescribeInstancesResult describeInstanceResult = ec2
				.describeInstances(describeInstanceRequest);
		InstanceState state = describeInstanceResult.getReservations().get(0)
				.getInstances().get(0).getState();
		System.out.println("current isntance state is......" + state);
		return state.getCode();

	}

	public static void main(String[] args) throws IOException {
		state test = new state();
		test.getInstanceStatus("i-aa73a143");
	}
}
