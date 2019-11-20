import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.core.status.Status;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.google.common.base.Preconditions;

public class AmazonAwsInfrastructureMaker {

	public static void main(String[] args) throws Exception {
		AmazonAwsInfrastructureMaker infrastructureMaker = new AmazonAwsInfrastructureMaker();
		infrastructureMaker.createAll();

	}

	public void createAll() {

	}

	public AmazonAwsInfrastructureMaker() throws Exception {

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

		// CREATE EC2 INSTANCES
		Placement placement = new Placement("us-east-1c");
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
				.withInstanceType("t2.micro").withImageId("ami-00a11e68")
				.withMinCount(1).withMaxCount(1)
				.withSecurityGroups("Cloudera Manager").withKeyName("Dragon");
		// .withUserData(Base64.encodeBase64String(myUserData.getBytes()));

		RunInstancesResult runInstances = ec2.runInstances(runInstancesRequest);

		// TAG EC2 INSTANCES

		List<Instance> instances = runInstances.getReservation().getInstances();
		int idx = 1;
		for (Instance instance : instances) {
			CreateTagsRequest createTagsRequest = new CreateTagsRequest();
			createTagsRequest.withResources(instance.getInstanceId()) //
					.withTags(
							new Tag("Name",
									"Dragonfly-auto--provisioning with chef-client"
											+ idx));
			ec2.createTags(createTagsRequest);
			System.out.println(instance.getInstanceId());

			idx++;	
			
			
			//return instance state running, pending or terminate based on instance id'
			DescribeInstancesRequest describeInstanceRequest = new DescribeInstancesRequest().withInstanceIds(instance.getInstanceId());
		    DescribeInstancesResult describeInstanceResult = ec2.describeInstances(describeInstanceRequest);
		    InstanceState state = describeInstanceResult.getReservations().get(0).getInstances().get(0).getState();
		    System.out.println("current isntance state is......" + state);
		    return;
		    
		     	   	
		    	 

		}
	}

	

}