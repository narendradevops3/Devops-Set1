import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;

public class DatafactoryService 
{
	JSONParser parser=new JSONParser();
	DatameerDFAPI dataAPI = null;
	Runtime run = Runtime.getRuntime();
	Process proc = null;
	
	String noaaId1 ="",noaaId2 ="",noaaId3 = "";
	String twitId1 ="",twitId2 ="",twitId3 = "";
	String pentFile = "";
		
	boolean noaaFlag,twitFlag,pentFlag;
	
	DatafactoryService()
	{	
		//Read JSON data		 
		try {			
			Object objFile = parser.parse(new FileReader(new File("Datafactory.json")));        
			JSONArray msg = (JSONArray) objFile;
					
			JSONObject objData1 = (JSONObject) msg.get(0);
			JSONObject objData2 = (JSONObject) msg.get(1);
			JSONObject objData3 = (JSONObject) msg.get(2);
			
			System.out.println(objData1);
			System.out.println(objData2);
			System.out.println(objData3);
			
			this.noaaFlag = (Boolean) objData1.get("NoaaFlag");
			this.noaaId1 = objData1.get("id1").toString();
			this.noaaId2 = objData1.get("id2").toString();
			this.noaaId3 = objData1.get("id3").toString();
	        
			this.twitFlag = (Boolean) objData2.get("TwitFlag");
			this.twitId1 = objData2.get("id1").toString();
			this.twitId2 = objData2.get("id2").toString();
			this.twitId3 = objData2.get("id3").toString();
			
			this.pentFlag = (Boolean) objData3.get("PentFlag");
			this.pentFile = objData3.get("file").toString();
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}   
	}
	
	private String executeDatameerJob(String jobId)
	{
		System.out.println("Inside executeDatameerJob:"+jobId);
		dataAPI = new DatameerDFAPI();
		String jStatus = "";
		
		try{				
			proc = run.exec(IDatameer.CURL_JOB_EXEC+jobId);
			System.out.println("Inside try after run:"+IDatameer.CURL_JOB_EXEC+jobId);
			Thread.sleep(1000);
			String result = "";			
			System.out.println("Inside try before status run:"+IDatameer.CURL_JOB_STATUS+jobId);	
			do{					
				proc = run.exec(IDatameer.CURL_JOB_STATUS+jobId);				
				result = dataAPI.jobDetails(proc);
				
				Object obj = parser.parse(result.toString());
				JSONObject jsonObj = (JSONObject) obj;
				jStatus = jsonObj.get("jobStatus").toString();
				System.out.println("Sudheer status:"+jStatus);
			} while(jStatus.equals("QUEUED") || jStatus.equals("RUNNING") || jStatus.equals("WAITING_FOR_OTHER_JOB"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return jStatus;
	}
	
	private void executePentahoJob(String file)
	{	
		System.out.println("Inside executePentahoJob:"+file);
		try {
			KettleEnvironment.init();
		
			JobMeta jobMeta = new JobMeta(pentFile, null);
			Job job = new Job(null, jobMeta);
			job.run();
			job.waitUntilFinished();
			if (job.getErrors()!=0) {
				System.out.println("Error encountered!");
			}
		} catch (KettleException e) {
			e.printStackTrace();
		}
	}
	
	public void performExecution()
	{
		String result ="";
		if(noaaFlag)
		{
			result = executeDatameerJob(noaaId1);
			System.out.println("Final Job Status:"+result+":"+noaaId1);
			if("COMPLETED".equals(result) || "COMPLETED_WITH_WARNINGS".equals(result))
			{
				result = executeDatameerJob(noaaId2);
				System.out.println("Final Job Status:"+result+":"+noaaId2);
			}
			if("COMPLETED".equals(result) || "COMPLETED_WITH_WARNINGS".equals(result))
			{
				result = executeDatameerJob(noaaId3);
				System.out.println("Final Job Status:"+result+":"+noaaId3);
			}
		}
		
		if(twitFlag)
		{
			result = executeDatameerJob(twitId1);
			System.out.println("Final Job Status:"+result+":"+twitId1);
			if("COMPLETED".equals(result) || "COMPLETED_WITH_WARNINGS".equals(result))
			{
				result = executeDatameerJob(twitId2);
				System.out.println("Final Job Status");
				System.out.println("Final Job Status:"+result+":"+twitId2);
			}
			if("COMPLETED".equals(result) || "COMPLETED_WITH_WARNINGS".equals(result))
			{
				result = executeDatameerJob(twitId3);
				System.out.println("Final Job Status:"+result+":"+twitId3);
			}
		}
		
		if(pentFlag)
		{
			executePentahoJob(pentFile);			
		}
	}
	
	public static void main(String[] args)
	{
		DatafactoryService dfService = new DatafactoryService();
		//System.getProperties().setProperty("KETTLE_PLUGIN_BASE_FOLDERS", "/home/ec2-user/data-integration/plugins");
		System.getProperties().setProperty("KETTLE_PLUGIN_BASE_FOLDERS", IDatameer.PENTAHO_PLUGIN_PATH);
		dfService.performExecution();
	}	
}
