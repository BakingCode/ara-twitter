package bakingcode.io.twitter.model;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing a Trend
 */
public class Trend implements Serializable {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -374880173286818073L;

	/**
	 * Tag for logging
	 */
	private final static String TAG = "Trend";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Name of Trending
	 */
	private String name;
	
	/**
	 * True if is promited content
	 */
	private Boolean promotedContent;
	
	/**
	 * The query if need a search
	 */
	private String query;
	
	/**
	 * Url to twitter webpage
	 */
	private String url;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty trend instance. 
	 */
	public Trend () { }
	
	/**
	 * JSON parser constructor. It parses a json object to make a new trend instance
	 * 
	 * @param json jsonObject to parse
	 */
	public Trend (JSONObject json) {
		
		try {
			
			name = json.getString("name");
			
			if (!json.isNull("promoted_content")) {
				promotedContent = json.getBoolean("promoted_content");
			}
			
			query = json.getString("query");
			url = json.getString("url");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Parses a JSON string to a Trend list
	 * @param response Json string
	 * @return Place list
	 */
	public static List<Trend> parseList(String response) {
		
		try {
			
			JSONArray wraperArray = new JSONArray(response);
			
			if (wraperArray.length() == 0) {
				return null;
			}
			
			JSONObject jsonObj = wraperArray.getJSONObject(0);
			JSONArray arrJson = jsonObj.getJSONArray("trends");
			List<Trend> listTrends = new ArrayList<Trend>();
			
			for (int i=0 ; i<arrJson.length(); i++) {
				
				JSONObject jsonObject = arrJson.getJSONObject(i);
				Trend p = new Trend(jsonObject);
				listTrends.add(p);
				
			}
			
			return listTrends;
			
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
	 * @return the promotedContent
	 */
	public boolean isPromotedContent() {
		return promotedContent;
	}

	/**
	 * @param promotedContent the promotedContent to set
	 */
	public void setPromotedContent(boolean promotedContent) {
		this.promotedContent = promotedContent;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
