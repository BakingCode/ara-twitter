package com.bakingcode.io.twitter.model;

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
public class Place implements Serializable {
	
	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 7706914996266304022L;

	/**
	 * Tag for logging
	 */
	private final static String TAG = "Place";
	
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
	private String fullName;
	
	/**
	 * The type of location represented by this place.
	 */
	private String placeType;
	
	/**
	 * URL representing the location of additional place metadata for this place. Example: <a href="http://api.twitter.com/1/geo/id/7238f93a3e899af6.json">http://api.twitter.com/1/geo/id/7238f93a3e899af6.json</a>
	 */
	private String url;
	
	/**
	 * Where on earth id of yahoo
	 */
	private String id;

	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty place instance. 
	 */
	public Place () { }
	
	/**
	 * JSON parser constructor. It parses a json object to make a new place instance
	 * 
	 * @param json jsonObject to parse
	 */
	public Place (JSONObject json) {
		
		try {
			
			id = json.getString("id");
			
			country = json.getString("country");
				
			if (json.has("country_code")) {
				countryCode = json.getString("country_code");
			}
			
			fullName = json.getString("full_name");
			
			placeType = json.getString("place_type");
			
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
	public static List<Place> parseList(String response) {
		
		try {
			
			JSONArray arrJson = new JSONArray(response);
			List<Place> listPlaces = new ArrayList<Place>();
			
			for (int i=0 ; i<arrJson.length(); i++) {
				
				JSONObject jsonObject = arrJson.getJSONObject(i);
				Place p = new Place(jsonObject);
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
	 * @return the parentId
	 */
	public String getParentId() {
		return placeType;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.placeType = parentId;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setid(String id) {
		this.id = id;
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
