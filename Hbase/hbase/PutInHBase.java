package com.hbasetraining.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PutInHBase {

	public void putData() {
		Configuration conf = HBaseConfiguration.create(); 
		conf.set("hbase.zookeeper.quorum", "localhost");
		String tableName = "testtablemar24";
		String columnFamily = "data";
		String columQualifier_1 = "name";
		String columQualifier_2 = "age";
		String rowKey = new Date() + "_MRP00001_AL";
		try {

			HTable table = new HTable(conf, tableName);

			Put put = new Put(Bytes.toBytes(rowKey));

			/*put.add(Bytes.toBytes(columnFamily),
					Bytes.toBytes(columQualifier_1), Bytes.toBytes("senthil"));*/
			put.add(Bytes.toBytes(columnFamily),
					Bytes.toBytes(columQualifier_2), Bytes.toBytes("10"));
			
			put.add(Bytes.toBytes(columnFamily),
					Bytes.toBytes("NAME"), Bytes.toBytes("SIVA"));

			table.put(put);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void deleteData(){
		 Configuration conf = HBaseConfiguration.create();
		    
		 try {
		    HTable table = new HTable(conf, "hbasedemo");
		    
		    Delete delete = new Delete(Bytes.toBytes("02")); 
		    table.delete(delete); // co DeleteExample-9-DoDel Delete the data from the HBase table.

		   
				table.close();
			
		    // ^^ DeleteExample
		    System.out.println("After delete call...");
		    
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	}
	public void putListData(){
		Configuration conf = HBaseConfiguration.create(); 
		String tableName = "pashtable";
		String columnFamily = "data";
		String columQualifier_1 = "name";
		String columQualifier_2 = "age";
		String rowKey_1 = "03";
		String rowKey_2 = "04";
		String rowKey_3 = "05";
		try{
	    HTable table = new HTable(conf, tableName);

	    
	    List<Put> puts = new ArrayList<Put>(); 

	    Put put1 = new Put(Bytes.toBytes(rowKey_1));
	    put1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(columQualifier_1),
	      Bytes.toBytes("dinesh"));
	    puts.add(put1); 
	    
	    
	    
	    Put put2 = new Put(Bytes.toBytes(rowKey_2));
	    put2.add(Bytes.toBytes(columnFamily), Bytes.toBytes(columQualifier_1),
	      Bytes.toBytes("partha"));
	    puts.add(put2); 

	    Put put3 = new Put(Bytes.toBytes(rowKey_3));
	    put3.add(Bytes.toBytes(columnFamily), Bytes.toBytes(columQualifier_2),
	      Bytes.toBytes("27"));
	    puts.add(put3); 

	   
	    table.put(puts); 
		}
		catch(Exception e)	{
			e.printStackTrace();
		}
	    
	}
	

	public static void main(String[] args) throws IOException {
		PutInHBase client = new PutInHBase();
		client.putData();
//		client.putListData();
//		client.deleteData();
	}
}