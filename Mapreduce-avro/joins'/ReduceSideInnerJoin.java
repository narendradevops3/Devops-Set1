package org.samples.mapreduce.training;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;



// select patient.drugid,sum(patient.amount) from patient join drugtable on drugtable.drug = patient.drug

public class ReduceSideInnerJoin {
	public static class PatientMap extends
			Mapper<LongWritable, Text, Text, Text> {

		private Text outKey = new Text();
		private Text outValue = new Text();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] elements = line.split(",");

			outKey.set(elements[2]); // join key is drug name - paracetamol
			outValue.set("P" + elements[4]);// P is used to denote this value is from patient mapper class P100
			context.write(outKey, outValue);
		}
	}

	public static class DrugMap extends Mapper<LongWritable, Text, Text, Text> {

		private Text outKey = new Text();
		private Text outValue = new Text();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] elements = line.split(",");
			outKey.set(elements[1]);// join key is drug name paracetamol
			outValue.set("D" + elements[0]); // D is used to denote this value is ffrom drug mapper calss -- drug id  D01
			context.write(outKey, outValue);
		}
	}

	public static class JoinReduce extends
			Reducer<Text, Text, Text, IntWritable> {

		private Text outKey = new Text();
		private IntWritable outValue = new IntWritable();
		private Integer sum = 0;
		private ArrayList<Integer> listP = new ArrayList<Integer>();
		private ArrayList<String> listD = new ArrayList<String>();
		HashSet<String> hashSetD = new HashSet<String>();
		// input key - paracetamol  input values - [P100,D01,P60]
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			// Clear our lists
			listP.clear();
			hashSetD.clear();
//			listD.clear();
			sum = 0;
			// iterate through all our values, binning each record based on what
			// it was tagged with. Make sure to remove the tag!
			for (Text val : values)
				if (val.charAt(0) == 'P') {
					listP.add(Integer.parseInt(val.toString().substring(1))); // remove P from P100 and add 100 to list
				} else if (val.charAt(0) == 'D') {
					hashSetD.add(val.toString().substring(1)); //remove D from D01 and 01 to set
				}
			// listP = [100,60]
			//hashsetD = ["01"]
			executeJoinLogic(context);
		}

		private void executeJoinLogic(Context context) throws IOException,
				InterruptedException {
			System.out.println(hashSetD.toString());
			System.out.println(listP.toString());
			if (!listP.isEmpty() && !hashSetD.isEmpty() && hashSetD.size() == 1) {
				for (String D : hashSetD) {
					outKey.set(D);
					for (Integer a : listP) {
						sum += a;
					}
					outValue.set(sum);
				}
				context.write(outKey, outValue);// 01  160
			}

		}

	}

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.err.println("Insufficient args");
			System.exit(-1);
		}

		Configuration conf = new Configuration();

		conf.set("mapred.job.tracker", "hdfs://localhost:50001");
		conf.set("fs.default.name", "hdfs://localhost:50000");
		
		Job job = new Job(conf, "Reduce Side Inner Join");

		job.setJarByClass(ReduceSideInnerJoin.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		
		System.out.println(args[0]);
		System.out.println(args[1]);
//patientinputfile
		MultipleInputs.addInputPath(job, new Path(args[0]),
				TextInputFormat.class, PatientMap.class);
//druginput file
		MultipleInputs.addInputPath(job, new Path(args[1]),
				TextInputFormat.class, DrugMap.class);

		job.setReducerClass(JoinReduce.class);
		job.setNumReduceTasks(1);
//		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// FileInputFormat.addInputPath(job, new Path("/patientrx_10.txt"));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		job.waitForCompletion(true);

	}

}