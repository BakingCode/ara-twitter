/*
 * Copyright (c) 2012 BakingCode. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * BakingCode ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with BakingCode.
 */ 
package com.bakingcode.io.twitter.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class Tools {

	/**
     * An empty String for re-use in the Application
     */
    public static final String EMPTY_STRING = "";
    
    /**
     * Large twitter date format sample: "Wed Aug 27 13:08:45 +0000 2008"
     */
    private static final String LARGE_TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
    
    /**
     * Simple date format for match LARGET_TWITTER_DATE_FORMAT
     */
    private static final SimpleDateFormat sdfLargeTwitterDateFormat = new SimpleDateFormat(LARGE_TWITTER_DATE_FORMAT, Locale.ENGLISH);
	
	/**
     * Checks if a string is null or empty
     * @param strCheck the string to check
     * @return if the string is null or empty 
     */
    public static boolean isEmpty(String strCheck) {
    	return strCheck == null || EMPTY_STRING.equals(strCheck.trim());
    }
    
    /**
     * Checks if a string is not null and have more than 1 character that is not a space " "
     * @param strCheck the string to check
     * @return true if the string is not empty
     */
    public static boolean isNotEmpty(String strCheck) {
    	return strCheck != null && !EMPTY_STRING.equals(strCheck.trim());
    }
    
    /**
     * Checks if a Collection is null or empty 
     * @param c collection to check
     * @return if the collection is nul or empty
     */
    public static boolean isEmpty(Collection<?> c) {
    	return c == null || c.size() == 0;
    }
    
    /**
     * Checks if a Collection is not null and have more than 1 object 
     * @param c collection to check
     * @return true if the collection is not empty
     */
    public static boolean isNotEmpty(Collection<?> c) {
    	return c != null && c.size() > 0;
    }
    
    /**
     * Checks if a Array is not null and have more tha 1 object
     * @param c array to check
     * @return true if the collection is empty
     */
    public static boolean isEmpty(Object[] c) {
    	return c == null || c.length == 0;
	}
	
    /**
     * Parses a large dat eof twitter like this:
     * "Wed Aug 27 13:08:45 +0000 2008"
     * @param largeDate large format date of twitter
     * @return a new date
     */
    public static Date parseLargeTweetDate(String largeDate) {
    	
    	try {
			return sdfLargeTwitterDateFormat.parse(largeDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
	/**
     * Apache Jakarta commons
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 *
	 * <p>No delimiter is added before or after the list.
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 *
	 * <pre>
	 * StringUtils.join(null, *)               = null
	 * StringUtils.join([], *)                 = ""
	 * StringUtils.join([null], *)             = ""
	 * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.join(["a", "b", "c"], null) = "abc"
	 * StringUtils.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 *
	 * @param array  the array of values to join together, may be null
	 * @param separator  the separator character to use
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(Object[] array, char separator) {
		
	    if (array == null) {
	        return null;
	    }
	    
	    int arraySize = array.length;
	    int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
	    StringBuilder buf = new StringBuilder(bufSize);
	
	    for (int i = 0; i < arraySize; i++) {
	    	
	        if (i > 0) {
	            buf.append(separator);
	        }
	        
	        if (array[i] != null) {
	            buf.append(array[i]);
	        }
	        
	    }

	    return buf.toString();
	}
	
	/**
     * Apache Jakarta commons
	 * <p>Joins the elements of the provided array into a single String
	 * containing the provided list of elements.</p>
	 *
	 * <p>No delimiter is added before or after the list.
	 * Null objects or empty strings within the array are represented by
	 * empty strings.</p>
	 *
	 * <pre>
	 * StringUtils.join(null, *)               = null
	 * StringUtils.join([], *)                 = ""
	 * StringUtils.join([null], *)             = ""
	 * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
	 * StringUtils.join(["a", "b", "c"], null) = "abc"
	 * StringUtils.join([null, "", "a"], ';')  = ";;a"
	 * </pre>
	 *
	 * @param array  the array of values to join together, may be null
	 * @param separator  the separator character to use
	 * @return the joined String, <code>null</code> if null array input
	 */
	public static String join(long[] array, char separator) {
		
	    if (array == null) {
	        return null;
	    }
	    
	    int arraySize = array.length;
	    StringBuilder buf = new StringBuilder();
	
	    for (int i = 0; i < arraySize; i++) {
	    	
	        if (i > 0) {
	            buf.append(separator);
	        }
	        
            buf.append(Long.toString(array[i]));
	        
	    }

	    return buf.toString();
	}

}
