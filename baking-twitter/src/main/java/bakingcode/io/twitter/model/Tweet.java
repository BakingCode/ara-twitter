/*
 * Copyright (c) 2012 BakingCode. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * BakingCode ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with BakingCode.
 */ 
package bakingcode.io.twitter.model;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bakingcode.io.twitter.tools.HTMLEntity;
import bakingcode.io.twitter.tools.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Also known as a Status Update, Tweets are the basic atomic unit of all things Twitter. Users create Tweets. Tweets can be embedded, replied to, favorited, unfavorited, retweeted, unretweeted and deleted. A retweet contains an embedded Tweet object within the "retweeted_status" attribute.
 */
public class Tweet implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 1415346057322953722L;
	
	/**
	 * Private log tag
	 */
	private static final String TAG = "Tweet";

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Nullable. Represents the geographic location of this Tweet as reported by the user or client application. The inner coordinates array is formatted as geoJSON (longitude first, then latitude).
	 */
	private Coordinates coordinates;
	
	/**
	 * Nullable. When present, indicates that the tweet is associated (but not necessarily originating from) a Place.
	 */
	private Place place;
	
	/**
	 * UTC time when this Tweet was created.
	 */
	private Date createdAt;
	
	/**
	 * Nullable. Perspectival. Indicates whether this Tweet has been favorited by the authenticating user.
	 */
	private Boolean favorited;
	
	/**
	 * The integer representation of the unique identifier for this Tweet. This number is greater than 53 bits and some programming languages may have difficulty/silent defects in interpreting it. Using a signed 64 bit integer for storing this identifier is safe. Use id_str for fetching the identifier to stay on the safe side. See Twitter IDs, JSON and Snowflake.
	 */
	private long id;
	
	/**
	 * Nullable. If the represented Tweet is a reply, this field will contain the screen name of the original Tweet's author.
	 */
	private String inReplyToScreenName;
	
	/**
	 * Nullable. If the represented Tweet is a reply, this field will contain the integer representation of the original Tweet's ID.
	 */
	private Long inReplyToStatusId;
	
	/**
	 * Nullable. If the represented Tweet is a reply, this field will contain the integer representation of the original Tweet's author ID.
	 */
	private Long inReplyToUserId;
	
	/**
	 * Number of times this Tweet has been retweeted. This field is no longer capped at 99 and will not turn into a String for "100+"
	 */
	private int retweetCount;
	
	/**
	 * Perspectival. Indicates whether this Tweet has been retweeted by the authenticating user.
	 */
	private boolean retweeted;
	
	/**
	 * If this tweet is retweeted, this instance will be the original tweet and retweetStatus the tweet retweeted
	 */
	private Tweet retweetedStatus;
	
	/**
	 * The actual UTF-8 text of the status update. See twitter-text for details on what is currently considered valid characters.
	 */
	private String text;
	
	/**
	 * Indicates whether the value of the text parameter was truncated, for example, as a result of a retweet exceeding the 140 character Tweet length. Truncated text will end in ellipsis, like this ...
	 */
	private boolean truncated;
	
	/**
	 * The user who posted this Tweet. Perspectival attributes embedded within this object are unreliable. See Why are embedded objects stale or inaccurate?.
	 */
	private User user;
	
	/**
	 * An array of media attached to the Tweet with the new Twitter Photo Upload feature.
	 * (This field only will be filled up if the request is called with INCLUDE_ENTITIES = TRUE)
	 */
	private MediaEntity[] mediaEntities;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty tweet instance. 
	 */
	public Tweet() { }
	
	/**
	 * Parses a Tweet object and returns a new instance of tweet
	 * 
	 * @param response JSON response
	 * @return a new Tweet instance
	 */
	public static Tweet parse(String response) {
		
		if (Tools.isNotEmpty(response)) {
			
			try {
				
				JSONObject jsonObject = new JSONObject(response);
				Tweet t = new Tweet(jsonObject);
				return t;
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
			
		}
		
		return null;
		
	}

	/**
	 * Parses a List of tweets envolved by a JsonArray
	 * 
	 * @param response JSON string response
	 * @return List of tweets
	 */
	public static List<Tweet> parseList(String response) {
		
		try {
			
			JSONArray arrJson = new JSONArray(response);
			List<Tweet> listTweets = new ArrayList<Tweet>();
			
			for (int i=0 ; i<arrJson.length(); i++) {
				
				JSONObject jsonObject = arrJson.getJSONObject(i);
				Tweet t = new Tweet(jsonObject);
				listTweets.add(t);
				
			}
			
			return listTweets;
			
		} catch (JSONException e) {
			LT(TAG, e);
		}
		
		return null;
		
	}
	
	/**
	 * JSON parser constructor. It parses a json object to make a new tweet instance
	 * 
	 * @param json jsonObject to parse
	 */
	public Tweet(JSONObject json) {
		
		try {
			
			if (!json.isNull("coordinates")) {
				coordinates = new Coordinates(json.getJSONObject("coordinates"));
			}
			
			if (json.has("place") && !json.isNull("place")) {
				setPlace(new Place(json.getJSONObject("place")));
			}
			
			if (json.has("created_at") && !json.isNull("created_at")) {
				createdAt = Tools.parseLargeTweetDate(json.getString("created_at"));
			}
			
			if (json.has("entities") && !json.isNull("entities")) {
				
				JSONObject entitiesObject = json.getJSONObject("entities");
				
				if (entitiesObject.has("media") && !entitiesObject.isNull("media")) {
					
					JSONArray mediaArray = entitiesObject.getJSONArray("media");
					mediaEntities = new MediaEntity[mediaArray.length()];
					
					for (int i = 0 ; i < mediaArray.length() ; i++) {
						
						JSONObject mediaJson = mediaArray.getJSONObject(i);
						MediaEntity m = new MediaEntity(mediaJson);
						mediaEntities[i] = m;
						
					}
					
				}
				
			}
			
			if (!json.isNull("favorited")) {
				favorited = json.getBoolean("favorited");
			}
			
			id = json.getLong("id");
			inReplyToScreenName = json.getString("in_reply_to_screen_name");
			
			if (!json.isNull("in_reply_to_status_id")) {
				inReplyToStatusId = json.getLong("in_reply_to_status_id");
			}
			
			if (!json.isNull("in_reply_to_user_id")) {
				inReplyToUserId = json.getLong("in_reply_to_user_id");
			}
			
			retweetCount = json.getInt("retweet_count");
			retweeted = json.getBoolean("retweeted");
			
			text = HTMLEntity.unescape(json.getString("text"));
			
			truncated = json.getBoolean("truncated");
			
			if (!json.isNull("user")) {
				user = new User(json.getJSONObject("user"));
			}
			
			if (json.has("retweeted_status") && !json.isNull("retweeted_status")) {
				retweetedStatus = new Tweet(json.getJSONObject("retweeted_status"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return the coordinates
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the favorited
	 */
	public Boolean getFavorited() {
		return favorited;
	}

	/**
	 * @param favorited the favorited to set
	 */
	public void setFavorited(Boolean favorited) {
		this.favorited = favorited;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the inReplyToScreenName
	 */
	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	/**
	 * @param inReplyToScreenName the inReplyToScreenName to set
	 */
	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	/**
	 * @return the inReplyToStatusId
	 */
	public Long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	/**
	 * @param inReplyToStatusId the inReplyToStatusId to set
	 */
	public void setInReplyToStatusId(Long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	/**
	 * @return the inReplyToUserId
	 */
	public Long getInReplyToUserId() {
		return inReplyToUserId;
	}

	/**
	 * @param inReplyToUserId the inReplyToUserId to set
	 */
	public void setInReplyToUserId(Long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	/**
	 * @return the retweetCount
	 */
	public int getRetweetCount() {
		return retweetCount;
	}

	/**
	 * @param retweetCount the retweetCount to set
	 */
	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	/**
	 * @return the retweeted
	 */
	public boolean isRetweeted() {
		return retweeted;
	}

	/**
	 * @param retweeted the retweeted to set
	 */
	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the truncated
	 */
	public boolean isTruncated() {
		return truncated;
	}

	/**
	 * @param truncated the truncated to set
	 */
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the retweetedStatus
	 */
	public Tweet getRetweetedStatus() {
		return retweetedStatus;
	}

	/**
	 * @param retweetedStatus the retweetedStatus to set
	 */
	public void setRetweetedStatus(Tweet retweetedStatus) {
		this.retweetedStatus = retweetedStatus;
	}

	/**
	 * @return the place
	 */
	public Place getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(Place place) {
		this.place = place;
	}

	/**
	 * @return the mediaEntities
	 */
	public MediaEntity[] getMediaEntities() {
		return mediaEntities;
	}

	/**
	 * @param mediaEntities the mediaEntities to set
	 */
	public void setMediaEntities(MediaEntity[] mediaEntities) {
		this.mediaEntities = mediaEntities;
	}
}
