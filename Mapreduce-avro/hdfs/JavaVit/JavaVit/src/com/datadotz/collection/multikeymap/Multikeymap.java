package com.datadotz.collection.multikeymap;

import org.apache.commons.collections.map.MultiKeyMap;

//import org.apache.commons.collections.map.MultiKeyMap;

public class Multikeymap {
	public static void main(String args[]) {
		MultiKeyMap multiKeyMap = new MultiKeyMap();
		multiKeyMap.put("New York", "IBM", "Sam");
		multiKeyMap.put("Sydney", "Infosys", "Honey");
		multiKeyMap.put("Prague", "JP Morgan", "Peter");
		multiKeyMap.put("Scotland", "RBS", "Deny");
		multiKeyMap.put("Paris", "Nomura", "Lily");
		multiKeyMap.put("Melbourne", "Citi Bank", "Sandy");
		multiKeyMap.put("Aukland", "Bank of America", "Tommy");

		System.out.println(multiKeyMap.get("Sydney", "Infosys"));
		System.out.println(multiKeyMap.get("Paris", "Nomura"));
		System.out.println(multiKeyMap.get("Nomura", "Paris"));
		System.out.println(multiKeyMap.get("Sydney", "IBM"));
	}
}