package com.bakingcode.io.twitter.model;

import com.bakingcode.io.twitter.tools.Tools;

/**
 * Abstracts a http response with the response string and a error
 */
public class ResponseString {
	
	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The string of response
	 */
	private String responseString;
	
	/**
	 * If the response code of twitter request is != 200 this will parse a error item
	 */
	private IError error;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Utils
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Method for known if the response String is empty or not.
	 * @return true if the response string is not empty false otherwise
	 */
	public boolean isNotEmpty() {
		return Tools.isNotEmpty(responseString);
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return the responseString
	 */
	public String getResponseString() {
		return responseString;
	}

	/**
	 * @param responseString the responseString to set
	 */
	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	/**
	 * @return the error
	 */
	public IError getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(IError error) {
		this.error = error;
	}

}
