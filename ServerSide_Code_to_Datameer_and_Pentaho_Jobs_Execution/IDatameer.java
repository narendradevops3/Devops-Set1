public interface IDatameer 
{
	public static final String CURL_JOB_EXEC = "curl -u admin:admin -X POST http://54.174.19.63:7777/rest/job-execution?configuration=";
	public static final String CURL_JOB_STATUS = "curl -s -u admin:admin -X GET http://54.174.19.63:7777/rest/job-configuration/job-status/";
	
	public static final String CURL_IMP_JOB_DET = "curl -u admin:admin -X GET http://54.174.19.63:7777/rest/import-job";
	public static final String CURL_WB_JOB_DET = "curl -u admin:admin -X GET http://54.174.19.63:7777/rest/workbook";
	public static final String CURL_EXP_JOB_DET = "curl -u admin:admin -X GET http://54.174.19.63:7777/rest/export-job";
	
	public static final String PENTAHO_PLUGIN_PATH = "/home/ec2-user/data-integration/plugins";
}
