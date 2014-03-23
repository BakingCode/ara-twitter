package com.bakingcode.io.twitter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Api configuration wraper object 
 */
public class ApiConfiguration implements Serializable {

	/**
	 *  Serial Id
	 */
	private static final long serialVersionUID = -3441699331272124409L;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Characters reserver per media. Useful for calculate remain chars tweet with urls
	 */
	private int charactersReservedPerMedia;
	
	/**
	 * Max media files per upload.
	 */
	private int maxMediaPerUpload;
	
	/**
	 * The non username paths
	 */
	private List<String> nonUsernamePaths;
	
	/**
	 * The limit size of photo 
	 */
	private long photoSizeLimit;
	
	/**
	 * Short url length (in http)
	 */
	private int shortUrlLength;
	
	/**
	 * Short url length (in https)
	 */
	private int shortUrlLengthHttps;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty ApiConfiguration instance.
	 */
	public ApiConfiguration() { }

	/**
	 * JSON parser constructor. It parses a json object to make a new ApiConfiguration instance
	 * 
	 * @param json jsonObject to parse
	 */
	public ApiConfiguration(JSONObject json) {
	
		try {
			
			charactersReservedPerMedia = json.getInt("characters_reserved_per_media");
			maxMediaPerUpload = json.getInt("max_media_per_upload");

			JSONArray jsonArray = json.getJSONArray("non_username_paths");
			nonUsernamePaths = new ArrayList<String>();
			for (int i = 0 ; i<jsonArray.length(); i++) {
				
				nonUsernamePaths.add(jsonArray.getString(i));
				
			}
			
			photoSizeLimit = json.getLong("photo_size_limit");
			shortUrlLength = json.getInt("short_url_length");
			shortUrlLengthHttps = json.getInt("short_url_length_https");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @return the charactersReservedPerMedia
	 */
	public int getCharactersReservedPerMedia() {
		return charactersReservedPerMedia;
	}

	/**
	 * @param charactersReservedPerMedia the charactersReservedPerMedia to set
	 */
	public void setCharactersReservedPerMedia(int charactersReservedPerMedia) {
		this.charactersReservedPerMedia = charactersReservedPerMedia;
	}

	/**
	 * @return the maxMediaPerUpload
	 */
	public int getMaxMediaPerUpload() {
		return maxMediaPerUpload;
	}

	/**
	 * @param maxMediaPerUpload the maxMediaPerUpload to set
	 */
	public void setMaxMediaPerUpload(int maxMediaPerUpload) {
		this.maxMediaPerUpload = maxMediaPerUpload;
	}

	/**
	 * @return the nonUsernamePaths
	 */
	public List<String> getNonUsernamePaths() {
		return nonUsernamePaths;
	}

	/**
	 * @param nonUsernamePaths the nonUsernamePaths to set
	 */
	public void setNonUsernamePaths(List<String> nonUsernamePaths) {
		this.nonUsernamePaths = nonUsernamePaths;
	}

	/**
	 * @return the photoSizeLimit
	 */
	public long getPhotoSizeLimit() {
		return photoSizeLimit;
	}

	/**
	 * @param photoSizeLimit the photoSizeLimit to set
	 */
	public void setPhotoSizeLimit(long photoSizeLimit) {
		this.photoSizeLimit = photoSizeLimit;
	}

	/**
	 * @return the shortUrlLength
	 */
	public int getShortUrlLength() {
		return shortUrlLength;
	}

	/**
	 * @param shortUrlLength the shortUrlLength to set
	 */
	public void setShortUrlLength(int shortUrlLength) {
		this.shortUrlLength = shortUrlLength;
	}

	/**
	 * @return the shortUrlLengthHttps
	 */
	public int getShortUrlLengthHttps() {
		return shortUrlLengthHttps;
	}

	/**
	 * @param shortUrlLengthHttps the shortUrlLengthHttps to set
	 */
	public void setShortUrlLengthHttps(int shortUrlLengthHttps) {
		this.shortUrlLengthHttps = shortUrlLengthHttps;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
