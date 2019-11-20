
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerTest implements Runnable {
	private KafkaStream m_stream;
	private int m_threadNumber;
	
	public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
		m_threadNumber = a_threadNumber;
		m_stream = a_stream;
	}

	public void run()  {

		
		
		
		try{
		
			Configuration conf = new Configuration();
			conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
			conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
			
			FileSystem hdfs;
			
				hdfs = FileSystem.get(conf);
			
				Path filepath = new Path("/data/sri2/t.json");
				
				if ( hdfs.exists( filepath ))
				{
					
					hdfs.delete( filepath);
					System.out.println("Existing file deleted");
				}
				
				FSDataOutputStream os = hdfs.create(filepath);
				
				System.out.println("New file created");
				ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
				while (it.hasNext()) {
					System.out.println("Thread " + m_threadNumber + ": "
							+ new String(it.next().message()));
						 
					os.writeUTF(new String(it.next().message()));
					System.out.println("File writing");
					os.flush();
				}
					
					os.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
					System.out.println("Shutting down Thread: " + m_threadNumber);
			}				
			
			
	}
	