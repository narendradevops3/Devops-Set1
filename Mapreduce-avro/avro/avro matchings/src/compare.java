import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.Array;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericRecordBuilder;
import org.codehaus.jackson.node.DoubleNode;

public class compare {

	// static DriverAshortPairProcess driverA = new DriverAshortPairProcess();

	public static Schema getCompareSchema() {
//		String[] fields = { "result", "Namescore", "loginscore", "themeScore",
//				"urlscore", "userlocationscore", "statescore", "countryscore",
//				"Average_Final", "Average", "AverageX" };
		String[] fields = { "result", "Namescore" };

		Schema s = Schema.createRecord("Ex2", "namespace", "something", false);

		List<Schema.Field> lstFields = new LinkedList<Schema.Field>();

		for (String f : fields) {

			lstFields
					.add(new Schema.Field(f, Schema.create(Schema.Type.DOUBLE),
							"doc", new DoubleNode(0)));

		}

		s.setFields(lstFields);

		return s;

	}

	// /this is the new function that needs to be implemented
	public static GenericRecord compare(GenericRecord v1, GenericRecord v2) {

		Schema s = compare.getCompareSchema();
		GenericRecordBuilder builder = new GenericRecordBuilder(s);
		GenericRecord r = builder.build();

		int[] name_parts_length = new int[2];
		double[] average = new double[4];
		double[] average2 = new double[6];
		double themeScore = 0;
		double Namescore = 0, urlscore = 0, loginscore = 0;

		int count = 0;

		String name_s1 = v2.get("user_name").toString();
		String name_s2 = v1.get("login_name").toString();

		// String name_s1b = v2.get("name_s1b").toString();
		//String name_s2b = v1.get("name_s2b").toString();

		if (isValid(name_s1, name_s2)) {

			name_parts_length[0] = name_s1.split(" ").length;
			name_parts_length[1] = name_s2.split(" ").length;

			Namescore = getSimname(name_s1, name_s2);
			// System.out.println("Namescore" + Namescore);

			average[0] = Namescore;
			r.put("Namescore", Namescore);
			count++;
		}
//		if (isValid(name_s1, name_s2)) {
//
//			loginscore = getSimname(name_s1, name_s2);
//			// System.out.println("loginscore" + loginscore);
//			r.put("loginscore", loginscore);
//
//		}

//		GenericData.Array<String> themes_s1 = (GenericData.Array<String>) v2
//				.get("themes_s1");
//		GenericData.Array<String> themes_s2 = (GenericData.Array<String>) v1
//				.get("themes_s2");
//
//		ArrayList<String> lstThemes_s1 = removeElements(themes_s1);
//		ArrayList<String> lstThemes_s2 = removeElements(themes_s2);
//
//		if (isValid(lstThemes_s1, lstThemes_s2)) {
//
//			// System.out.println("lstthemes_s1::" + lstThemes_s1);
//			// System.out.println("lstthemes_s2::" + lstThemes_s2);
//			if (lstThemes_s1.size() == 0 || lstThemes_s2.size() == 0) {
//				themeScore = 0;
//				// System.out.println("themescore both size zero::" +
//				// themeScore);
//				r.put("themeScore", themeScore);
//			} else {
//				themeScore = getThemeSim(lstThemes_s1, lstThemes_s2);
//				// System.out.println("themescore::" + themeScore);
//				average[1] = themeScore;
//				r.put("themeScore", themeScore);
//				count++;
//			}
//		}
//
//		String url_s1 = v2.get("url_s1").toString();
//		String url_s2 = v1.get("url_s2").toString();
//
//		if (isValid(url_s1, url_s2)) {
//
//			List<String> url1 = split(url_s1);
//			List<String> url2 = split(url_s2);
//			System.out.println("url_s1" + url1);
//			System.out.println("url_s2" + url2);
//			ArrayList<String> cleanurl1 = getHomePage(url1);
//			ArrayList<String> cleanurl2 = getHomePage(url2);
//			System.out.println("cleanurl_s1" + cleanurl1);
//			System.out.println("cleanurl_s2" + cleanurl2);
//			urlscore = listCompare(url1, url2);
//			System.out.println("urlscore" + urlscore);
//
//			r.put("urlscore", urlscore);
//			average[2] = urlscore;
//
//			count++;
//		}
//
//		double sum = 0;
//		double Average = 0;
//		for (int i = 0; i < average.length; i++) { // for loop is executed
//													// name,theme,loginscore,urlscore
//
//			sum += average[i];
//		}
//
//		Average = (sum / count);
//		// System.out.println(jaro"Average:Global(url+name+theme)" + Average);
//		r.put("Average", Average);
//
//		double AverageX = -1;// can be any one of Average2, Average3
//		int count_location = 0;
//
//		String location_formatted_s1 = v2.get("location_formatted_s1")
//				.toString();
//		String location_formatted_s2 = v1.get("location_formatted_s2")
//				.toString();
//
//		String userlocation_s1 = v2.get("userlocation_s1").toString();
//		String userlocation_s2 = v1.get("userlocation_s2").toString();
//
//		if (location_formatted_s1.length() == 0
//				|| location_formatted_s2.length() == 0) {
//
//			if (isValid(userlocation_s1, userlocation_s2)) {
//
//				userlocation_s1 = userlocation_s1.replaceAll(",", "");
//				userlocation_s2 = userlocation_s2.replaceAll(",", "");
//
//				double userlocationscore = getSimuserlocation(userlocation_s1,
//						userlocation_s2);
//				// System.out.println("userlocationscore:" + userlocationscore);
//				// if (userlocationscore < 0.87) {
//				// userlocationscore = 0;
//				// }
//
//				r.put("userlocationscore", userlocationscore);
//
//				AverageX = userlocationscore;
//				r.put("AverageX", AverageX);
//				count++;
//			}
//		}
//
//		String state_s1 = v2.get("state_s1").toString();
//		String state_s2 = v1.get("state_s2").toString();
//		String country_s1 = v2.get("country_s1").toString();
//		String country_s2 = v1.get("country_s2").toString();
//
//		if (isValid(location_formatted_s1) && isValid(location_formatted_s2)) {
//
//			if (isValid(state_s1, state_s2)) {
//
//				double statescore = getSimstate(state_s1, state_s2);
//				// System.out.println("statescore: " + statescore);
//				if (statescore < 0.87) {
//					statescore = 0;
//				}
//
//				r.put("statescore", statescore);
//
//				average2[0] = statescore;
//				count++;
//				count_location++;
//
//			}
//			if (isValid(country_s1, country_s2)) {
//				double countryscore = getSimcountry(country_s1, country_s2);
//				// System.out.println("countryscore: " + countryscore);
//				if (countryscore < 0.87) {
//					countryscore = 0;
//				}
//
//				r.put("countryscore", countryscore);
//
//				average2[1] = countryscore;
//				count++;
//				count_location++;
//
//			}
//
//			if (count_location > 0) {
//				double sum1 = 0;
//				for (int i = 0; i < average2.length; i++) {
//					sum1 += average2[i];
//				}
//				double Average_locationformatted = (sum1 / count_location);
//				AverageX = Average_locationformatted;
//				// System.out.println("AverageX:location_formated" + AverageX);
//				r.put("AverageX", AverageX);
//			}
//		}
//
//		double Average_Final = 0;
//		if (AverageX != -1) {
//			Average_Final = (Average + AverageX) / 2;
//			// System.out.println("result:" + Average_Final);
//
//		} else {
//			Average_Final = Average;
//			// System.out.println("result:" + Average_Final);
//
//		}
//
//		r.put("Average_Final", Average_Final);
//		if ((urlscore > 0.99) && ((loginscore > 0.87) || (Namescore > 0.8))) {
//			// System.out.println("result:" + Average_Final);
//			r.put("result", Average_Final);
//		} else if ((Average_Final >= 0.881 && count == 3
//				&& name_parts_length[0] > 1 && name_parts_length[1] > 1)
//				|| (Average_Final > 0.78 && count == 4)
//				|| (Average_Final > 0.70 && count >= 5)) {
//
//			if (themeScore > 0.5 && Namescore < 0.86) {
//				// do nothing no match
//			} else if ((Namescore < 0.4) && (urlscore <= 0.1)) {
//				// do nothing no match
//
//			} else if ((themeScore <= 0.1) && (urlscore <= 0.1)
//					&& (Namescore < 0.86)) {
//				// do nothing no match
//			} else {
//				// System.out.println("result:" + Average_Final);
//				r.put("result", Average_Final);
//
//			}
//		}
		return r;
	}

	// public static String getHomePage(String homePage) {
	// String url = homePage;
	// url = url.replaceAll("^https?://?", "").replaceAll("^www.?", "")
	// .replaceAll("/$", "");
	// // System.out.println(url);
	//
	// return url;
	// }
//	private static ArrayList<String> getHomePage(List<String> url) {
//		// TODO Auto-generated method stub
//		String[] excludes = { "^https?://?", "^www.?", "/$" };
//		// List<String> url = url1;
//		// url = url.replaceAll("^https?://?", "").replaceAll("^www.?", "")
//		// .replaceAll("/$", "");
//		// System.out.println(url);
//		ArrayList<String> url1 = new ArrayList<String>();
//		for (String skill : url) {
//			boolean f = false;
//			for (int i = 0; i < excludes.length; i++)
//				if (skill.trim().equals(excludes[i])) {
//					f = true;
//					break;
//				}
//			if (!f)
//				url1.add(skill.trim());
//		}
//
//		return url1;
//
//	}

//	public static String delimit(String sd) {
//		// System.out.println("in delimit");
//		String sd1 = null;
//		if (sd.contains("::")) {
//			sd = sd.replaceFirst("^::", "");
//			if (sd.endsWith("::")) {
//				// sd = sd.substring(0,sd.length() - 1);
//				sd = sd.substring(0, sd.lastIndexOf("::"));
//			}
//			// System.out.println("in delimit1"+sd);
//			return sd;
//		} else
//			// System.out.println("in delimit2"+sd);
//			return sd;
//	}

	// /this is the new function that needs to be implemented
//	public static boolean isValid(ArrayList<String> arr) {
//
//		if (arr == null)
//			return false;
//		if (arr.size() == 0)
//			return false;
//		if (arr.isEmpty())
//			return false;
//		return true;
//
//	}

	public static boolean isValid(String str) {

		if (str == null)
			return false;
		if (str.length() == 0)
			return false;
		if (str.isEmpty())
			return false;
		return true;

	}

	public static boolean isValid(String str1, String str2) {

		return isValid(str1) && isValid(str2);

	}

//	public static boolean isValid(ArrayList<String> str1, ArrayList<String> str2) {
//
//		return isValid(str1) && isValid(str2);
//
//	}

//	public static int listCompare(List<?> l1, List<?> l2) {
//		int flag = 0;
//
//		Iterator<?> it = l1.iterator();
//		while (it.hasNext()) {
//			String st1 = (String) it.next();
//			if (l2.contains(st1)) {
//				flag = 1;
//			}
//
//		}
//		return flag;
//
//	}

//	public static List<String> split(String s1) {
//
//		List<String> list = new ArrayList<String>();
//
//		String res[] = s1.split("::");
//		// System.out.println(res.length);
//		for (int i = 0; i < res.length; i++) {
//			// System.out.println(res[i]);
//			list.add(res[i]);
//		}
//		return list;
//	}

//	public static String removeElements(String str) {
//		String[] excludes = { "applications", "application", "development",
//				"data", "programming", "testing", "on", "string", "library",
//				"software", "url" };
//
//		for (String s : excludes) {
//			str = str.replaceAll(s, "");
//			str = str.replaceAll("::::", "::");
//		}
//		return str;
//
//	}

//	public static ArrayList<String> removeElements(Array<String> arr) {
//		String[] excludes = { "applications", "application", "development",
//				"data", "programming", "testing", "on", "string", "library",
//				"software", "", " " };
//		ArrayList<String> rt = new ArrayList<String>();
//
//		for (String skill : arr) {
//			boolean f = false;
//			for (int i = 0; i < excludes.length; i++)
//				if (skill.trim().equals(excludes[i])) {
//					f = true;
//					break;
//				}
//			if (!f)
//				rt.add(skill.trim());
//		}
//
//		return rt;
//
//	}

	public static float jaro(String x, String y) {

		int maxDistance = ((Math.min(x.length(), y.length())) / 2) + 1;
		StringBuffer mx = findMatchingChars(x, y, maxDistance);
		StringBuffer my = findMatchingChars(y, x, maxDistance);

		if (mx.length() == 0 || my.length() == 0 || mx.length() != my.length())
			return 0.0f;
		else {
			int t = 0;
			for (int i = 0; i < mx.length(); i++)
				if (mx.charAt(i) != my.charAt(i))
					t++;

			t /= 2.0f;

			float x1 = Math.abs(((float) mx.length()))
					/ Math.abs(((float) x.length()));
			float x2 = Math.abs(((float) my.length()))
					/ Math.abs(((float) y.length()));
			float x3 = Math.abs(((float) ((mx.length()) - t)))
					/ Math.abs(((float) (mx.length())));
			return ((x1 + x2 + x3) / 3);
		}
	}

	private static StringBuffer findMatchingChars(String x, String y,
			int maxDistance) {
		StringBuffer result = new StringBuffer();
		StringBuffer y_backup = new StringBuffer(y);

		for (int i = 0; i < x.length(); i++) {
			boolean found = false;
			for (int j = Math.max(0, i - maxDistance); !found
					&& j <= Math.min(i + maxDistance, y_backup.length() - 1); j++) {
				if (x.charAt(i) == y_backup.charAt(j)) {
					found = true;
					result.append(x.charAt(i));
					y_backup.setCharAt(j, '*');
				}
			}
		}

		return result;
	}

	public static double getSimname(String record1, String record2) {

		double sim1 = 0;
		double sim2 = 0;

		sim1 = jaro(record1, record2) / 100;

		sim2 = getSim2(sim1, 80);

		// System.out.println("sim2:" + sim2);
		return sim2;
	}

	public static double getSim2(double sim1, double weight) {
		double sim2;
		sim2 = ((2 * sim1 * weight) / (1 + (sim1 * weight)));
		// System.out.println("SIM2:" + sim2);

		return sim2;
	}

	public static double getSimuserlocation(String record1, String record2) {

		double sim1 = 0;
		double sim2 = 0;

		sim1 = jaro(record1, record2) / 100;

		sim2 = getSim2(sim1, 80);
		// System.out.println("sim2:" + sim2);
		return sim2;
	}

	public static double getSimstate(String record1, String record2) {

		double sim1 = 0;
		float weight;
		double sim2 = 0;
		double outputscore = 0;

		double levenscore = getLevenshteinDistance(record1, record2);

		int maxsize = Math.max(record1.length(), record2.length());

		if ((1 - Math.abs(levenscore) / maxsize) > 1) {
			outputscore = 1;
		} else {
			outputscore = (1 - Math.abs(levenscore) / maxsize);
			// System.out.println(" sim is " + outputscore);
		}

		sim1 = outputscore / 100;
		weight = 80;
		sim2 = getSim2(sim1, weight);
		// System.out.println("sim2:" + sim2);
		return sim2;
	}

	public static double getSimcountry(String record1, String record2) {

		double sim1 = 0;
		float weight;
		double sim2 = 0;
		double outputscore = 0;

		double levenscore = getLevenshteinDistance(record1, record2);
		int maxsize = Math.max(record1.length(), record2.length());

		if ((1 - Math.abs(levenscore) / maxsize) > 1) {
			outputscore = 1;
		} else {
			outputscore = (1 - Math.abs(levenscore) / maxsize);
			// System.out.println(" sim is " + outputscore);
		}

		sim1 = outputscore / 100;
		weight = 80;
		sim2 = getSim2(sim1, weight);
		// System.out.println(record1+" "+record2+"sim2: "+ sim2);
		return sim2;
	}

	public static double getThemeSim(String record1, String record2) {

		double sim = 0;

		String[] s1 = record1.toString().split("::");
		String[] s2 = record2.toString().split("::");

		int s1Count = s1.length;
		int s2Count = s2.length;
		int less_attrib_source = 0;
		double percentage;
		double rec1 = 0;
		double rec2 = 0;

		if (s1Count <= s2Count) {
			less_attrib_source = s1Count;
		} else if (s2Count <= s1Count) {
			less_attrib_source = s2Count;
		}

		percentage = (double) 1 / less_attrib_source;

		// System.out.println("percentage:theme" + percentage);
		ArrayList<String> objArray = new ArrayList<String>();
		ArrayList<String> objArray2 = new ArrayList<String>();
		int i = 0;
		for (String skill : s1) {
			objArray.add(i, skill.trim());
			i++;
		}
		int j = 0;
		for (String skill : s2) {
			objArray2.add(j, skill.trim());
			j++;
		}

		objArray.retainAll(objArray2);
		// System.out.println(objArray.size());

		sim = objArray.size() * percentage;
		// System.out.println("sim" + sim);
		rec1 = s1Count;
		rec2 = s2Count;
		double attrPercentage = 0;
		if (rec1 > rec2) {

			attrPercentage = (rec2 / rec1) * 100;
		} else {
			attrPercentage = (rec1 / rec2) * 100;

		}

		// System.out.println("attr perce=" + attrPercentage);

		if (attrPercentage > 32) {
			sim = sim;
		} else if (attrPercentage < 20) {
			sim = 0;
		} else {
			sim = sim / 2;
		}

		int weight = 80;
		sim = sim / 100;
		double sim2;
		sim2 = getSim2(sim, weight);
		return sim2;

	}

	public static double getThemeSim(ArrayList<String> themes_s1,
			ArrayList<String> themes_s2) {

		double sim = 0;

		int s1Count = themes_s1.size();
		int s2Count = themes_s2.size();

		int less_attrib_source = 0;
		double percentage;
		double rec1 = 0;
		double rec2 = 0;

		less_attrib_source = Math.min(s1Count, s2Count);

		percentage = (double) 1 / less_attrib_source;

		themes_s1.retainAll(themes_s2); // System.out.println(objArray.size());

		sim = themes_s1.size() * percentage; // System.out.println("sim" + sim);
		rec1 = s1Count;
		rec2 = s2Count;

		double attrPercentage = ((Math.min(rec1, rec2) / Math.max(rec1, rec2)) * 100);

		// System.out.println("attr perce=" + attrPercentage);

		if (attrPercentage > 32) {
			// sim = sim;
			// do nothing
		} else if (attrPercentage < 20) {
			sim = 0;
		} else {
			sim = sim / 2;
		}

		int weight = 80;
		sim = sim / 100;
		double sim2;
		sim2 = getSim2(sim, weight);
		return sim2;

	}

	public static int getLevenshteinDistance(String s, String t) {
		if (s == null || t == null) {
			throw new IllegalArgumentException("Strings must not be null");
		}

		int n = s.length(); // length of s
		int m = t.length(); // length of t

		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}

		if (n > m) {
			// swap the input strings to consume less memory
			String tmp = s;
			s = t;
			t = tmp;
			n = m;
			m = t.length();
		}

		int p[] = new int[n + 1]; // 'previous' cost array, horizontally
		int d[] = new int[n + 1]; // cost array, horizontally
		int _d[]; // placeholder to assist in swapping p and d

		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t

		char t_j; // jth character of t

		int cost; // cost

		for (i = 0; i <= n; i++) {
			p[i] = i;
		}

		for (j = 1; j <= m; j++) {
			t_j = t.charAt(j - 1);
			d[0] = j;

			for (i = 1; i <= n; i++) {
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// minimum of cell to the left+1, to the top+1, diagonally
				// left and up +cost
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
						+ cost);
			}

			// copy current distance counts to 'previous row' distance
			// counts
			_d = p;
			p = d;
			d = _d;
		}

		// our last action in the above loop was to switch d and p, so p now
		// actually has the most recent cost counts
		return p[n];
	}
}