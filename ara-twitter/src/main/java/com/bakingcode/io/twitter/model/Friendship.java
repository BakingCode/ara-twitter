package com.bakingcode.io.twitter.model;

import com.bakingcode.io.twitter.tools.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 *  Describes the friendship connections with a related user
 */
public class Friendship implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 8505943900465155134L;
	
	/**
	 * Logging tag
	 */
	private static final String TAG = "Friendship";
	
	/**
	 * Connection types of Friendship
	 * From twitter api: "Values for connections can be: following, following_requested, followed_by, none."
	 */
	public enum ConnectionType {
		
		FOLLOWING,
		FOLLOWING_REQUESTED,
		FOLLOWED_BY,
		NONE;
		
	};
	
	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Name of user
	 */
	private String name;
	
	/**
	 * The id of user
	 */
	private Long id;
	
	/**
	 * A array of connections
	 */
	private ConnectionType[] connections;
	
	/**
	 * The screenname of user
	 */
	private String screenName;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Default constructor. It creates a new empty friendship instance. 
	 */
	public Friendship() { }

	/**
	 * JSON parser constructor. It parses a json object to make a new friendship instance
	 * 
	 * @param json jsonObject to parse
	 */
	public Friendship(JSONObject json) {
		
		try {

			name = json.getString("name");
			id = json.getLong("id");
			
			JSONArray arrConn = json.getJSONArray("connections");
			connections = new ConnectionType[arrConn.length()];
			
			for (int i = 0 ; i < arrConn.length() ; i ++) {
				
				String tmpConnection = arrConn.getString(i).toUpperCase(Locale.getDefault());
				connections[i] = ConnectionType.valueOf(tmpConnection);
				
			}
			
			screenName = json.getString("screen_name");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Parses a Tweet object and returns a new instance of friendship
	 * 
	 * @param response JSON response
	 * @return a new Tweet instance
	 */
	public static Friendship parse(String response) {
		
		if (Tools.isNotEmpty(response)) {
			
			try {
				
				JSONObject jsonObject = new JSONObject(response);
				Friendship f = new Friendship(jsonObject);
				return f;
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * Parses a List of friendships envolved by a JsonArray
	 * 
	 * @param response JSON string response
	 * @return List of tweets
	 */
	public static List<Friendship> parseList(String response) {
		
		try {
			
			JSONArray arrJson = new JSONArray(response);
			List<Friendship> listFriendships = new ArrayList<Friendship>();
			
			for (int i=0 ; i<arrJson.length(); i++) {
				
				JSONObject jsonObject = arrJson.getJSONObject(i);
				Friendship t = new Friendship(jsonObject);
				listFriendships.add(t);
				
			}
			
			return listFriendships;
			
		} catch (JSONException e) {
			LT(TAG, e);
		}
		
		return null;
		
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the connections
	 */
	public ConnectionType[] getConnections() {
		return connections;
	}

	/**
	 * @param connections the connections to set
	 */
	public void setConnections(ConnectionType[] connections) {
		this.connections = connections;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
}
