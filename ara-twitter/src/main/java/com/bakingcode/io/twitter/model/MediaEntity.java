package com.bakingcode.io.twitter.model;

import static com.bakingcode.io.twitter.tools.TwitterLogging.LT;

import java.io.Serializable;

import com.bakingcode.io.twitter.tools.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaEntity implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = -20025991799762541L;

	/**
	 * Private log tag
	 */
	private static final String TAG = "MediaEntity";

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * the media ID (int format)
	 */
	private long id;
	
	/**
	 * The URL of the media file (see the `sizes` attribute for available sizes)
	 */
	private String mediaUrl;
	
	/**
	 * The SSL URL of the media file (see the sizes attribute for available sizes)
	 */
	private String mediaUrlHttps;
	
	/**
	 * The media URL that was extracted
	 */
	private String url;
	
	/**
	 * Not a URL but a string to display instead of the media URL
	 */
	private String displayUrl;
	
	/**
	 * The fully resolved media URL
	 */
	private String expandedUrl;
	
	/**
	 * only photo for now 
	 * 
	 * (Convert this field to a Enum if twitter decides to put more types in media)
	 */
	private String type;

	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty MediaEntity instance. 
	 */
	public MediaEntity() { }
	
	/**
	 * Parses a Tweet object and returns a new instance of MediaEntity
	 * 
	 * @param response JSON response
	 * @return a new MediaEntity instance
	 */
	public static MediaEntity parse(String response) {
		
		if (Tools.isNotEmpty(response)) {
			
			try {
				
				JSONObject jsonObject = new JSONObject(response);
				MediaEntity m = new MediaEntity(jsonObject);
				return m;
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * JSON parser constructor. It parses a json object to make a new MediaEntity instance
	 * 
	 * @param json jsonObject to parse
	 */
	public MediaEntity(JSONObject json) {
		
		try {
			
			id = json.getLong("id");
			mediaUrl = json.getString("media_url");
			mediaUrlHttps = json.getString("media_url_https");
			url = json.getString("url");
			displayUrl = json.getString("display_url");
			expandedUrl = json.getString("expanded_url");
			type = json.getString("type");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
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
	 * @return the mediaUrl
	 */
	public String getMediaUrl() {
		return mediaUrl;
	}

	/**
	 * @param mediaUrl the mediaUrl to set
	 */
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	/**
	 * @return the mediaUrlHttps
	 */
	public String getMediaUrlHttps() {
		return mediaUrlHttps;
	}

	/**
	 * @param mediaUrlHttps the mediaUrlHttps to set
	 */
	public void setMediaUrlHttps(String mediaUrlHttps) {
		this.mediaUrlHttps = mediaUrlHttps;
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
	 * @return the displayUrl
	 */
	public String getDisplayUrl() {
		return displayUrl;
	}

	/**
	 * @param displayUrl the displayUrl to set
	 */
	public void setDisplayUrl(String displayUrl) {
		this.displayUrl = displayUrl;
	}

	/**
	 * @return the expandedUrl
	 */
	public String getExpandedUrl() {
		return expandedUrl;
	}

	/**
	 * @param expandedUrl the expandedUrl to set
	 */
	public void setExpandedUrl(String expandedUrl) {
		this.expandedUrl = expandedUrl;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
