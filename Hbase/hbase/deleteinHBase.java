package com.hbasetraining.client;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.util.Bytes;


public class deleteinHBase {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Configuration conf = HBaseConfiguration.create();
	    
	    HTable table = new HTable(conf, "testtable");
	    
	    System.out.println("Started Executing");
	    
	    Delete delete = new Delete(Bytes.toBytes("row1")); 

//	    delete.deleteColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
	    table.delete(delete); 

	    table.close();
	    System.out.println("delete completed");
	    
		
	}

}
