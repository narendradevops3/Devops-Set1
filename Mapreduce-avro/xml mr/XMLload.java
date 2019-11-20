package org.samples.mapreduce.training;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hive.contrib.mr.example.IdentityMapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;


public class XMLload{
	public static class XMLDataLoadMapper extends Mapper<LongWritable, Text, Text, Text>{
		private Document document;
		

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			
//			<student>
//			<rollno>12</rollno>
//			<name>senthil</name>
//			</student>			
			SAXBuilder saxBuilder = new SAXBuilder();
			System.out.println(value.toString());
			StringReader reader = new StringReader(value.toString());
			try {
				document =  saxBuilder.build(reader);
				String rollNo = getNodeAttribute(document,"//rollno");

				String name = getNodeAttribute(document, "//name");			
				
				context.write(new Text(rollNo), new Text(name));
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("rawtypes")
		private String getNodeAttribute(Document document, String xpath) {
			String attrValue = null;
			XPathFactory xpfac = XPathFactory.instance();
			XPathExpression xp = xpfac.compile(xpath,Filters.element(),null);
			Element element = (Element)xp.evaluateFirst(document);
			if(element!=null)
				attrValue = element.getText();
			return attrValue;
		}	
		
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		String inputPath = args[0];
		String outputPath = args[1];
		if(args.length!=2){
			System.err.println("Insufficient args");
			System.exit(-1);
		}
		
		Configuration conf = new Configuration();
		conf.set(XMLInputFormat.START_TAG_KEY, "<student");
		conf.set(XMLInputFormat.END_TAG_KEY, "</student>");
		
		Job job = new Job(conf,"xml input format");
		job.setJobName("xml Load");
		FileInputFormat.addInputPath(job, new Path(inputPath));
		
		job.setJarByClass(XMLload.class);

		job.setMapperClass(XMLDataLoadMapper.class);
		
		job.setInputFormatClass(XMLInputFormat.class); 
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setNumReduceTasks(0);
		
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		job.waitForCompletion(true);
		System.out.println(job.isSuccessful()?0:1);
	}
}
