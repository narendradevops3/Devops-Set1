import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DatameerDFAPI implements IDatameer
{	
	public String jobDetails(Process proc) throws IOException
	{
	    BufferedReader read = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line ="", append ="";
							
		while((line = read.readLine()) != null)
		{
			System.out.println(line);
			append = append+ line;
		}
		return append;
	}
}
