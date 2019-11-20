package com.hbasetraining.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class ScanInHbase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void scanAll() {

		String tableName = "testtablemar24";
		String columnFamily = "data";
		String columQualifier = "NAME";
		String rowKey = "01";
		try {
			Configuration conf = HBaseConfiguration.create();

			conf.set("hbase.zookeeper.quorum", "localhost");
			HTable ta = new HTable(conf, tableName);

			Scan scan = new Scan();
			// scan1.getCaching(500);
			ResultScanner resultScanner = ta.getScanner(scan);
			for (Result res : resultScanner) {
				System.out.println(res);
				System.out.println(res.raw());
			}
			resultScanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
