package bakingcode.io.twitter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * Describes the place of a coordinate 
 */
public class TrendPlace implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 7706914996266304022L;

	/**
	 * Tag for logging
	 */
	private final static String TAG = "TrendPlace";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The country of this place
	 */
	private String country;
	
	/**
	 * The country code of this place
	 */
	private String countryCode;
	
	/**
	 * The name of this place
	 */
	private String name;
	
	/**
	 * Full human-readable representation of the place's name
	 */
	private String fullName;
	
	/**
	 * The id of parent of this place
	 */
	private long parentId;
	
	/**
	 * Url of yahoo apis sample: <a href="http://where.yahooapis.com/v1/place/23424748">http://where.yahooapis.com/v1/place/23424748</a>
	 */
	private String url;
	
	/**
	 * Where on earth id of yahoo
	 */
	private long woeid;

	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty place instance. 
	 */
	public TrendPlace () { }
	
	/**
	 * JSON parser constructor. It parses a json object to make a new place instance
	 * 
	 * @param json jsonObject to parse
	 */
	public TrendPlace (JSONObject json) {
		
		try {
			
			if (json.has("woeid")) {
				woeid = json.getLong("woeid");
			}
			
			country = json.getString("country");
				
			if (json.has("countryCode")) {
				countryCode = json.getString("countryCode");
			}
			
			name = json.getString("name");
			
			if (json.has("parentid")) {
				parentId = json.getLong("parentid");
			}
			
			if (json.has("url")) {
				url = json.getString("url");
			}
			
		} catch (JSONException e) {
			
			LT(TAG, e);
		}
		
	}
	
	/**
	 * Parses a JSON string to a Place list
	 * @param response Json string
	 * @return Place list
	 */
	public static List<TrendPlace> parseList(String response) {
		
		try {
			
			JSONArray arrJson = new JSONArray(response);
			List<TrendPlace> listPlaces = new ArrayList<TrendPlace>();
			
			for (int i=0 ; i<arrJson.length(); i++) {
				
				JSONObject jsonObject = arrJson.getJSONObject(i);
				TrendPlace p = new TrendPlace(jsonObject);
				listPlaces.add(p);
				
			}
			
			return listPlaces;
			
		} catch (JSONException e) {
			LT(TAG, e);
		}
		
		return null;
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

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
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(long parentId) {
		this.parentId = parentId;
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

	/**
	 * @return the woeid
	 */
	public long getWoeid() {
		return woeid;
	}

	/**
	 * @param woeid the woeid to set
	 */
	public void setWoeid(long woeid) {
		this.woeid = woeid;
	}

	/**
	 * @return the full name
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName Full name of place
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
