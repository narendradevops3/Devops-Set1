package com.hbasetraining.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

public class CreateTable {

	public void create() {
		String tableName = "testtablev1";
		String ColumnFamily = "data";
		try {
			Configuration conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", "localhost");
			HBaseAdmin admin = new HBaseAdmin(conf);

			
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
			}

			HTableDescriptor desc = new HTableDescriptor(tableName);

			HColumnDescriptor coldef = new HColumnDescriptor(ColumnFamily);
			desc.addFamily(coldef);

			admin.createTable(desc);
			System.out.println("Table Created Successfully");
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void main(String[] args) throws IOException {
		CreateTable client = new CreateTable();
		client.create();
	}
}