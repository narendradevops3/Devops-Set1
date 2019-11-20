package org.samples.mapreduce.training;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author senthil
 * MapSide Join Program - inputs are patientfile and DrugFile
 */
public class MapSideInnerJoin_patientRx extends Configured implements Tool {
	public static class Mapper1 extends Mapper<LongWritable, Text, Text, IntWritable> {

		private Map<String, String> keyLookup = new HashMap<String, String>();
		private Text outKey = new Text();
		private IntWritable outValue = new IntWritable();

		@Override
		protected void setup(Context ctx) throws IOException,
				InterruptedException {

			Path[] cachedFiles = DistributedCache.getLocalCacheFiles(ctx
					.getConfiguration()); // cachedFiles will have ony one element bcoz i have added one file to DS cache
			if (cachedFiles != null && cachedFiles.length > 0) {
				BufferedReader reader = new BufferedReader(new FileReader(
						cachedFiles[0].toString()));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						System.out.println(line);
						String[] cols = line.split(",");

						keyLookup.put(cols[1], cols[0]); // cols[1] -- paracetamol  cols [0] -- 01
					}
				} finally {
					reader.close();
				}
			}
		}

		@Override
		protected void map(LongWritable key, Text value, Context ctx)
				throws IOException, InterruptedException {
	
//		1,senthil,paracetamol,male,100
			String line = value.toString();
			String[] elements = line.split(","); //elements[2] - paracetamol
			if (keyLookup.get(elements[2]) != null) {
				// HashMAP "keyLookup" contains ("PARACETAMOL","01")
				//keyLookup.get(elements[2]) gives  01
				outKey.set(keyLookup.get(elements[2])); // new Text(1)
				outValue.set(Integer.parseInt(elements[4]));
				ctx.write(outKey, outValue);//01 , 100
			}
		}
	}

	public static class Reducer1 extends Reducer<Text, IntWritable, Text, IntWritable> {

		private int sum = 0;
		private IntWritable i = new IntWritable();
		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context ctx) throws IOException, InterruptedException {
			sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			i.set(sum);
			ctx.write(key, i);
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		Configuration conf = getConf();
		Job job = new Job(conf,"mapsidejjoin");
		DistributedCache.addCacheFile(new Path(args[1]).toUri(),job.getConfiguration());
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		job.setJarByClass(MapSideInnerJoin_patientRx.class);
		job.setMapperClass(Mapper1.class);
		job.setReducerClass(Reducer1.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		boolean succ = job.waitForCompletion(true);
		if (!succ) {
			System.out.println("Job failed, exiting");
			return -1;
		}
		return 0;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(args.toString());
		if (args.length < 3) {
			System.out.println("Usage: inputfile1 joinfile output");
			System.exit(-1);
		}
		int res = ToolRunner.run(new Configuration(),
				new MapSideInnerJoin_patientRx(), args);
		System.exit(res);
	}

}
