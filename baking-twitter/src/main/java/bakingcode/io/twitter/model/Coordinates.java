package bakingcode.io.twitter.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Represents the geographic location of this Tweet as reported by the user or client application. The inner coordinates array is formatted as geoJSON (longitude first, then latitude).
 */
public class Coordinates implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = -3142844708706062103L;

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Longitude
	 */
	private double longitude;
	
	/**
	 * Latitude
	 */
	private double latitude;
	
	/**
	 * The type of data encoded in the coordinates property. This will be "Point" for Tweet coordinates fields.
	 */
	private String type;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty coordinates instance. 
	 */
	public Coordinates() { }
	
	/**
	 * JSON parser constructor. It parses a json object to make a new coordinates instance
	 * 
	 * @param json jsonObject to parse
	 */
	public Coordinates(JSONObject json) {
		
		try {

			JSONArray coordArray = json.getJSONArray("coordinates");
			longitude = coordArray.getDouble(0);
			latitude = coordArray.getDouble(1);
			
			type = json.getString("type");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
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
