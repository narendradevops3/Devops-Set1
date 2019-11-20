
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;

public class AvroToCsvConverter {
	static int countfiles = 1;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Welcome to Avro converter!");
		File[] listoffiles = finder("/home/cloudera/Documents/out1407774897980/");

		for (File file : listoffiles) {
			try {

				AvroToCsvConverter.readAvroFile(file.toString());
				countfiles++;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static File[] finder(String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(".avro");
			}
		});

	}

	@SuppressWarnings("unchecked")
	private static void readAvroFile(String strFile) throws IOException {

		File file = new File(strFile);

		DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>();

		DataFileReader<GenericRecord> dataFileReader;
		dataFileReader = new DataFileReader<GenericRecord>(file, reader);

		Schema s = dataFileReader.getSchema();

		System.out.println(s.toString(true));

		Record r;
		String recKey;
		Record r1;
		Record r2;

		File res_file = new File("/home/cloudera/Documents/out1407774897980/"
				+ countfiles + ".csv");

		// if file doesnt exists, then create it
		if (!res_file.exists()) {
			res_file.createNewFile();
		}

		FileWriter fw = new FileWriter(res_file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		// while (dataFileReader.hasNext()) {

		while (dataFileReader.hasNext()) {

			r = (Record) dataFileReader.next();

			recKey = (String) r.get("key").toString();

		GenericData.Array<GenericRecord> recValue = (GenericData.Array<GenericRecord>) r
			.get("value");
			r1 = (Record) recValue.get(0);
			r2 = (Record) recValue.get(1);
			String rec = recKey + getStrForRec(r1) + getStrForRec(r2);

			bw.flush();

			bw.write(rec + "\n");
			// c++;
			bw.flush();
			// if(c==50) break;
		}
		// i++;c=1;
		bw.close();
	}

	private static String getStrForRec(Record r)

	{

		String rt = "";

		for (int i = 0; i < r.getSchema().getFields().size(); i++)

			rt += "|" + r.get(i);

		return rt;
	}

	@SuppressWarnings("unused")
	private static String getStrForSchema(Record r)

	{

		String rt = "";

		Schema s = r.getSchema();

		for (int i = 0; i < s.getFields().size(); i++)

			rt += "|" + s.getName() + "." + s.getFields().get(i).name();

		return rt;
	}

}