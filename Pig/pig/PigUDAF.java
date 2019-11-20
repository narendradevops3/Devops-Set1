package com.datadotz.pigudaf;

import org.apache.pig.data.Tuple;

public class max extends EvalFunc<Integer> {

	@Override
	public Integer exec(Tuple input) throws IOException {
		try {
			DataBag bag = (DataBag) input.get(0);
			if (bag == null)
				return null;

			int max_value = Integer.MIN_VALUE;
			for (Iterator<Tuple> it = bag.iterator(); it.hasNext();) {
				Tuple t = it.next();
				if (max_value < (Integer) t.get(0)) {
					max_value = (Integer) t.get(0);
				}
			}
			return max_value;
		} catch (ExecException ee) {
			throw ee;
		} catch (Exception e) {
			int errCode = 2106;
			String msg = "Error while computing maximum in "
					+ this.getClass().getSimpleName();
			throw new ExecException(msg, errCode, PigException.BUG, e);
		}
	}
}



// HOW TO RUN
// register /home/user/Desktop/max.jar 
// A = load '/user/pig/drug.txt' using PigStorage(',') as (pid:int, pname:chararray, drug:chararray,gender:chararray,tot_amt:int);
// B = group A by $2; 
// sm = foreach B generate group, com.max(A.$4) as s; (specify fully quqlified name)
// smorder = order sm by s desc;
// dump smorder;
