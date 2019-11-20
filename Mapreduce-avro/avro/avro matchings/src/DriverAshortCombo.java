import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.AvroCollector;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroReducer;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapred.Pair;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.avro.mapreduce.AvroMultipleOutputs;
import org.apache.avro.util.Utf8;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DriverAshortCombo extends Configured implements Tool {
	
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String OUT_DATA_PATH = "/home/cloudera/Documents/hadoopout/"+ System.currentTimeMillis();
		String IN_DATA_PATH = "/home/cloudera/Documents/hadoopin/";
		Schema s1 = getSchemaFromFile(IN_DATA_PATH + "github.csv.avro");
		Schema s2 = getSchemaFromFile(IN_DATA_PATH + "sof.csv.avro");
		ArrayList<Schema> l = new ArrayList<Schema>();
		l.add(s1);
		l.add(s2);
		Schema s = Schema.createUnion(l);
		JobConf conf = new JobConf();
		// uncomment before deploying to cluster
		//conf.setJar("/usr/hadoop/comboavro.jar");
		conf.set("mapred.max.split.size", "20000");
		conf.set("mapred.compress.map.output", "true");
		/*
		 * <property> <name>mapred.map.output.compression.codec</name>
		 * <value>org.apache.hadoop.io.compress.DefaultCodec</value> </property>
		 */
		conf.set("mapred.map.output.compression.codec","org.apache.hadoop.io.compress.DefaultCodec");
		conf.set("mapred.child.java.opts", "-Xmx4096m -Xss600m");
		conf.set("mapred.map.child.java.opts", "-Xmx4096m -Xss600m");
		conf.set("mapred.reduce.child.java.opts", "-Xmx1000m");
		conf.set("io.sort.factor", "50");
		conf.set("io.sort.mb", "500");
		conf.set("io.sort.spill.percent", "0.80");
		Job j = new Job(conf);
		j.getConfiguration().set("mapreduce.map.log.level", "DEBUG");
		j.getConfiguration().set("mapreduce.reduce.log.level", "TRACE");
		DistributedCache.addFileToClassPath(new Path("/home/cloudera/Downloads/avrojars/avro-mapred-1.7.6-hadoop2.jar"), j.getConfiguration());
		DistributedCache.addFileToClassPath(new Path("/home/cloudera/Downloads/avrojars/avro-1.7.6.jar"), j.getConfiguration());
		DistributedCache.addFileToClassPath(new Path("/home/cloudera/Downloads/secondstring-20120620.jar"), j.getConfiguration());
		URI[] paths = DistributedCache.getCacheFiles(j.getConfiguration());
		for (URI p : paths)
			System.out.println("Distributed cache file:" + p.toString());
		AvroJob.setInputKeySchema(j, s);
		AvroJob.setMapOutputKeySchema(j, Schema.create(Schema.Type.STRING));
		AvroJob.setMapOutputValueSchema(j, s);
		// AvroJob.setOutputKeySchema(j, Schema.create(Schema.Type.STRING));
		// AvroJob.setOutputValueSchema(j, Schema.create(Schema.Type.STRING));
		AvroJob.setOutputKeySchema(j, s1);
		AvroJob.setOutputValueSchema(j, s2);
		AvroMultipleOutputs.addNamedOutput(j, "avro",AvroKeyValueOutputFormat.class, s1, s2);
		j.setNumReduceTasks(2);
		j.setInputFormatClass(AvroKeyInputFormat.class);
		FileInputFormat.setInputPaths(j, new Path(IN_DATA_PATH));
		j.setMapperClass(MapImpl.class);
		j.setReducerClass(ReduceImpl.class);
		FileOutputFormat.setOutputPath(j, new Path(OUT_DATA_PATH));
		//JobClient.runJob(job);
		j.waitForCompletion(true);
		return 0;
	}
	private Schema getSchemaFromFile(String strFile) {
		File file = new File(strFile);
		DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>();
		DataFileReader<GenericRecord> dataFileReader = null;
		try {
			dataFileReader = new DataFileReader<GenericRecord>(file, reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception:" + e.getMessage());
			e.printStackTrace();
		}
		Schema s = dataFileReader.getSchema();
		System.out.println("Input schema:" + s.toString(true));
		return s;
	}
	public static class MapImpl
			extends
			// AvroMapper<GenericRecord, Pair<String, GenericRecord>> {
			Mapper<AvroKey<GenericRecord>, NullWritable, AvroKey<String>, AvroValue<GenericRecord>> {
		public void map(AvroKey<GenericRecord> datum, NullWritable value,
				Context context) throws IOException, InterruptedException {
			//System.out.println("Welcome to map implementation");
			String avroid = datum.datum().get("avroid").toString();
			String datatype = datum.datum().get("datatype").toString();
			if (datatype.equalsIgnoreCase("sof")) {
				// for (int i = 1; i < 188194; i++) {
				for (int i = 1; i <= 101; i++) {
					String key = "github:" + Integer.toString(i) + ":sof:"+ avroid;
					// System.out.println("Map:" + key + ":" + datum);
					context.write(new AvroKey<String>(key),new AvroValue<GenericRecord>(datum.datum()));
				}
			}
			if (datatype.equalsIgnoreCase("github")) {
				// for (int i = 1; i < 67648; i++) {
				for (int i = 1; i <= 101; i++) {
					String key = "github:" + avroid + ":sof:"	+ Integer.toString(i);
					// System.out.println("Map:" + key + ":" + datum);
					context.write(new AvroKey<String>(key),new AvroValue<GenericRecord>(datum.datum()));
				}
			}
		}
	}
	public static class ReduceImplAvro
			extends
			AvroReducer<AvroKey<Utf8>, AvroValue<GenericRecord>, Pair<GenericRecord, GenericRecord>> {
		public void reduce(AvroKey<Utf8> key,
				Iterable<AvroValue<GenericRecord>> values,
				AvroCollector<Pair<GenericRecord, GenericRecord>> collector)
				throws IOException, InterruptedException {
			GenericRecord outkey = values.iterator().next().datum();
			GenericRecord outkeyCopy = GenericData.get().deepCopy(outkey.getSchema(), outkey);
			if (values.iterator().hasNext()) {
				GenericRecord outvalue = values.iterator().next().datum();
				GenericRecord outvalueCopy = GenericData.get().deepCopy(outvalue.getSchema(), outvalue);
				String datatype = outkeyCopy.get("datatype").toString();
				System.out.println(datatype);
				collector.collect(new Pair<GenericRecord, GenericRecord>(outkeyCopy, outvalueCopy));
			}
		}
	} 
	public static class ReduceImpl extends
			Reducer<AvroKey<Utf8>, AvroValue<GenericRecord>, AvroKey<GenericRecord>, AvroValue<GenericRecord>> {
		private AvroMultipleOutputs amos;
		public void setup(Context context) {
			amos = new AvroMultipleOutputs(context);
		}
		public void cleanup(Context context) throws IOException {
			try {
				amos.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void reduce(AvroKey<Utf8> key,
				Iterable<AvroValue<GenericRecord>> values, Context context)
				throws IOException, InterruptedException {
			GenericRecord outkey = values.iterator().next().datum();
			GenericRecord outkeyCopy = GenericData.get().deepCopy(outkey.getSchema(), outkey);
			if (values.iterator().hasNext()) {
				GenericRecord outvalue = values.iterator().next().datum();
				GenericRecord outvalueCopy = GenericData.get().deepCopy(outvalue.getSchema(), outvalue);
				String datatype = outkeyCopy.get("datatype").toString();
				// System.out.println(datatype);
				if (datatype.contains("github")) {
					// context.write(new AvroKey<GenericRecord>(outkeyCopy),
					// new AvroValue<GenericRecord>(outvalueCopy));
					amos.write("avro", new AvroKey<GenericRecord>(outkeyCopy),new AvroValue<GenericRecord>(outvalueCopy));
				} else {
					// context.write(new AvroKey<GenericRecord>(outvalueCopy),
					// new AvroValue<GenericRecord>(outkeyCopy));
					amos.write("avro",new AvroKey<GenericRecord>(outvalueCopy),new AvroValue<GenericRecord>(outkeyCopy));
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new DriverAshortCombo(),args);
		System.exit(res);
	}
}