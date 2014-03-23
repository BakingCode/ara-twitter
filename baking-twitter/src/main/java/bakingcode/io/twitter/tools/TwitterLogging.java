/*
 * Copyright (c) 2012 BakingCode. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * BakingCode ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with BakingCode.
 */ 
package bakingcode.io.twitter.tools;

import android.util.Log;
import bakingcode.io.twitter.BuildConfig;

/**
 * Class that provides a simple logging with short methods like "L" or "LT".
 * import this class memebers statically to make the method calls L(TAG, "msg");
 */
public class TwitterLogging {

	/**
	 * Logs a message with simply L method
	 *
	 * @param tag tag to log
	 * @param s string to log
	 */
	public static void L(String tag, String s) {
		
		if (BuildConfig.DEBUG) {
			Log.d(tag, s);
		}
	}
	
	/**
	 * Logs a throwable with simply LT method
	 *
	 * @param tag tag to log
	 * @param e throwable
	 */
	public static void LT(String tag, Throwable e) {

        if (BuildConfig.DEBUG) {
		    L(tag, Log.getStackTraceString(e));
        }
		
	}
	
}
