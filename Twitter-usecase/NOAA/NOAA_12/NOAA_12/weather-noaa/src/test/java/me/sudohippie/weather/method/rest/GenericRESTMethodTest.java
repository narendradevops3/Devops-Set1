package me.sudohippie.weather.method.rest;

import java.io.IOException;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.XML;
import org.json.simple.JSONValue;

import net.sf.json.JSONObject;
import me.sudohippie.weather.NOAAWeather;
import me.sudohippie.weather.exception.NOAACommunicationException;
import me.sudohippie.weather.method.NOAAMethod;

/**
 * Raghav Sidhanti
 * 9/20/13
 */
public class GenericRESTMethodTest {
    public static void testNDFDGen() throws NOAACommunicationException, JSONException, IOException {

        NOAAMethod method = new GenericRESTMethod();
        
        method.addArgument("zipCodeList", "00501+00544+06390+10001");
       /* method.addArgument("listLat1", "40.496029");
        method.addArgument("listLon1", "-79.761971");
        method.addArgument("listLat2", "45.012810");
        method.addArgument("listLon2", "-71.856247");
        method.addArgument("resolutionSub", "1");*/
        
        method.addArgument("product", "time-series");
        method.addArgument("begin", "2014-11-16T00:00:00");
        method.addArgument("end", "2017-11-22T00:00:00");
        method.addArgument("Unit", "e");
        method.addArgument("maxt", "maxt");
        method.addArgument("mint", "mint");
        method.addArgument("temp", "temp");
        method.addArgument("sky", "sky");
        method.addArgument("snow", "snow");
        method.addArgument("wdir", "wdir");

        NOAAWeather weather = new NOAAWeather();
        String data = weather.query(method);
        org.json.JSONObject xmlJSONObj = XML.toJSONObject(data);
        
        String jsonstring = xmlJSONObj.toString(2);
              
        

       // System.out.println(data);
        //System.out.println("4");
       // System.out.println(jsonPrettyPrintString);
       System.out.println(jsonstring);
        
    }

    public static void main(String[] args) throws NOAACommunicationException, JSONException, IOException {
        testNDFDGen();
    }
}
