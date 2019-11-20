import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.InstanceStatusSummary;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;


public class TestEC2DescribeInstanceStatus {


  public static void main( String[] args ) throws Exception {
    final TestEC2DescribeInstanceStatus test =  new TestEC2DescribeInstanceStatus();
    test.test();
  }

  private AWSCredentials credentials() {
    return new BasicAWSCredentials(
        "AKIAI5LJBI2E7CGKZAOA",
        "Nk4Ru/knRuBPpYR+WSaOQtfFMcKFWfCFqNltaSue"
    );
  }

  private AmazonEC2 getEc2Client( ) {
	  final AmazonEC2 ec2 = new AmazonEC2Client( credentials());
	  ec2.setEndpoint("https://us-east-1.ec2.amazonaws.com");    
    return ec2;
  }

  private String instanceType() {
    return "m1.small";
  }

  private void assertThat( boolean condition,
                           String message ){
    assert condition : message;
  }

  private void print( String text ) {
    System.out.println( text );
  }

  private void test() throws Exception{
    final AmazonEC2 ec2 = getEc2Client();

    // Find an appropriate image to launch
    final DescribeImagesResult imagesResult = ec2.describeImages( new DescribeImagesRequest().withFilters(
        new Filter().withName( "image-type" ).withValues( "machine" ),
        new Filter().withName( "root-device-type" ).withValues( "instance-store" )
    ) );

    assertThat( imagesResult.getImages().size() > 0, "Image not found" );

    final String imageId = imagesResult.getImages().get( 0 ).getImageId();
    print( "Using image: " + imageId );

    // End discovery, start test
    final List<Runnable> cleanupTasks = new ArrayList<Runnable>();
    try {
      // Create launch configuration
      print( "Running instance" );
      final RunInstancesResult runResult =
          ec2.runInstances( new RunInstancesRequest().withImageId( "ami-4ef8e526" ).withMinCount(1).withMaxCount(1).withSecurityGroups("Cloudera Manager").withKeyName("Dragon") );
      final String instanceId = getInstancesIds( runResult.getReservation() ).get(0);
      print( "Launched instance: " + instanceId );
      cleanupTasks.add( new Runnable() {
        @Override
        public void run() {
          print( "Terminating instance: " + instanceId );
          ec2.terminateInstances( new TerminateInstancesRequest().withInstanceIds( instanceId ) );
        }
      } );

      // Wait for instance
      final long timeout = TimeUnit.MINUTES.toMillis( 1 );
      waitForInstance( ec2, timeout, instanceId, "pending" );
      final String az = waitForInstance( ec2, timeout, instanceId, "running" );

      // Verify response format
      final DescribeInstanceStatusResult instanceStatusResult =
          ec2.describeInstanceStatus( new DescribeInstanceStatusRequest()
              .withInstanceIds( instanceId ) );
      assertThat( instanceStatusResult.getInstanceStatuses().size() == 1, "Instance not found" );
      final InstanceStatus status = instanceStatusResult.getInstanceStatuses().get( 0 );
      assertThat( status != null, "Null instance status" );
      assertThat( status.getAvailabilityZone() != null, "Missing availability zone" );
      assertThat( instanceId.equals(status.getInstanceId()), "Unexpected instance id : " + status.getInstanceId() );
      assertThat( status.getInstanceState()!=null , "Missing instance state" );
      assertThat( status.getInstanceState().getCode()==16 , "Unexpected instance state code : " + status.getInstanceState().getCode() );
      assertThat( "running".equals(status.getInstanceState().getName()) , "Unexpected instance state name : " + status.getInstanceState().getName() );
      assertStatusSummary( status.getInstanceStatus(), "instance" );
      assertStatusSummary( status.getSystemStatus(), "system" );

      // Test selection with filters
      String[][] filterTestValues = {
          { "availability-zone",            az,         "invalid-zone-name" },
          { "instance-state-name",          "running",  "pending" },
          { "instance-state-code",          "16",       "0" },
          { "system-status.status",         "ok",       "impaired" },
          { "system-status.reachability",   "passed",   "failed" },
          { "instance-status.status",       "ok",       "impaired" },
          { "instance-status.reachability", "passed",   "failed" },
      };
      for ( final String[] values : filterTestValues ) {
        final String filterName = values[0];
        final String filterGoodValue = values[1];
        final String filterBadValue = values[2];

        print( "Testing filter - " + filterName );
        assertThat( describeInstanceStatus( ec2, instanceId, filterName, filterGoodValue, 1 ), "Expected result for " + filterName + "=" + filterGoodValue );
        assertThat( describeInstanceStatus( ec2, instanceId, filterName, filterBadValue, 0 ), "Expected no results for " + filterName + "=" + filterBadValue );
      }

      print( "Test complete" );
    } finally {
      // Attempt to clean up anything we created
      Collections.reverse( cleanupTasks );
      for ( final Runnable cleanupTask : cleanupTasks ) {
        try {
          cleanupTask.run();
        } catch ( Exception e ) {
          e.printStackTrace();
        }
      }
    }
  }

  private void assertStatusSummary( final InstanceStatusSummary status,
                                    final String description ) {
    assertThat( status != null, "Missing " + description + " status");
    assertThat( Arrays.asList( "ok", "impaired", "initializing", "insufficient-data", "not-applicable" )
        .contains( status.getStatus() ), "Invalid status value: " + status.getStatus() );
    assertThat( status.getDetails() != null, "Missing status details" );
    assertThat( status.getDetails().size() == 1, "Unexpected details count: " + status.getDetails().size() );
    assertThat( "reachability".equals( status.getDetails().get(0).getName() ),
        "Unexpected details type: " + status.getDetails().get(0).getName() );
    assertThat( Arrays.asList( "passed", "failed", "initializing", "insufficient-data" )
        .contains( status.getDetails().get(0).getStatus()),
        "Invalid details value: " + status.getDetails().get(0).getStatus() );
  }

  private boolean describeInstanceStatus( final AmazonEC2 ec2,
                                          final String instanceId,
                                          final String filterName,
                                          final String filterValue,
                                          final int expectedCount ) {
    final DescribeInstanceStatusResult instanceStatusResult =
        ec2.describeInstanceStatus( new DescribeInstanceStatusRequest()
            .withInstanceIds( instanceId )
            .withFilters( new Filter()
                .withName( filterName )
                .withValues( filterValue ) ));
    return instanceStatusResult.getInstanceStatuses().size() == expectedCount;
  }

  private String waitForInstance( final AmazonEC2 ec2,
                                  final long timeout,
                                  final String expectedId,
                                  final String state ) throws Exception {
    print( "Waiting for instance state " + state );
    String az = null;
    final long startTime = System.currentTimeMillis( );
    boolean completed = false;
    while ( !completed && ( System.currentTimeMillis() - startTime ) < timeout ) {
      final DescribeInstanceStatusResult instanceStatusResult =
          ec2.describeInstanceStatus( new DescribeInstanceStatusRequest()
              .withInstanceIds( expectedId )
              .withIncludeAllInstances( true )
              .withFilters( new Filter()
                  .withName( "instance-state-name" )
                  .withValues( state ) ) );
      completed = instanceStatusResult.getInstanceStatuses().size() == 1;
      if ( completed ) {
        az = instanceStatusResult.getInstanceStatuses().get(0).getAvailabilityZone();
        assertThat( expectedId.equals( instanceStatusResult.getInstanceStatuses().get(0).getInstanceId() ), "Incorrect instance id" );
        assertThat( state.equals( instanceStatusResult.getInstanceStatuses().get( 0 ).getInstanceState().getName() ), "Incorrect instance state" );
      }
      Thread.sleep( 5000 );
    }
    assertThat( completed, "Instance not reported within the expected timeout" );
    print( "Instance reported "+state+" in " + ( System.currentTimeMillis()-startTime ) + "ms" );
    return az;
  }

  private List<String> getInstancesIds( final Reservation... reservations ) {
    final List<String> instances = new ArrayList<String>();
    for ( final Reservation reservation : reservations ) {
      for ( final Instance instance : reservation.getInstances() ) {
        instances.add( instance.getInstanceId() );
      }
    }
    return instances;
  }
}