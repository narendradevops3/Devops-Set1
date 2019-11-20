import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.google.common.base.Preconditions;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class AmazonAwsInfrastructureMaker {

	public static void main(String[] args) throws Exception {
		AmazonAwsInfrastructureMaker infrastructureMaker = new AmazonAwsInfrastructureMaker();
		infrastructureMaker.createAll();

	}

	public void createAll() {

	}

	@SuppressWarnings("deprecation")
	public AmazonAwsInfrastructureMaker() throws IOException {

		String AMI_Tomcat = "ami-ed61b486";
		String AMI_MongoDB = "ami-09b76362";
		String InstanceType = "t2.micro";
		String SecurityGroup = "Cloudera Manager";
		String Keyname = "Dragon";

		// Console c = System.console();
		// if (c == null) {
		// System.err.println("No console.");
		// System.exit(1);
		// }

		// String InstanceType = c.readLine("Enter Instance Type to lanuch: ");
		// String SecurityGroup = c.readLine("Enter your security group: ");
		// String Keyname = c.readLine("Enter your Keyname: ");

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
		RunInstancesRequest runInstancesRequest1 = new RunInstancesRequest()
				.withInstanceType(InstanceType).withImageId(AMI_Tomcat)
				.withMinCount(1).withMaxCount(1)
				.withSecurityGroups(SecurityGroup).withKeyName(Keyname);
		RunInstancesRequest runInstancesRequest2 = new RunInstancesRequest()
				.withInstanceType(InstanceType).withImageId(AMI_MongoDB)
				.withMinCount(1).withMaxCount(1)
				.withSecurityGroups(SecurityGroup).withKeyName(Keyname);

		RunInstancesResult runInstances1 = ec2
				.runInstances(runInstancesRequest1);
		RunInstancesResult runInstances2 = ec2
				.runInstances(runInstancesRequest2);

		// TAG EC2 INSTANCES
		List<Instance> instances = runInstances1.getReservation()
				.getInstances();

		for (Instance instance : instances) {
			CreateTagsRequest createTagsRequest = new CreateTagsRequest();
			createTagsRequest.withResources(instance.getInstanceId()) //
					.withTags(
							new Tag("Name",
									"Dragonfly-auto--provisioning-Tomcat"));
			ec2.createTags(createTagsRequest);
			String TomcatIP;
			TomcatIP = instance.getPrivateIpAddress();
			System.out.println(TomcatIP);
			

			List<Instance> instances1 = runInstances2.getReservation()
					.getInstances();

			for (Instance instance1 : instances1) {
				CreateTagsRequest createTagsRequest1 = new CreateTagsRequest();
				createTagsRequest.withResources(instance.getInstanceId())
						//
						.withTags(
								new Tag("Name",
										"Dragonfly-auto--provisioning-MongoDB"));
				String MongoIP;
				MongoIP = instance1.getPrivateIpAddress();
				MongoIP = instance1.getPublicIpAddress();
				System.out.println(MongoIP);
				ec2.createTags(createTagsRequest);

			
				// TimeUnit.MINUTES.sleep(2);
				 //MongoClient mongoClient = new MongoClient(MongoIP, 27017);
				// // get database
				// // if database doesn't exists, mongodb will create it for
				// // you
				// BasicDBObject document = new BasicDBObject();
				// DB db = mongoClient.getDB("test12");
				// document.put("size", 12121212);
				// document.put("mongoip", MongoIP);
				// document.put("Tomcatip", TomcatIP);
				// db.createCollection("services", document);
				//
				//
				//
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				//
				// } catch (MongoException e) {
				// e.printStackTrace();
				// }

			}
		}

	}

}
