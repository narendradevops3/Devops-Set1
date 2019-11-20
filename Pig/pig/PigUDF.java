package com.datadotz.pigudf;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.impl.util.WrappedIOException;

@SuppressWarnings("deprecation")
public class Upper extends EvalFunc<String> {

	@Override
	public String exec(Tuple input) throws IOException {
		// TODO Auto-generated method stub

		if (input == null || input.size() == 0) {
			return null;
		}
		try {
			String str = (String) input.get(0);
			return str.toUpperCase();
		} catch (IOException e) {
			e.getMessage();
		}
		return null;
	}
}


// How to Run

// register /home/user/Desktop/upper.jar 
// A = load '/user/pig/drug.txt' using PigStorage(',') as (pid:int, pname:chararray, drug:chararray,gender:chararray,tot_amt:int);
// B = foreach A generate com.Upper(pname); (specify fully quqlified name)
// dump B;
