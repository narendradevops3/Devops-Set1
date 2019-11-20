import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;

import org.apache.hadoop.hbase.HColumnDescriptor;

import org.apache.hadoop.hbase.HTableDescriptor;

import org.apache.hadoop.hbase.client.HBaseAdmin;

public class HbaseConnection {
	public static void main(String[] args) throws IOException {
				
		HBaseConfiguration config = new HBaseConfiguration();
        config.clear();
        config.set("hbase.zookeeper.quorum", "52.0.190.86");
        config.set("hbase.zookeeper.property.clientPort","2181");
        config.set("hbase.master", "52.0.190.86:60000");

		HTableDescriptor ht = new HTableDescriptor("Tmp");

		ht.addFamily(new HColumnDescriptor("Ids"));

		ht.addFamily(new HColumnDescriptor("Names"));

		System.out.println("connecting");

		HBaseAdmin hba = new HBaseAdmin(config);

		System.out.println("Creating Table");

		hba.createTable(ht);

		System.out.println("Done......");
	}
}