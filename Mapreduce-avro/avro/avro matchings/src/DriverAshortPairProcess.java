import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.avro.mapreduce.AvroKeyValueInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.avro.mapreduce.AvroMultipleOutputs;
import org.apache.avro.util.Utf8;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.codehaus.jackson.node.DoubleNode;
import org.codehaus.jackson.node.TextNode;

import com.wcohen.ss.Levenstein;

public class DriverAshortPairProcess extends Configured implements Tool {

	// public static void main(String[] args) {

	public int run(String[] args) {

		String OUT_DATA_PATH = "/home/cloudera/Documents/out"
				+ System.currentTimeMillis();

		// String IN_DATA_PATH =
		// "/Users/serge/Downloads/Gozaik/input_pair_1249/";

		String IN_DATA_PATH = "/home/cloudera/Documents/in";

		// String IN_DATA_PATH =
		// "/Users/serge/Downloads/Gozaik/input_elie_9/pair";

		// String IN_DATA_PATH =
		// "/Users/serge/Downloads/Gozaik/input_elie_3k/pair";

		// /Users/serge/Downloads/Gozaik/input_elie_1249_2/avro

		// /Users/serge/Downloads/Gozaik/input_elie_a_2/avro/

		// Schema schemaKey =
		// getSchemaFromFile("/Users/serge/Downloads/Gozaik/input_elie_test_1000/Avro/github.csv.avro");
		Schema schemaKey = getSchemaFromFile("/home/cloudera/Documents/schema/github.csv.avro");
		// Schema schemaKey =
		// getSchemaFromFile("/Users/serge/Downloads/Gozaik/input_elie_1249_2/avro/github.csv.avro");

		System.out.println("Input schema:" + schemaKey.toString(true));

		// Schema schemaValue =
		// getSchemaFromFile("/Users/serge/Downloads/Gozaik/input_elie_test_1000/Avro/sof.csv.avro");
		Schema schemaValue = getSchemaFromFile("/home/cloudera/Documents/schema/sof.csv.avro");
		// Schema schemaValue =
		// getSchemaFromFile("/Users/serge/Downloads/Gozaik/input_elie_1249_2/avro/sof.csv.avro");

		System.out.println("Input schema:" + schemaValue.toString(true));

		ArrayList<Schema> l = new ArrayList<Schema>();
		l.add(schemaKey);
		l.add(schemaValue);
		Schema schemaOutput = Schema.createArray(Schema.createUnion(l));

		JobConf conf = new JobConf();

		// uncomment before deploying to cluster
		// conf.setJar("/home/ec2-user/avrodriverAshortPair.jar");

		// 1000000000
		// 1483680198
		conf.set("mapred.max.split.size", "10000");
		// conf.set("mapred.compress.map.output", "true");
		/*
		 * <property> <name>mapred.map.output.compression.codec</name>
		 * <value>org.apache.hadoop.io.compress.DefaultCodec</value> </property>
		 */
		// conf.set("mapred.map.output.compression.codec","org.apache.hadoop.io.compress.DefaultCodec");

		conf.set("mapred.child.java.opts", "-Xmx1000m");

		conf.set("mapred.map.child.java.opts", "-Xmx1000m");

		conf.set("mapred.reduce.child.java.opts", "-Xmx1000m");

		conf.set("io.sort.factor", "50");

		conf.set("io.sort.mb", "500");

		conf.set("io.sort.spill.percent", "0.80");

		Job j;
		try {
			j = new Job(conf);

			DistributedCache
					.addFileToClassPath(
							new Path(
									"/home/cloudera/Downloads/java/avro-mapred-1.7.5-hadoop2.jar"),
							j.getConfiguration());

			DistributedCache.addFileToClassPath(new Path(
					"/home/cloudera/Downloads/java/avro-1.7.5.jar"), j
					.getConfiguration());

			DistributedCache.addFileToClassPath(new Path(
					"/home/cloudera/Downloads/secondstring-20120620.jar"), j
					.getConfiguration());

			URI[] paths = DistributedCache.getCacheFiles(j.getConfiguration());
			for (URI p : paths)
				System.out.println("Distributed cache file:" + p.toString());

			AvroJob.setInputKeySchema(j, schemaKey);
			AvroJob.setInputValueSchema(j, schemaValue);

			// AvroJob.setMapOutputKeySchema(j, schemaKey);
			// AvroJob.setMapOutputValueSchema(j, schemaValue);
			// AvroJob.setOutputKeySchema(j, Schema.create(Schema.Type.STRING));
			// AvroJob.setOutputValueSchema(j,
			// Schema.create(Schema.Type.STRING));

			// AvroJob.setMapOutputKeySchema(j,
			// Schema.create(Schema.Type.STRING));

			AvroJob.setMapOutputKeySchema(j, compare.getCompareSchema());
			AvroJob.setMapOutputValueSchema(j, schemaOutput);

			// AvroJob.setOutputKeySchema(j, Schema.create(Schema.Type.STRING));
			AvroJob.setOutputKeySchema(j, compare.getCompareSchema());

			AvroJob.setOutputValueSchema(j, schemaOutput);

			j.setNumReduceTasks(0);

			j.setInputFormatClass(AvroKeyValueInputFormat.class);
			j.setOutputFormatClass(AvroKeyValueOutputFormat.class);

			AvroMultipleOutputs.addNamedOutput(j, "avro",
					AvroKeyValueOutputFormat.class, compare.getCompareSchema(),
					schemaOutput);

			FileInputFormat.setInputPaths(j, new Path(IN_DATA_PATH));

			j.setMapperClass(MapImpl.class);

			FileOutputFormat.setOutputPath(j, new Path(OUT_DATA_PATH));

			System.out.println("Launching map -reduce job:");

			j.waitForCompletion(true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception:" + e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception:" + e);

			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception:" + e);

			e.printStackTrace();
		}
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
			Mapper<AvroKey<GenericRecord>, AvroValue<GenericRecord>, AvroKey<String>, AvroValue<GenericData.Array<GenericRecord>>> {

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

		public void map(AvroKey<GenericRecord> v1, AvroValue<GenericRecord> v2,
				Context context) throws IOException, InterruptedException {

			/*
			 * String name_s1 = v2.datum().get("name_s1").toString(); String
			 * name_s2 = v1.datum().get("name_s2").toString(); String
			 * name="asanghi";
			 * 
			 * if(name_s1.equalsIgnoreCase("aditya sanghi")&&(name_s2.
			 * equalsIgnoreCase("asanghi")))
			 * 
			 * {
			 */// LOOKING FOR A SPECIFIC PAIR
			GenericRecord recResult = compare.compare(v1.datum(), v2.datum());

			double result = (Double) recResult.get("result");
			//if (result > 0) {

			Schema schemaKey = v1.datum().getSchema();

			Schema schemaValue = v2.datum().getSchema();

			ArrayList<Schema> l = new ArrayList<Schema>();
			l.add(schemaKey);
			l.add(schemaValue);
			Schema schemaOutput = Schema.createArray(Schema.createUnion(l));

			GenericData.Array<GenericRecord> output = new GenericData.Array<GenericRecord>(
					2, schemaOutput);

			output.add(v1.datum());
			output.add(v2.datum());

			System.out.println(output);
			// context.write(new AvroKey<String>(Double.toString(result)),
			// new AvroValue<GenericData.Array<GenericRecord>>(output));

			amos.write("avro", new AvroKey<GenericRecord>(recResult),
					new AvroValue<GenericData.Array<GenericRecord>>(output));

		}
		// } ///LOOKING FOR A SPECIFIC PAIR

		// }
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(),
				new DriverAshortPairProcess(), args);
		System.exit(res);

	}
}