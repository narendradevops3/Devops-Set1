package com.hbasetraining.client;

// cc GetExample Example application retrieving data from HBase
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
//import util.HBaseHelper;

import java.io.IOException;

public class GetFromHBase {

	public void get() {
		String tableName = "testtablemar24";
		String columnFamily = "data";
		String columQualifier  = "NAME";
		String rowKey = "01";
		try
		{
		Configuration conf = HBaseConfiguration.create();

		conf.set("hbase.zookeeper.quorum", "localhost");
		HTable ta = new HTable(conf, tableName);

		Get getobj = new Get(Bytes.toBytes(rowKey));
		getobj.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columQualifier)); // data:name

		
		
		Result res = ta.get(getobj);
		byte[] val = res.getValue(Bytes.toBytes(columnFamily),
				Bytes.toBytes("name"));
		System.out.println("Value: " + Bytes.toString(val));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		GetFromHBase client = new  GetFromHBase();
		client.get();
	}
}