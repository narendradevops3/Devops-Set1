package me.sudohippie.weather.service.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.json.JSONException;
import org.json.XML;

public class RESTClientTest {
	public static void main(String[] args) throws Exception  {
		String endpoint = "http://graphical.weather.gov/xml/sample_products/browser_interface/ndfdXMLclient.php";
		//String endpoint = "http://graphical.weather.gov/xml/sample_products/browser_interface/ndfdBrowserClientByDay.php";
		RESTClient client = new RESTClient(endpoint);

		Map<String, String> params = new LinkedHashMap<String, String>();
		//params.put("listLatLon", "40.71,-74.01");
		params.put("zipCodeList", "20910");
		//params.put("zipCodeList", "00501+00544+06390+10001");
		params.put("product", "time-series");
		
		
		/*params.put("begin", "2014-11-16T00:00:00");
		params.put("end", "2017-11-22T00:00:00");*/
		//params.put("compType", "Between");
		//params.put("featureType", "Forecast_Gml2Point");
		//params.put("propertyName", "maxt,mint,wspd,sky,snow,dew,appt,rh");
		params.put("maxt", "maxt");
		params.put("mint", "mint");
		params.put("sky", "sky");
		params.put("snow", "snow");
		params.put("dew", "dew");
		params.put("appt", "appt");
		params.put("rh", "rh");
		params.put("wspd", "wspd");
		params.put("temp", "temp");
		

		String data;
		
			data = client.getDataAsString(params);
			
			
			
		
			 org.json.JSONObject xmlJSONObj = XML.toJSONObject(data);
			 
			
			 
		        
		     System.out.println(xmlJSONObj.toString(4));
		              
		/*File file = new File("E:/s4.json");
		if (!file.exists()) {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(DatainJsonObject.toString());
		}*/

		//System.out.println("Done");
	}
}
