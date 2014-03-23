package com.bakingcode.io.twitter.exceptions;

import com.bakingcode.io.twitter.model.IError;

/**
 * This exception is used for raise an unhandled communications exception 
 */
public class TwitterCommunicationException extends Exception implements IError {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 9024095081357910969L;

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Exception Stack trace
	 */
	private String stackTrace;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor
	 */
	public TwitterCommunicationException(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	@Override
	public String toString() {
		return stackTrace;
	}
	
}
