package com.bakingcode.io.twitter.model;

import static com.bakingcode.io.twitter.tools.TwitterLogging.LT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import bakingcode.io.twitter.model.ApiResourceFamily.ResourceFamily;
import bakingcode.io.twitter.tools.Tools;

/**
 * Api Method representing a method of the Twitter API.
 */
public class ApiMethod implements Serializable {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 1980688086519525851L;

	/**
	 * Tag for logging
	 */
	private final static String TAG = "ApiMethod";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Logic
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Methods
	 */
	public enum Method {

		ACCOUNT_SETTINGS(ResourceFamily.ACCOUNT + "/settings"),
		ACCOUNT_VERIFY_CREDENTIALS(ResourceFamily.ACCOUNT + "/verify_credentials"),
		
		APPLICATION_RATE_LIMIT_STATUS(ResourceFamily.APPLICATION + "/rate_limit_status"),
		
		BLOCKS_IDS(ResourceFamily.BLOCKS + "/ids"),
		BLOCKS_LIST(ResourceFamily.BLOCKS + "/list"),

		DIRECT_MESSAGES(ResourceFamily.DIRECT_MESSAGES.getName()),
		DIRECT_MESSAGES_SENT(ResourceFamily.DIRECT_MESSAGES + "/sent"),
		DIRECT_MESSAGES_SHOW(ResourceFamily.DIRECT_MESSAGES + "/show"),
		
		FAVOURITES(ResourceFamily.FAVOURITES + "/list"),

		FOLLOWERS_IDS(ResourceFamily.FOLLOWERS + "/ids"),
		FOLLOWERS_LIST(ResourceFamily.FOLLOWERS + "/list"),

		FRIENDS_IDS(ResourceFamily.FRIENDS + "/ids"),
		FRIENDS_LIST(ResourceFamily.FRIENDS + "/list"),

		FRIENDSHIPS_INCOMING(ResourceFamily.FRIENDSHIPS + "/incoming"),
		FRIENDSHIPS_LOOKUP(ResourceFamily.FRIENDSHIPS + "/lookup"),
		FRIENDSHIPS_NO_RETWEETS_IDS(ResourceFamily.FRIENDSHIPS + "/no_retweets/ids"),
		FRIENDSHIPS_OUTGOING(ResourceFamily.FRIENDSHIPS + "/outgoing"),
		FRIENDSHIPS_SHOW(ResourceFamily.FRIENDSHIPS + "/show"),

		GEO_ID_PLACE_ID(ResourceFamily.GEO + "/id/:place_id"),
		GEO_REVERSE_GEOCODE(ResourceFamily.GEO + "/reverse_geocode"),
		GEO_SEARCH(ResourceFamily.GEO + "/search"),
		GEO_SIMILAR_PLACES(ResourceFamily.GEO + "/similar_places"),

		HELP_CONFIGURATION(ResourceFamily.HELP + "/configuration"),
		HELP_LANGUAGES(ResourceFamily.HELP + "/languages"),
		HELP_PRIVACY(ResourceFamily.HELP + "/privacy"),
		HELP_TOS(ResourceFamily.HELP + "/tos"),

		LISTS(ResourceFamily.LISTS.getName()),
		LISTS_LIST(ResourceFamily.LISTS + "/list"),
		LISTS_MEMBERS(ResourceFamily.LISTS + "/members"),
		LISTS_MEMBERS_SHOW(ResourceFamily.LISTS + "/members/show"),
		LISTS_MEMBERSHIPS(ResourceFamily.LISTS + "/memberships"),
		LISTS_SHOW(ResourceFamily.LISTS + "/show"),
		LISTS_STATUSES(ResourceFamily.LISTS + "/statuses"),
		LISTS_SUSCRIBERS(ResourceFamily.LISTS + "/subscribers"),
		LISTS_SUSCRIBERS_SHOW(ResourceFamily.LISTS + "/subscribers/show"),
		LISTS_SUBSCRIPTIONS(ResourceFamily.LISTS + "/subscriptions"),

		SAVED_SEARCHES_LIST(ResourceFamily.SAVED_SEARCHES + "/list"),
		SAVED_SEARCHES_SHOW_ID(ResourceFamily.SAVED_SEARCHES + "/show/:id"),
		
		SEARCH_TWEETS(ResourceFamily.SEARCH + "/tweets"),

		STATUSES_HOME_TIMELINE(ResourceFamily.STATUSES + "/home_timeline"),
		STATUSES_MENTIONS_TIMELINE(ResourceFamily.STATUSES + "/mentions_timeline"),
		STATUSES_OEMBED(ResourceFamily.STATUSES + "/oembed"),
		STATUSES_RETWEETS_ID(ResourceFamily.STATUSES + "/retweets/:id"),
		STATUSES_RETWEETS_OF_ME(ResourceFamily.STATUSES + "/retweets_of_me"),
		STATUSES_SHOW_ID(ResourceFamily.STATUSES + "/show/:id"),
		STATUSES_USER_TIMELINE(ResourceFamily.STATUSES + "/user_timeline"),

		TRENDS_AVAILABLE(ResourceFamily.TRENDS + "/available"),
		TRENDS_CLOSEST(ResourceFamily.TRENDS + "/closest"),
		TRENDS_PLACE(ResourceFamily.TRENDS + "/place"),

		USERS_CONTRIBUTEES(ResourceFamily.USERS + "/contributees"),
		USERS_CONTRIBUTORS(ResourceFamily.USERS + "/contributors"),
		USERS_LOOKUP(ResourceFamily.USERS + "/lookup"),
		USERS_PROFILE_BANNER(ResourceFamily.USERS + "/profile_banner"),
		USERS_SEARCH(ResourceFamily.USERS + "/search"),
		USERS_SHOW(ResourceFamily.USERS + "/show"),
		USERS_SUGGESTIONS(ResourceFamily.USERS + "/suggestions"),
		USERS_SUGGESTIONS_SLUG(ResourceFamily.USERS + "/suggestions/:slug"),
		USERS_SUGGESTIONS_SLUG_MEMBERS(ResourceFamily.USERS + "/suggestions/:slug/members");	
		
		/**
		 * Obtains the list of methods that start with the name provided.
		 * 
		 * @param name Name of the method
		 * @return List of methods
		 */
		public static List<Method> getMethodsStartWith(String name) {
			
			if (Tools.isEmpty(name)) {
				return null;
			}
			
			// Add all methods starting with the name
			List<Method> methods = new ArrayList<Method>();
			Locale locale = Locale.getDefault();
			for (Method meth : Method.values()) {
				if (name.startsWith(name.toUpperCase(locale))) {
					methods.add(meth);
				}
			}
			
			return methods;
		}
		
		/**
		 * Name of the method
		 */
		private String name;

		private Method(String name) {
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The method value.
	 */
	private Method method;
	
	/**
	 * The remaining time until the next request available.
	 */
	private int remaining;
	
	/**
	 * The time when the requests will be available again.
	 */
	private long reset;
	
	/**
	 * The limit of the requests.
	 */
	private int limit;
	

	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new api method instance. 
	 */
	public ApiMethod () { }
	
	/**
	 * JSON parser constructor. It parses a json object to make a new api method.
	 * 
	 * @param json jsonObject to parse
	 * @param method Method value
	 */
	public ApiMethod (JSONObject json, Method method) {
		
		try {
			
			this.method = method;
			remaining = json.getInt("remaining");
			reset = json.getLong("reset");
			limit = json.getInt("limit");
			
		} catch (JSONException e) {
			
			LT(TAG, e);
		}
		
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
	
	/**
	 * @return the remaining
	 */
	public int getRemaining() {
		return remaining;
	}

	/**
	 * @param remaining the remaining to set
	 */
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	/**
	 * @return the reset
	 */
	public long getReset() {
		return reset;
	}

	/**
	 * @param reset the reset to set
	 */
	public void setReset(long reset) {
		this.reset = reset;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
