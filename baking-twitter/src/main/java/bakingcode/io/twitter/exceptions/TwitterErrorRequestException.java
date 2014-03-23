package bakingcode.io.twitter.exceptions;

import bakingcode.io.twitter.model.IError;

/**
 * Exception that handles a twitter error  
 */
public class TwitterErrorRequestException extends Exception {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = -4473333355716260005L;

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Error object
	 */
	private IError error;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor with twitter error parameter
	 * @param error eror parameter
	 */
	public TwitterErrorRequestException(IError error) {
		this.error = error;
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
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