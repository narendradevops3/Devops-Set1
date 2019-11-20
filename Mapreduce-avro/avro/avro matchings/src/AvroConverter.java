import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.codehaus.jackson.node.DecimalNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.TextNode;
import org.codehaus.jackson.node.BooleanNode;

public class AvroConverter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to Avro converter!");
		try {
			AvroConverter.readFile("/home/cloudera/Documents/csvin/sof.csv",
					"sof");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeSchema(Schema s, String file) {
		BufferedWriter out;
		try {
			// Create file
			FileWriter fstream = new FileWriter(file);
			out = new BufferedWriter(fstream);
			out.write(s.toString(true));
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void readFile(String infile, String datatype)
			throws IOException {
		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(infile));
		String line = br.readLine();
		System.out.println(line);
		Schema s = AvroConverter.createSchemaFromHeader(line, datatype);
		System.out.println(s.toString(true));
		writeSchema(s, infile + ".json");
		List<Schema.Field> fields = s.getFields();
		GenericRecordBuilder builder = new GenericRecordBuilder(s);
		File file = new File(infile + ".avro");
		DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(
				s);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(
				writer);
		dataFileWriter.create(s, file);
		try {
			while (true) {
				line = br.readLine();
				if (line == null)
					break;
				GenericRecord r = builder.build();
				System.out.println(line);
				if (line.length() > 2)
					line = line.substring(1, line.length() - 1);
				String[] values = line.split("\",\"");
				for (String value : values)
					System.out.println(value);
				// System.exit(0);
				int loop = values.length;
				if (loop > fields.size())
					loop = fields.size();
				for (int i = 0; i < loop; i++) {
					if (!values[i].equalsIgnoreCase("NULL")) {
						String f = fields.get(i).name();
						if (!f.contains("themes"))
							r.put(f, values[i].replace("\"", ""));
						else {
							ArrayList<String> arr = new ArrayList<String>();
							String[] themes = values[i].split("::");
							for (String theme : themes)
								arr.add(theme);
							r.put(f, arr);
						}
					}
				}
				r.put("avroid", Integer.toString(count + 1));
				r.put("datatype", datatype);
				dataFileWriter.append(r);
				System.out.println(r);
				// System.exit(0);
				count++;
			}
		} finally {
			System.out.println(count);
			br.close();
			dataFileWriter.close();
		}
	}

	public static Schema createSchemaFromHeader(String header, String datatype) {
		String[] fields = header.replace("\"", "").split(",");
		Schema s = Schema.createRecord(datatype, " User Data", "com.draganfly",
				false);
		Schema schThemes = Schema.create(Schema.Type.STRING);
		List<Schema.Field> lstFields = new LinkedList<Schema.Field>();
		for (String f : fields) {
			if (!f.contains("themes"))
				lstFields.add(new Schema.Field(f.replace("\"", ""), Schema
						.create(Schema.Type.STRING), "doc",
						new org.codehaus.jackson.node.TextNode("")));
			else
				lstFields.add(new Schema.Field(f.replace("\"", ""), Schema
						.createArray(schThemes), "doc",
						new org.codehaus.jackson.node.TextNode("")));
		}
		lstFields.add(new Schema.Field("avroid", Schema
				.create(Schema.Type.STRING), "doc",
				new org.codehaus.jackson.node.TextNode("")));

		lstFields.add(new Schema.Field("datatype", Schema
				.create(Schema.Type.STRING), "doc",
				new org.codehaus.jackson.node.TextNode("sof")));

		s.setFields(lstFields);

		System.out.println(s.toString(true));

		return s;
	}

}