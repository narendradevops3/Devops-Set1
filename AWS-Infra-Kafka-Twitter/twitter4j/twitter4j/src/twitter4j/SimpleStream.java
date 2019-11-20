package twitter4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONValue;

import twitter4j.conf.ConfigurationBuilder;

public class SimpleStream {
	static int count = 0;

	public static void main(String[] args) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey("XOQWMOZYMgXMUM42NH5oAa9CG");
		cb.setOAuthConsumerSecret("jPm9uwEyfTynXb6OXx09bOnWH6tZbba6id6TJdI514qdouyde1");
		cb.setOAuthAccessToken("125330668-bIr2XldEZThubAcjrOkl0bXvGKRR6T9WmNKJl2iC");
		cb.setOAuthAccessTokenSecret("OtaifRIdbTnRaJsT2CnXQ24on5d2Is3JvxKmxb1uKHeDK");

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();

		StatusListener listener = new StatusListener() {

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatus(Status status) {
				User user = status.getUser();

				// gets Username
				String username = status.getUser().getScreenName();
				System.out.println(username);
				String profileLocation = user.getLocation();
				System.out.println(profileLocation);
				long tweetId = status.getId();
				System.out.println(tweetId);
				String content = status.getText();

				Map obj = new LinkedHashMap();
				obj.put("username", username);
				obj.put("profileLocation", profileLocation);
				obj.put("tweetId", tweetId);
				obj.put("content", content);
				StringWriter out = new StringWriter();
				try {
					JSONValue.writeJSONString(obj, out);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String jsonText = out.toString();
				System.out.print(jsonText);

				String path = "/home/ec2-user/tweet.json";
				File file = new File(path);
				FileWriter fileWriter = null;
				try {
					fileWriter = new FileWriter(file, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
				try {
					fileWriter.append(jsonText + "\n");
					bufferFileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Done");

				if (content != null) {
					count = count + 1;
					System.out.println("count:" + count);
				}
				System.out.println(content + "\n");

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};
		FilterQuery fq = new FilterQuery();

		String keywords[] = { "#jobs", "#sales", "#job", "#hiring" };

		fq.track(keywords);

		twitterStream.addListener(listener);
		twitterStream.filter(fq);

	}
}