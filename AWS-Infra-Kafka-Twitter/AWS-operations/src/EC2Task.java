
import java.util.List;
import java.util.Set;

import net.utoolity.bamboo.plugins.aws.EC2;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.task.TaskType;
import com.atlassian.bamboo.variable.CustomVariableContextImpl;
import com.google.common.collect.ImmutableSet;

public class EC2Task extends CustomVariableContextImpl implements TaskType
{
    private static final int WAIT_FOR_TRANSITION_INTERVAL = 5000;
    // REVIEW: There should be a respective enum defined somewhere?!
    private static final Set<String> INSTANCE_STATE_IN_PROGRESS_SET = ImmutableSet.<String> builder()
            .add("pending", "shutting-down", "stopping").build();
    private static final Set<String> INSTANCE_STATE_COMPLETE_SET = ImmutableSet.<String> builder()
            .add("running", "terminated", "stopped").build();
    private static final Set<String> BAMBOO_SUCCESS_SET = ImmutableSet.<String> builder()
            .addAll(INSTANCE_STATE_COMPLETE_SET).build();
    private static final Set<String> BAMBOO_FAILED_SET = ImmutableSet.<String> builder()
            .addAll(INSTANCE_STATE_IN_PROGRESS_SET).build();
    private static final String CONTACT_SUPPORT = "Encountered internal plugin error - please contact support!";

    @NotNull
    @java.lang.Override
    public TaskResult execute(@NotNull final TaskContext taskContext) throws TaskException, AmazonServiceException,
            AmazonClientException
    {
        final BuildLogger buildLogger = taskContext.getBuildLogger();
        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.create(taskContext);
        final ConfigurationMap configurationMap = taskContext.getConfigurationMap();

        final String instanceTransition = configurationMap.get(EC2TaskConfigurator.INSTANCE_TRANSITION);
        final String instanceRegion = configurationMap.get(EC2TaskConfigurator.INSTANCE_REGION);
        final String instanceId = substituteString(configurationMap.get(EC2TaskConfigurator.INSTANCE_ID));
        final Boolean forceStop = configurationMap.getAsBoolean(EC2TaskConfigurator.FORCE_STOP);
        final AWSCredentials credentials = new BasicAWSCredentials(
                configurationMap.get(AWSTaskConfigurator.ACCESS_KEY),
                configurationMap.get(AWSTaskConfigurator.SECRET_KEY));
        final String endpoint = EC2.ENDPOINT_MAP.get(instanceRegion);

        try
        {
            AmazonEC2 ec2 = new AmazonEC2Client(credentials);
            buildLogger.addBuildLogEntry("Selecting endpoint " + endpoint + " (" + instanceRegion + ")");
            ec2.setEndpoint(endpoint);

            if (EC2TaskConfigurator.START_TRANSITION.equals(instanceTransition))
            {
                final String transitionResult = startInstance(instanceId, ec2, buildLogger);
                determineTaskResult(transitionResult, taskResultBuilder, buildLogger);
            }
            else if (EC2TaskConfigurator.STOP_TRANSITION.equals(instanceTransition))
            {
                final String transitionResult = stopInstance(instanceId, forceStop, ec2, buildLogger);
                determineTaskResult(transitionResult, taskResultBuilder, buildLogger);
            }
            else if (EC2TaskConfigurator.REBOOT_TRANSITION.equals(instanceTransition))
            {
                // REVIEW: is rebootInstance() really a non traceable and never failing operation like so or is there
                // an option to check for success by some indirect means regardless?
                rebootInstance(instanceId, ec2, buildLogger);
                taskResultBuilder.success();
            }
            else
            {
                buildLogger.addErrorLogEntry(CONTACT_SUPPORT);
                taskResultBuilder.failedWithError();
            }
        }
        catch (AmazonServiceException ase)
        {
            buildLogger.addErrorLogEntry("Instance request rejected by AWS!", ase);
            taskResultBuilder.failedWithError();

        }
        catch (AmazonClientException ace)
        {
            buildLogger.addErrorLogEntry("Failed to communicate with AWS!", ace);
            taskResultBuilder.failedWithError();
        }
        catch (Exception e)
        {
            buildLogger.addErrorLogEntry("Failed to fetch resource from AWS!", e);
            taskResultBuilder.failedWithError();
        }

        return taskResultBuilder.build();
    }

    /**
     * @param instanceId
     * @param templateBody
     * @param notificationARNs
     * @param creationTimeout
     * @param enableRollback
     * @param ec2
     * @param buildLogger
     * @return String
     * @throws AmazonServiceException
     * @throws AmazonClientException
     * @throws InterruptedException
     * @throws Exception
     */
    private String startInstance(final String instanceId, AmazonEC2 ec2, final BuildLogger buildLogger)
            throws AmazonServiceException, AmazonClientException, InterruptedException
    {
        // Stop the instance
        StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceId);
        StartInstancesResult startResult = ec2.startInstances(startRequest);
        List<InstanceStateChange> stateChangeList = startResult.getStartingInstances();
        buildLogger.addBuildLogEntry("Starting instance '" + instanceId + "':");

        // Wait for the instance to be stopped
        return waitForTransitionCompletion(stateChangeList, "running", ec2, instanceId, buildLogger);
    }

    /**
     * @param instanceId
     * @param doForce
     * @param ec2
     * @param buildLogger
     * @return String
     * @throws AmazonServiceException
     * @throws AmazonClientException
     * @throws InterruptedException
     * @throws Exception
     */
    private String stopInstance(final String instanceId, final Boolean forceStop, AmazonEC2 ec2,
            final BuildLogger buildLogger) throws AmazonServiceException, AmazonClientException, InterruptedException
    {
        // Stop the instance
        StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceId).withForce(forceStop);
        StopInstancesResult startResult = ec2.stopInstances(stopRequest);
        List<InstanceStateChange> stateChangeList = startResult.getStoppingInstances();
        buildLogger.addBuildLogEntry("Stopping instance '" + instanceId + "':");

        // Wait for the instance to be stopped
        return waitForTransitionCompletion(stateChangeList, "stopped", ec2, instanceId, buildLogger);
    }

    /**
     * @param instanceId
     * @param ec2
     * @param buildLogger
     * @return String
     * @throws AmazonServiceException
     * @throws AmazonClientException
     * @throws Exception
     */
    private void rebootInstance(final String instanceId, AmazonEC2 ec2, final BuildLogger buildLogger)
            throws AmazonServiceException, AmazonClientException
    {
        // Reboot the instance
        RebootInstancesRequest rebootRequest = new RebootInstancesRequest().withInstanceIds(instanceId);
        buildLogger.addBuildLogEntry("Rebooting instance '" + instanceId + "'");
        ec2.rebootInstances(rebootRequest);
    }

    /**
     * @param transitionResult
     * @param taskResultBuilder
     * @param buildLogger
     */
    private void determineTaskResult(final String transitionResult, final TaskResultBuilder taskResultBuilder,
            final BuildLogger buildLogger)
    {
        if (BAMBOO_SUCCESS_SET.contains(transitionResult))
        {
            taskResultBuilder.success();
        }
        else if (BAMBOO_FAILED_SET.contains(transitionResult))
        {
            taskResultBuilder.failed();
        }
        else
        {
            buildLogger.addErrorLogEntry(CONTACT_SUPPORT);
            taskResultBuilder.failedWithError();
        }
    }

    /**
     * Wait for a instance to complete transitioning (i.e. status not being in INSTANCE_STATE_IN_PROGRESS_SET or the
     * instance no longer existing).
     * 
     * @param stateChangeList
     * @param instancebuilder
     * @param instanceId
     * @param BuildLogger
     * @throws InterruptedException
     * @throws Exception
     */
    public final String waitForTransitionCompletion(List<InstanceStateChange> stateChangeList,
            final String desiredState, AmazonEC2 instancebuilder, String instanceId, BuildLogger buildLogger)
            throws InterruptedException
    {
        Boolean transitionCompleted = false;
        InstanceStateChange stateChange = stateChangeList.get(0);
        String previousState = stateChange.getPreviousState().getName();
        String currentState = stateChange.getCurrentState().getName();
        String transitionReason = "";

        while (!transitionCompleted)
        {
            try
            {
                Instance instance = EC2.describeInstance(instancebuilder, instanceId);
                currentState = instance.getState().getName();
                if (previousState.equals(currentState))
                {
                    buildLogger.addBuildLogEntry("... '" + instanceId + "' is still in state " + currentState + " ...");
                }
                else
                {
                    buildLogger.addBuildLogEntry("... '" + instanceId + "' entered state " + currentState + " ...");
                    transitionReason = instance.getStateTransitionReason();
                }
                previousState = currentState;

                if (currentState.equals(desiredState))
                {
                    transitionCompleted = true;
                }
            }
            catch (AmazonServiceException ase)
            {
                buildLogger.addErrorLogEntry("Failed to describe instance '" + instanceId + "'!", ase);
                throw ase;
            }

            // Sleep for WAIT_FOR_TRANSITION_INTERVAL seconds until transition has completed.
            if (!transitionCompleted)
            {
                Thread.sleep(WAIT_FOR_TRANSITION_INTERVAL);
            }
        }

        buildLogger.addBuildLogEntry("Transition of instance '" + instanceId + "' completed with state " + currentState
                + " (" + (StringUtils.isEmpty(transitionReason) ? "Unknown transition reason" : transitionReason)
                + ").");

        return currentState;
    }
}