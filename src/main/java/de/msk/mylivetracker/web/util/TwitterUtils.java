package de.msk.mylivetracker.web.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.util.datetime.DateTimeUtils;

/**
 * TwitterUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TwitterUtils {

	private static final Log log = LogFactory.getLog(TwitterUtils.class);
	
	public static final class TwitterMessage {
		private String timestamp;
		private String message;
		
		public TwitterMessage(String timestamp, String message) {
			this.timestamp = timestamp;
			this.message = message;
		}
		public String getTimestamp() {
			return timestamp;
		}
		public String getMessage() {
			return message;
		}
	}
	
	public static List<TwitterMessage> getTwitterMessages() {
		List<TwitterMessage> twitterMessages = new ArrayList<TwitterMessage>();
		try {
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			 	.setOAuthConsumerKey("lDBEGegKMuR8ImVdvFKsA")
			 	.setOAuthConsumerSecret("NngUV1pWZCnNdH5Na09XrPPb9BBrs2SzxJ9plSuq4")
			 	.setOAuthAccessToken("264289890-KLNzWlahmzlQ7BioSjcw0QEYFJ1qK2O0cWq0LO1j")
			 	.setOAuthAccessTokenSecret("jhLeTQaQa1MBPSn8xyq60mvVJBD19Na0abwFZFPZao");
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();
			List<Status> statusList = twitter.getHomeTimeline();
			if (statusList != null) {
				for (int i=0; (i < 3) && (i < statusList.size()); i++) {
					DateTime dateTime = new DateTime(
						statusList.get(i).getCreatedAt().getTime());
					twitterMessages.add(new TwitterMessage(
						DateTimeUtils.getDateTimeStrDe4UserRep(dateTime),
						statusList.get(i).getText()));
				}
			}
		} catch (Exception e) {
			log.fatal(e);
		}
		return twitterMessages;
	}
}
