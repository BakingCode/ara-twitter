package com.bakingcode.io.twitter;

import com.bakingcode.io.twitter.TwitterHttpClient.Request;
import com.bakingcode.io.twitter.exceptions.TwitterErrorRequestException;
import com.bakingcode.io.twitter.exceptions.TwitterException;
import com.bakingcode.io.twitter.model.ApiConfiguration;
import com.bakingcode.io.twitter.model.ApiResourceFamily;
import com.bakingcode.io.twitter.model.Friendship;
import com.bakingcode.io.twitter.model.IdentifierList;
import com.bakingcode.io.twitter.model.ResponseString;
import com.bakingcode.io.twitter.model.Trend;
import com.bakingcode.io.twitter.model.TrendPlace;
import com.bakingcode.io.twitter.model.Tweet;
import com.bakingcode.io.twitter.model.User;
import com.bakingcode.io.twitter.tools.Tools;
import oauth.signpost.OAuthConsumer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bakingcode.io.twitter.tools.TwitterLogging.L;
import static com.bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * Twitter API 1.1 consumer. You need to initialize with TwitterOAuth and when you get the token and tokenSecret
 * initialize this Twitter consumer class. 
 */
public class Twitter {

	/**
	 * Logging tag
	 */
	private final static String TAG = "TwitterAPI1_1";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Error messages
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Parameter required format
	 */
	private final static String PARAMETER_REQUIRED = "%s parameter is required, cannot be null";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Private members
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Twitter Http client for sing with oauth all the request and auto-generate request.
	 */
	private TwitterHttpClient twitterHttpClient;
	
	/**
	 * Creates a twitter consumer by all the secrets
	 * 
	 * @param consumerKey consumer key
	 * @param consumerSecret consumer secret
	 * @param token token
	 * @param tokenSecret token secret
	 */
	public Twitter(String consumerKey, String consumerSecret, String token, String tokenSecret) {

		twitterHttpClient = new TwitterHttpClient(consumerKey, consumerSecret, token, tokenSecret);
		
	}

    /**
     * Get ouath consumer
     * @return oauth consumer
     */
    public OAuthConsumer getOuathConsumer() {
        return twitterHttpClient.getConsumer();
    }
	
	/**
	 * @return the twitterHttpClient
	 */
	public TwitterHttpClient getTwitterHttpClient() {
		return twitterHttpClient;
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Petitions
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Makes a search query to twitter, for more information look here: 
	 * https://dev.twitter.com/docs/api/1.1/get/search/tweets
	 * 
	 * @param latitude latitude for search tweets 
	 * @param longitude longitude for search tweets
	 * @param radius the radius for search tweets in this format: "1km"/"1mi"
	 * @param count the count of tweets to retrieve, generally used only for first search
	 * @param sinceId the since id for search. Read more in <a href="https://dev.twitter.com/docs/working-with-timelines">twitter doc</a>
	 * @param maxId the max id for search. Read more in <a href="https://dev.twitter.com/docs/working-with-timelines">twitter doc</a>
	 * @return List<Tweet> a list of tweets searched
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public List<Tweet> search(String query, Double latitude, Double longitude, String radius, Integer count,  Long sinceId, Long maxId) throws TwitterErrorRequestException {
		
		L(TAG, "Search method");
		
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		
		if (Tools.isNotEmpty(query)) {
			nameValuePair.add(Parameters.QUERY.valuePairWith(query));
		}
		
		// Make geocode
		if (latitude != null && longitude != null && Tools.isNotEmpty(radius)) {
			
			String geocode = String.format("%s,%s,%s", Double.toString(latitude), Double.toString(longitude), radius);
			nameValuePair.add(Parameters.GEOCODE.valuePairWith(geocode));
			
		}
		
		if (count != null) {
			nameValuePair.add(Parameters.COUNT.valuePairWith(Integer.toString(count)));
		}
		
		if (sinceId != null) {
			nameValuePair.add(Parameters.SINCE_ID.valuePairWith(Long.toString(sinceId)));
		}

		if (maxId != null) {
			nameValuePair.add(Parameters.MAX_ID.valuePairWith(Long.toString(maxId)));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_SEARCH, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				L(TAG, "Init parsing search:");

				JSONObject statusesObject = new JSONObject(response.getResponseString());
				JSONArray arrJson = statusesObject.getJSONArray("statuses");
				List<Tweet> listTweets = new ArrayList<Tweet>();

				for (int i=0 ; i<arrJson.length(); i++) {

					JSONObject jsonObject = arrJson.getJSONObject(i);
					Tweet t = new Tweet(jsonObject);
					listTweets.add(t);

				}

				L(TAG, "Finish parsing search item count:"  + listTweets.size());

				return listTweets;

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;

	}

	/**
	 * Returns fully-hydrated user objects for up to 100 users per request, as specified by comma-separated values passed to the user_id and/or screen_name parameters.
	 * This method is especially useful when used in conjunction with collections of user IDs returned from GET friends/ids and GET followers/ids.
	 * GET users/show is used to retrieve a single user object.
	 *
	 * @param screenNames A list of users to do the lookup. A MAX OF 100 ARE ALLOWED BY REQUEST
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return List<User> a list of users looked up
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<User>lookupUsers(List<String> screenNames, boolean includeEntities) throws TwitterErrorRequestException {

		// Check parameters
		if (Tools.isEmpty(screenNames)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "screenNames"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		String joinedNames = Tools.join(screenNames.toArray(), ',');
		nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(joinedNames));
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_USER_LOOKUP, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				List<User> listUsers = new ArrayList<User>();

				L(TAG, "Init parsing lookup:");

				JSONArray arrJson = new JSONArray(response.getResponseString());
				for (int i=0 ; i<arrJson.length(); i++) {

					JSONObject jsonObject = arrJson.getJSONObject(i);
					User u = new User(jsonObject);
					listUsers.add(u);

				}

				L(TAG, "Finish parsing lookup:");

				return listUsers;

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;

	}

	/**
	 * Returns fully-hydrated user objects for up to 100 users per request, as specified by comma-separated values passed to the user_id and/or screen_name parameters.
	 * This method is especially useful when used in conjunction with collections of user IDs returned from GET friends/ids and GET followers/ids.
	 * GET users/show is used to retrieve a single user object.
	 *
	 * @param ids A array of user ids to do the lookup. A MAX OF 100 ARE ALLOWED BY REQUEST
	 * @return list of Twitter users
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public List<User>lookupUsers(long[] ids) throws TwitterErrorRequestException {

		// Check parameters
		if (ids == null || ids.length == 0) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "ids"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		String joinedNames = Tools.join(ids, ',');
		nameValuePair.add(Parameters.USER_ID.valuePairWith(joinedNames));
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.FALSE.toString()));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_USER_LOOKUP, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				List<User> listUsers = new ArrayList<User>();

				L(TAG, "Init parsing lookup:");

				JSONArray arrJson = new JSONArray(response.getResponseString());
				for (int i=0 ; i<arrJson.length(); i++) {

					JSONObject jsonObject = arrJson.getJSONObject(i);
					User u = new User(jsonObject);
					listUsers.add(u);

				}

				L(TAG, "Finish parsing lookup:");

				return listUsers;

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;

	}

	/**
	 * Returns a variety of information about the user specified by the required user_id or screen_name parameter. The author's most recent Tweet will be returned inline when possible.
	 *
	 * @param screenName the user screen name
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return a User object
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public User showUser(String screenName, Long twitterid, boolean includeEntities) throws TwitterErrorRequestException {

		if (Tools.isEmpty(screenName) && twitterid == null) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "screenName or twitterId"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        if (Tools.isNotEmpty(screenName)) {
		    nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(screenName));
        }

        if (twitterid != null) {
            nameValuePair.add(Parameters.USER_ID.valuePairWith(Long.toString(twitterid)));
        }

		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_USER_SHOW.getUrl(), nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return User.parse(response.getResponseString());

		}

		return null;

	}

	/**
	 * Returns the current configuration used by Twitter including twitter.com slugs which are not usernames, maximum photo resolutions, and t.co URL lengths.
	 * It is recommended applications request this endpoint when they are loaded, but no more than once a day.
	 *
	 * @return A new ApiConfiguration Object. This Object is SERIALIZABLE, you can save it.
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public ApiConfiguration getApiConfiguration() throws TwitterErrorRequestException {

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_API_CONFIGURATION, null);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				JSONObject jsonResponse = new JSONObject(response.getResponseString());
                return new ApiConfiguration(jsonResponse);

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;

	}

	/**
	 * Returns a single Tweet, specified by the id parameter. The Tweet's author will also be embedded within the tweet.
	 *
	 * @param id the tweet id
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return a tweet object
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public Tweet showStatus(long id, boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.ID.valuePairWith(Long.toString(id)));
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_STATUSES_SHOW, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parse(response.getResponseString());

		}

		return null;
	}

	/**
	 * Returns an HTTP 200 OK response code and a representation of the requesting user if authentication was successful; returns a 401 status code and an error message if not. Use this method to test if supplied user credentials are valid.
	 *
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return a User representation of the requesting user
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public User verifyCredentials(boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));
		nameValuePair.add(Parameters.SKIP_STATUS.valuePairWith(Boolean.TRUE.toString()));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_VERIFY_CREDENTIALS, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return User.parse(response.getResponseString());

		}

		return null;

	}
	/**
	 * TODO: Untested!
	 *
	 * Returns a list of the resource families and the rate limit status information,
	 * for all their methods. For more information look here:
	 * https://dev.twitter.com/docs/api/1.1/get/application/rate_limit_status
	 *
	 * @param resourceFamilies List of resource families.
	 * @return List<ApiResourceFamily> A list of the resource families.
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<ApiResourceFamily> rateLimitStatus(List<String> resourceFamilies) throws TwitterErrorRequestException {

		L(TAG, "rateLimitStatus method");

		if (Tools.isEmpty(resourceFamilies)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "resourceFamilies"));
		}

		// Append all the families in one string
		StringBuilder resources = new StringBuilder();
		for (String res : resourceFamilies) {
			resources.append(res).append(",");
		}

		// Remove the comma if the buffer has data
		int length = resources.length();
		if (length > 0) {
			resources.deleteCharAt(length-1);
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.RESOURCES.valuePairWith(resources.toString()));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_RATE_LIMIT_STATUS, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return ApiResourceFamily.parseList(response.getResponseString(), resourceFamilies);

		}

		return null;

	}

	// ///////////////////////////////////////////////////////////////////////////
	// Timelines
	// ///////////////////////////////////////////////////////////////////////////

    /**
     * Returns a collection of the most recent Tweets and retweets posted by the authenticating user and the users they follow. The home timeline is central to how most users interact with the Twitter service.
     * Up to 800 Tweets are obtainable on the home timeline. It is more volatile for users that follow many users or follow users who tweet frequently.
     *
     * @param count Specifies the number of records to retrieve. Must be less than or equal to 200. Defaults to 20.
     * @param sinceId Returns results with an ID greater than (that is, more recent than) the specified ID. There are limits to the number of Tweets which can be accessed through the API. If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
     * @param maxId Returns results with an ID less than (that is, older than) or equal to the specified ID.
     * @param includeEntities True if you want to include parsing of twitter entities false otherwise
     * @return a list with tweets
     * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
     */
    public List<Tweet> retweetsOfMe(Integer count, Long sinceId, Long maxId, boolean includeEntities) throws TwitterErrorRequestException {

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

        if (count != null) {
            nameValuePair.add(Parameters.COUNT.valuePairWith(count.toString()));
        }

        if (sinceId != null) {
            nameValuePair.add(Parameters.SINCE_ID.valuePairWith(sinceId.toString()));
        }

        if (maxId != null) {
            nameValuePair.add(Parameters.MAX_ID.valuePairWith(maxId.toString()));
        }

        ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_RETWEETS_OF_ME, nameValuePair);

        if (response.getError() != null) {

            throw new TwitterErrorRequestException(response.getError());

        } else if (response.isNotEmpty()) {

            return Tweet.parseList(response.getResponseString());

        }

        return null;
    }

	/**
	 * Returns a collection of the most recent Tweets and retweets posted by the authenticating user and the users they follow. The home timeline is central to how most users interact with the Twitter service.
	 * Up to 800 Tweets are obtainable on the home timeline. It is more volatile for users that follow many users or follow users who tweet frequently.
	 *
	 * @param count Specifies the number of records to retrieve. Must be less than or equal to 200. Defaults to 20.
	 * @param sinceId Returns results with an ID greater than (that is, more recent than) the specified ID. There are limits to the number of Tweets which can be accessed through the API. If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
	 * @param maxId Returns results with an ID less than (that is, older than) or equal to the specified ID.
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return a list with tweets
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<Tweet> homeTimeline(Integer count, Long sinceId, Long maxId, boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

		if (count != null) {
			nameValuePair.add(Parameters.COUNT.valuePairWith(count.toString()));
		}

		if (sinceId != null) {
			nameValuePair.add(Parameters.SINCE_ID.valuePairWith(sinceId.toString()));
		}

		if (maxId != null) {
			nameValuePair.add(Parameters.MAX_ID.valuePairWith(maxId.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_HOME_TIMELINE, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parseList(response.getResponseString());

		}

		return null;
	}

	/**
	 * Returns a collection of the most recent Tweets posted by the user indicated by the screen_name or user_id parameters.
	 * User timelines belonging to protected users may only be requested when the authenticated user either "owns" the timeline or is an approved follower of the owner.
	 * The timeline returned is the equivalent of the one seen when you view a user's profile on twitter.com.
	 * This method can only return up to 3,200 of a user's most recent Tweets. Native retweets of other statuses by the user is included in this total, regardless of whether include_rts is set to false when requesting this resource.
	 *
	 * @param userId The ID of the user for whom to return results for. Helpful for disambiguating when a valid user ID is also a valid screen name.
	 * @param screenName The screen name of the user for whom to return results for. Helpful for disambiguating when a valid screen name is also a user ID.
	 * @param count Specifies the number of records to retrieve. Must be less than or equal to 200. Defaults to 20.
	 * @param sinceId Returns results with an ID greater than (that is, more recent than) the specified ID. There are limits to the number of Tweets which can be accessed through the API. If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
	 * @param maxId Returns results with an ID less than (that is, older than) or equal to the specified ID.
	 * @param exludeReplies This parameter will prevent replies from appearing in the returned timeline. Using exclude_replies with the count parameter will mean you will receive up-to count tweets ��� this is because the count parameter retrieves that many tweets before filtering out retweets and replies. This parameter is only supported for JSON and XML responses.
	 * @param includeRts When set to false, the timeline will strip any native retweets (though they will still count toward both the maximal length of the timeline and the slice selected by the count parameter). Note: If you're using the trim_user parameter in conjunction with include_rts, the retweets will still contain a full user object.
	 * @return a list with tweets
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<Tweet> userTimeline(Long userId, String screenName, Integer count, Long sinceId, Long maxId, Boolean exludeReplies, Boolean includeRts) throws TwitterErrorRequestException {

		// Check params
		if (userId == null && Tools.isEmpty(screenName)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "userId or screenName required"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        if (userId != null) {
            nameValuePair.add(Parameters.USER_ID.valuePairWith(userId.toString()));
        }

		if (count != null) {
			nameValuePair.add(Parameters.COUNT.valuePairWith(count.toString()));
		}

		if (sinceId != null) {
			nameValuePair.add(Parameters.SINCE_ID.valuePairWith(sinceId.toString()));
		}

		if (maxId != null) {
			nameValuePair.add(Parameters.MAX_ID.valuePairWith(maxId.toString()));
		}

		if (exludeReplies != null) {
			nameValuePair.add(Parameters.EXCLUDE_REPLIES.valuePairWith(exludeReplies.toString()));
		}

		if (includeRts != null) {
			nameValuePair.add(Parameters.INCLUDE_RTS.valuePairWith(includeRts.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_USER_TIMELINE, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parseList(response.getResponseString());

		}

		return null;

	}

	/**
	 * Returns the 20 most recent mentions (status containing @username) for the authenticating user.
	 * The timeline returned is the equivalent of the one seen when you view your mentions on twitter.com.
	 * This method can only return up to 800 statuses.
	 * This method will include retweets in the JSON response regardless of whether the include_rts parameter is set.
	 *
	 * @param count Specifies the number of records to retrieve. Must be less than or equal to 200. Defaults to 20.
	 * @param sinceId Returns results with an ID greater than (that is, more recent than) the specified ID. There are limits to the number of Tweets which can be accessed through the API. If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
	 * @param maxId Returns results with an ID less than (that is, older than) or equal to the specified ID.
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return a list with tweets
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public List<Tweet> mentionsTimeline(Integer count, Long sinceId, Long maxId, boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

		if (count != null) {
			nameValuePair.add(Parameters.COUNT.valuePairWith(count.toString()));
		}

		if (sinceId != null) {
			nameValuePair.add(Parameters.SINCE_ID.valuePairWith(sinceId.toString()));
		}

		if (maxId != null) {
			nameValuePair.add(Parameters.MAX_ID.valuePairWith(maxId.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_MENTIONS_TIMELINE, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parseList(response.getResponseString());

		}

		return null;
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Tweets
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Retweets a tweet. Returns the original tweet with retweet details embedded.
	 *
	 * @param id the id of tweet to retweet
	 * @return Tweet a tweet retweeted
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public Tweet retweet(long id) throws TwitterErrorRequestException {

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, String.format(Urls.URL_RETWEET.getUrl(), Long.toString(id)) , null);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parse(response.getResponseString());

		}

		return null;

	}

	/**
	 * First retweets a tweet. Returns up to 100 of the first retweets of a given tweet.
	 *
	 * @param id the id of tweet to retweet
	 * @return a list with the first retweets for the given tweet id
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public List<Tweet> firstRetweets(long id) throws TwitterErrorRequestException {

		ResponseString response = twitterHttpClient.makeRequestWithParameters(
				Request.GET, String.format(Urls.URL_RETWEETS.getUrl(), Long.toString(id)), null);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parseList(response.getResponseString());

		}

		return null;

	}

	/**
	 * Updates the authenticating user's current status, also known as tweeting. To upload an image to accompany the tweet, use POST statuses/update_with_media.
	 * For each update attempt, the update text is compared with the authenticating user's recent tweets. Any attempt that would result in duplication will be blocked, resulting in a 403 error.
	 * Therefore, a user cannot submit the same status twice in a row. While not rate limited by the API a user is limited in the number of tweets they can create at a time. If the number of
	 * updates posted by the user reaches the current allowed limit this method will return an HTTP 403 error.
	 *
	 * @param update update
	 * @return Tweet a tweet update
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public Tweet update(String update) throws TwitterErrorRequestException {
		return update(update, null, null, null, Boolean.FALSE);
	}

	/**
	 * Updates the authenticating user's current status, also known as tweeting. To upload an image to accompany the tweet, use POST statuses/update_with_media.
	 * For each update attempt, the update text is compared with the authenticating user's recent tweets. Any attempt that would result in duplication will be blocked, resulting in a 403 error.
	 * Therefore, a user cannot submit the same status twice in a row. While not rate limited by the API a user is limited in the number of tweets they can create at a time. If the number of
	 * updates posted by the user reaches the current allowed limit this method will return an HTTP 403 error.
	 *
	 * @param update String with status to update
	 * @param inReplyToStatusId if is a reply of other tweet, send the other tweet id
	 * @param lat latitude of tweet
	 * @param lon longitude of tweet
	 * @param displayCoordinates True if want to displau coordinates
	 * @return the uploaded tweet
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public Tweet update(String update, Long inReplyToStatusId, Double lat, Double lon, Boolean displayCoordinates) throws TwitterErrorRequestException {

		L(TAG, "Post Update");

		// Check required parameters
		if (Tools.isEmpty(update)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "update"));
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(Parameters.STATUS.valuePairWith(update));

        if (inReplyToStatusId != null) {
        	nameValuePairs.add(Parameters.IN_REPLY_TO_STATUS_ID.valuePairWith(inReplyToStatusId.toString()));
        }

        if (lat != null) {
        	nameValuePairs.add(Parameters.LATITUDE.valuePairWith(lat.toString()));
        }

        if (lon != null) {
        	nameValuePairs.add(Parameters.LONGITUDE.valuePairWith(lon.toString()));
        }

        if (displayCoordinates != null && displayCoordinates) {
        	nameValuePairs.add(Parameters.DISPLAY_COORDINATES.valuePairWith(Boolean.TRUE.toString()));
        }

        ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_STATUSES_UPDATE, nameValuePairs);

        if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parse(response.getResponseString());

		}

        return null;
	}

	/**
	 * Updates the authenticating user's current status and attaches media for upload. In other words, it creates a Tweet with a picture attached.
	 * Unlike POST statuses/update, this method expects raw multipart data. Your POST request's Content-Type should be set to multipart/form-data with the media[] parameter
	 * The Tweet text will be rewritten to include the media URL(s), which will reduce the number of characters allowed in the Tweet text. If the URL(s) cannot be appended without text truncation, the tweet will be rejected and this method will return an HTTP 403 error.
	 *
	 * @param status String with status to update
	 * @param fImg File with image to upload
	 * @param inReplyToStatusId if is a reply of other tweet, send the other tweet id
	 * @param lat latitude of tweet
	 * @param lon longitude of tweet
	 * @param displayCoordinates True if want to displau coordinates
	 * @return the uploaded tweet
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public Tweet updateWithMedia(String status, File fImg, Long inReplyToStatusId, Double lat, Double lon, Boolean displayCoordinates) throws TwitterErrorRequestException {

		// Check parameters
		if (Tools.isEmpty(status)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "status"));
		}

		if (fImg == null || !fImg.exists() || !fImg.canRead()) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "file media"));
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(Parameters.STATUS.valuePairWith(status));

        if (inReplyToStatusId != null) {
        	nameValuePairs.add(Parameters.IN_REPLY_TO_STATUS_ID.valuePairWith(inReplyToStatusId.toString()));
        }

        if (lat != null) {
        	nameValuePairs.add(Parameters.LATITUDE.valuePairWith(lat.toString()));
        }

        if (lon != null) {
        	nameValuePairs.add(Parameters.LONGITUDE.valuePairWith(lon.toString()));
        }

        if (displayCoordinates != null && displayCoordinates) {
        	nameValuePairs.add(Parameters.DISPLAY_COORDINATES.valuePairWith(Boolean.TRUE.toString()));
        }

        ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST_WITH_MEDIA, Urls.URL_UPDATE_WITH_MEDIA.getUrl(), nameValuePairs, null, fImg);

        if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parse(response.getResponseString());

		}

        return null;

	}

	// ///////////////////////////////////////////////////////////////////////////
	// Favorites
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the 20 most recent Tweets favorited by the authenticating or specified user.
	 *
     * @param userId user id
	 * @param count Specifies the number of records to retrieve. Must be less than or equal to 200. Defaults to 20.
	 * @param sinceId Returns results with an ID greater than (that is, more recent than) the specified ID. There are limits to the number of Tweets which can be accessed through the API. If the limit of Tweets has occured since the since_id, the since_id will be forced to the oldest ID available.
	 * @param maxId Returns results with an ID less than (that is, older than) or equal to the specified ID.
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @return a list with tweets
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<Tweet> favoritesList(Long userId, String screenName, Integer count, Long sinceId, Long maxId, boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));

        if (userId != null) {
            nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(userId.toString()));
        }

		if (screenName != null) {
			nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(screenName));
		}

		if (count != null) {
			nameValuePair.add(Parameters.COUNT.valuePairWith(count.toString()));
		}

		if (sinceId != null) {
			nameValuePair.add(Parameters.SINCE_ID.valuePairWith(sinceId.toString()));
		}

		if (maxId != null) {
			nameValuePair.add(Parameters.MAX_ID.valuePairWith(maxId.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_FAVORITES_LIST, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				L(TAG, "Init parsing favoritesList:");

				JSONArray arrJson = new JSONArray(response.getResponseString());
				List<Tweet> listTweets = new ArrayList<Tweet>();

				for (int i=0 ; i<arrJson.length(); i++) {

					JSONObject jsonObject = arrJson.getJSONObject(i);
					Tweet t = new Tweet(jsonObject);
					listTweets.add(t);

				}

				L(TAG, "Finish parsing favoritesList:");

				return listTweets;

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;
	}

	/**
	 * Un-favorites the status specified in the ID parameter as the authenticating user. Returns the un-favorited status in the requested format when successful.
	 * This process invoked by this method is asynchronous. The immediately returned status may not indicate the resultant favorited status of the tweet. A 200 OK response from this method will indicate whether the intended action was successful or not.
	 *
	 * @param id the id of tweet to un-favorite
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public Tweet destroyFavorite(long id, boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));
		nameValuePair.add(Parameters.ID.valuePairWith(Long.toString(id)));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_DESTROY_FAVORITE, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parse(response.getResponseString());

		}

		return null;

	}

	/**
	 * Favorites the status specified in the ID parameter as the authenticating user. Returns the favorite status when successful.
	 * This process invoked by this method is asynchronous. The immediately returned status may not indicate the resultant favorited status of the tweet. A 200 OK response from this method will indicate whether the intended action was successful or not.
	 *
	 * @param id the id of tweet to favorite
	 * @param includeEntities True if you want to include parsing of twitter entities false otherwise
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public Tweet createFavorite(long id, boolean includeEntities) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.INCLUDE_ENTITIES.valuePairWith(Boolean.toString(includeEntities)));
		nameValuePair.add(Parameters.ID.valuePairWith(Long.toString(id)));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_CREATE_FAVORITE, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Tweet.parse(response.getResponseString());

		}

		return null;

	}

	// ///////////////////////////////////////////////////////////////////////////
	// Friends & followers
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns a cursored collection of user IDs for every user the specified user is following (otherwise known as their "friends").
	 * At this time, results are ordered with the most recent following first ��� however, this ordering is subject to unannounced change and eventual consistency issues.
	 * Results are given in groups of 5,000 user IDs and multiple "pages" of results can be navigated through using the next_cursor value in subsequent requests.
	 * See Using cursors to navigate collections for more information.
	 *
	 * @param userId the user whose friends are requested. If empty, then screenName must be informed.
	 * @param screenName the user name whose friends are requested. If empty, then userId must be informed
	 * @param cursor The next cursor for a cursored query. If null, cursor = -1 is the default.
	 * @param stringifyIds: true if the ids are retrieved as strings, false or null if ids are retrieved as longs
	 * @return the list of identifiers of the friends of the user
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public IdentifierList getFriendIds(Long userId, String screenName, Long cursor, Boolean stringifyIds) throws TwitterErrorRequestException {

		// Check parameters: userId and screenName cannot be both null at the same time
		if ((userId == null) && (Tools.isEmpty(screenName))) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "userId or screenName"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		if (userId != null) {
			nameValuePair.add(Parameters.USER_ID.valuePairWith(userId.toString()));
		}

		if (!Tools.isEmpty(screenName)) {
			nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(screenName));
		}

		if (cursor != null) {
			nameValuePair.add(Parameters.CURSOR.valuePairWith(cursor.toString()));
		}

		if (stringifyIds != null) {
			nameValuePair.add(Parameters.STRINGIFY_IDS.valuePairWith(stringifyIds.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_GET_FRIENDS, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				JSONObject jsonResponse = new JSONObject(response.getResponseString());
				return new IdentifierList(jsonResponse);

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;
	}

	/**
	 * Returns the relationships of the authenticating user to the comma-separated list of up to 100 screen_names or user_ids provided.
	 *  Values for connections can be: following, following_requested, followed_by, none.
	 *
	 * @param userIds A comma separated list of user IDs, up to 100 are allowed in a single request.
	 * @param screenNames A comma separated list of screen names, up to 100 are allowed in a single request.
	 * @return A list of friendships for the logged user
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<Friendship> lookupFriendships(long[] userIds, List<String> screenNames) throws TwitterErrorRequestException {

		// Check parameters: userId and screenName cannot be both null at the same time
		if ((userIds == null || userIds.length == 0) && (Tools.isEmpty(screenNames))) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "userId or screenName"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		if (userIds != null) {
			nameValuePair.add(Parameters.USER_ID.valuePairWith(Tools.join(userIds, ',')));
		}

		if (!Tools.isEmpty(screenNames)) {
			nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(Tools.join(screenNames.toArray(), ',')));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_LOOKUP_FRIENDSHIPS, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Friendship.parseList(response.getResponseString());

		}

		return null;

	}

	/**
	 * Returns a cursored collection of user IDs for every user following the specified user.
	 * At this time, results are ordered with the most recent following first ��� however, this ordering is subject to unannounced change and eventual consistency issues.
	 * Results are given in groups of 5,000 user IDs and multiple "pages" of results can be navigated through using the next_cursor value in subsequent requests.
	 * See Using cursors to navigate collections for more information.
	 *
	 * @param userId the user whose friends are requested. If empty, then screenName must be informed.
	 * @param screenName the user name whose friends are requested. If empty, then userId must be informed
	 * @param cursor The next cursor for a cursored query. If null, cursor = -1 is the default.
	 * @param stringifyIds: true if the ids are retrieved as strings, false or null if ids are retrieved as longs
	 * @return the list of identifiers of the friends of the user
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public IdentifierList getFollowersIds(Long userId, String screenName, Long cursor, Boolean stringifyIds) throws TwitterErrorRequestException {

		// Check parameters: userId and screenName cannot be both null at the same time
		if ((userId == null) && (Tools.isEmpty(screenName))) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "userId or screenName"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		if (userId != null) {
			nameValuePair.add(Parameters.USER_ID.valuePairWith(userId.toString()));
		}

		if (!Tools.isEmpty(screenName)) {
			nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(screenName));
		}

		if (cursor != null) {
			nameValuePair.add(Parameters.CURSOR.valuePairWith(cursor.toString()));
		}

		if (stringifyIds != null) {
			nameValuePair.add(Parameters.STRINGIFY_IDS.valuePairWith(stringifyIds.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_GET_FOLLOWERS, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			try {

				JSONObject jsonResponse = new JSONObject(response.getResponseString());
				return new IdentifierList(jsonResponse);

			} catch (JSONException e) {
				LT(TAG, e);
			}

		}

		return null;
	}

	/**
	 * Allows the authenticating users to follow the user specified in the ID parameter.
	 * Returns the befriended user in the requested format when successful. Returns a string describing the failure condition when unsuccessful. If you are already friends with the user a HTTP 403 may be returned, though for performance reasons you may get a 200 OK message even if the friendship already exists.
	 * Actions taken in this method are asynchronous and changes will be eventually consistent.
	 *
	 * @param userId The screen name of the user for whom to befriend.
	 * @param screenName The ID of the user for whom to befriend.
	 * @param follow Enable notifications for the target user.
	 * @return the user who follows profile
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public User createFriendship(Long userId, String screenName, Boolean follow) throws TwitterErrorRequestException {

		// Check required parameters
		if (userId == null && Tools.isEmpty(screenName)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "userId or screenName"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		if (userId != null) {
			nameValuePair.add(Parameters.USER_ID.valuePairWith(userId.toString()));
		}

		if (!Tools.isEmpty(screenName)) {
			nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(screenName));
		}

		if (follow != null) {
			nameValuePair.add(Parameters.FOLLOW.valuePairWith(follow.toString()));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_CREATE_FRIENDSHIP, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return User.parse(response.getResponseString());

		}
		return null;

	}

	/**
	 * Allows the authenticating user to unfollow the user specified in the ID parameter.
	 * Returns the unfollowed user in the requested format when successful. Returns a string describing the failure condition when unsuccessful.
	 * Actions taken in this method are asynchronous and changes will be eventually consistent.
	 *
	 * @param userId The screen name of the user for whom to unfollow.
	 * @param screenName The ID of the user for whom to unfollow.
	 * @return the user who unfollows profile
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public User destroyFriendship(Long userId, String screenName) throws TwitterErrorRequestException {

		// Check required parameters
		if (userId == null && Tools.isEmpty(screenName)) {
			throw new TwitterException(String.format(PARAMETER_REQUIRED, "userId or screenName"));
		}

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		if (userId != null) {
			nameValuePair.add(Parameters.USER_ID.valuePairWith(userId.toString()));
		}

		if (!Tools.isEmpty(screenName)) {
			nameValuePair.add(Parameters.SCREEN_NAME.valuePairWith(screenName));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_DESTROY_FRIENDSHIP, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return User.parse(response.getResponseString());

		}

		return null;

	}

	// ///////////////////////////////////////////////////////////////////////////
	// Trends
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the locations that Twitter has trending topic information for, closest to a specified location.
	 * The response is an array of "locations" that encode the location's WOEID and some other human-readable information such as a canonical name and country the location belongs in.
	 *
	 * @param lon If provided with a long parameter the available trend locations will be sorted by distance, nearest to furthest, to the co-ordinate pair. The valid ranges for longitude is -180.0 to +180.0 (West is negative, East is positive) inclusive.
	 * @param lat If provided with a lat parameter the available trend locations will be sorted by distance, nearest to furthest, to the co-ordinate pair. The valid ranges for longitude is -180.0 to +180.0 (West is negative, East is positive) inclusive.
	 * @return A list of places near the given coordinates
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 */
	public List<TrendPlace> closestPlaces(Double lon, Double lat) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

		if (lat != null) {
			nameValuePair.add(Parameters.LATITUDE.valuePairWith(Double.toString(lat)));
		}

		if (lon != null) {
			nameValuePair.add(Parameters.LONGITUDE.valuePairWith(Double.toString(lon)));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_CLOSEST_PLACES, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return TrendPlace.parseList(response.getResponseString());

		}

		return null;
	}

	/**
	 * Returns the top 10 trending topics for a specific WOEID, if trending information is available for it.
	 * The response is an array of "trend" objects that encode the name of the trending topic, the query parameter that can be used to search for the topic on Twitter Search, and the Twitter Search URL.
	 * This information is cached for 5 minutes. Requesting more frequently than that will not return any more data, and will count against your rate limit usage.
	 *
	 * @param woeid The Yahoo! Where On Earth ID of the location to return trending information for. Global information is available by using 1 as the WOEID
	 * @param exclude Setting this equal to hashtags will remove all hashtags from the trends list.
	 * @return a list of trends
	 * @throws TwitterErrorRequestException Throws a Twitter Error request exception if something fails exs: Communication error / twitter api down / Twitter params request errors..
	 *
	 */
	public List<Trend> trendsForPlace(long woeid, String exclude) throws TwitterErrorRequestException {

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(Parameters.ID.valuePairWith(Double.toString(woeid)));

		if (Tools.isNotEmpty(exclude)) {
			nameValuePair.add(Parameters.EXCLUDE.valuePairWith(exclude));
		}

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.GET, Urls.URL_TRENDS_PLACE, nameValuePair);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			return Trend.parseList(response.getResponseString());

		}

		return null;
	}

	/**
	 * Invalidates current token via oauth basic authorization
	 * @return The access token (bearer)
	 * @throws TwitterErrorRequestException
	 */
	public String obtainBearerToken() throws TwitterErrorRequestException {

		Map<String, String> headers = new HashMap<String, String>();
		String header = "Basic " + twitterHttpClient.getOauthBase64BearerToken();
		headers.put("Authorization", header);
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("grant_type", "client_credentials"));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_OBTAIN_BEARER_TOKEN.getUrl(), nameValuePair, headers);

		if (response.getError() != null) {

			throw new TwitterErrorRequestException(response.getError());

		} else if (response.isNotEmpty()) {

			String rString = response.getResponseString();
			JSONObject jsonObj;
			try {

				jsonObj = new JSONObject(rString);
                return jsonObj.getString("access_token");

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	/**
	 * Invalidates current token via oauth basic authorization
	 * @return The access token (bearer)
	 * @throws TwitterErrorRequestException
	 */
	public String invalidateBearerToken(String accessTokenBearer) throws TwitterErrorRequestException {

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Basic " + twitterHttpClient.getOauthBase64BearerToken());
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("access_token", accessTokenBearer));

		ResponseString response = twitterHttpClient.makeRequestWithParameters(Request.POST, Urls.URL_INVALIDATE_BEARER_TOKEN.getUrl(), nameValuePair, headers);
		
		if (response.getError() != null) {
			
			throw new TwitterErrorRequestException(response.getError());
			
		} else if (response.isNotEmpty()) {
			
			String rString = response.getResponseString();
			JSONObject jsonObj;
			try {
				
				jsonObj = new JSONObject(rString);
                return jsonObj.getString("access_token");
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
	
}
