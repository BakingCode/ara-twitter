package com.bakingcode.io.twitter.model;

import com.bakingcode.io.twitter.tools.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static com.bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * It wraps a error message from twitter and parses it from a JSON 
 */
public class TwitterError implements Serializable, IError {

    // ///////////////////////////////////////////////////////////////////////////
    // Twitter error constants
    // ///////////////////////////////////////////////////////////////////////////

    /**
     * User has been suspended
     */
    public final static int ERROR_USER_SUSPENDED = 63;

    /**
     * Rate limit Error
     */
    public final static int ERROR_RATE_LIMIT = 88;

    /**
     * Page not exists
     */
    public final static int PAGE_NOT_EXISTS = 34;

    // ///////////////////////////////////////////////////////////////////////////
    // Constants
    // ///////////////////////////////////////////////////////////////////////////

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = -7973344154357095880L;
	
	/**
	 * Private log tag
	 */
	private static final String TAG = "TwError";

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Message of error
	 */
	private String message;
	
	/**
	 * Error code
	 */
	private int code;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty user instance. 
	 */
	public TwitterError() { }
	
	/**
	 * Parses a Tweet object and returns a new instance of user
	 * 
	 * @param response JSON response
	 * @return a new User instance
	 */
	public static TwitterError parse(String response) {
		
		if (Tools.isNotEmpty(response)) {
			
			try {
				
				JSONObject jsonObject = new JSONObject(response);
				JSONArray jsonErrors = jsonObject.getJSONArray("errors");
				
				if (jsonErrors.length() > 0) {
					
					JSONObject jsonErr = jsonErrors.getJSONObject(0);
					TwitterError e = new TwitterError();
					e.setMessage(jsonErr.getString("message"));
					e.setCode(jsonErr.getInt("code"));
					
					return e;
				}
				
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
			
		}
		
		return null;
		
	}
	

	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
}
