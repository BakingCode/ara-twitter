package bakingcode.io.twitter;

import android.os.AsyncTask;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * This class provides oauth abstraction. To use this:
 * 
 * - Create a new instance with a simple new TwitterOauth().
 * - Retrieve request token (retrieveRequestToken())
 * - When received message in OauthLister load the url received and authorize the app (user fill fields)
 * - call accessToken with the verifier received
 * - Wait for message access token received in the listener
 * - Ready to use the token and token secret.
 *
 */
public class TwitterOAuth {
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constants
	// ///////////////////////////////////////////////////////////////////////////

    /**
     * Logging tag
     */
    private static final String TAG = "TwitterOAuth";

	/**
	 * Twitter request URL
	 */
	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";

	/**
	 * Twitter access URL
	 */
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";

	/**
	 * Twitter Authorize URL
	 */
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Oauth Vars
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * OAuth consumer for provide signing on petitions
	 */
	private OAuthConsumer consumer = null;
	
	/**
	 * Oauth provider
	 */
	private CommonsHttpOAuthProvider provider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
	
	/**
	 * Default constructor. Provide a consumer key and consumer secret to retrieve token and token secret. 
	 * 
	 * @param consumerKey consumer key
	 * @param consumerSecret consumer secret
	 */
	public TwitterOAuth(String consumerKey, String consumerSecret) {
		
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		provider.setOAuth10a(true);

	}

	/**
	 * Retrieves the request token
	 * 
	 * @param oauthUrlCallBack configured oauth callback
	 * @param listener listener
	 */
	public void retrieveRequestToken(String oauthUrlCallBack, OAuthListener listener) {
		
		new RetrieveRequestToken(listener).execute(oauthUrlCallBack);
		
	}
	
	/**
	 * Retrieves the access token
	 * 
	 * @param verifier verifier
	 * @param listener listener
	 */
	public void retrieveAccessToken(String verifier, OAuthListener listener) {
		
		new RetrieveAccessToken(listener).execute(verifier);
		
	}

	/**
	 * AsyncTask to retrieve access token
	 */
	class RetrieveAccessToken extends AsyncTask<String, Void, Void> {

		/**
		 * Listener to notify OauthMessages
		 */
		private OAuthListener listener;
		
		public RetrieveAccessToken(OAuthListener listener) {
			this.listener = listener;
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			String verifier = params[0];
			try {
				
				provider.retrieveAccessToken(consumer, verifier);
				
			} catch (Throwable e) {
				LT(TAG, e);
			}
			
			
			return null;
		}
		
		protected void onPostExecute(Void result) {
			
			// Request twitter
			String token = consumer.getToken();
			String tokenSecret = consumer.getTokenSecret();
			
			if (listener != null) {
				listener.onAccessTokenReceived(token, tokenSecret);
			}
			
		}
		
	}
    
	/**
	 * Asynctask to retrieve request token
	 */
	class RetrieveRequestToken extends AsyncTask<String, Void, String> {
		
		/**
		 * OAuthListener for notify oauth messages
		 */
		private OAuthListener listener;
		
		public RetrieveRequestToken(OAuthListener listener) {
			this.listener = listener;
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			String oauthCallBack = params[0];
			
			try {
				
				return provider.retrieveRequestToken(consumer, oauthCallBack);
				
			} catch (Throwable e) {
                LT(TAG, e);
			}
			
			return null;
		}
		
		protected void onPostExecute(final String authUrl) {
			
			if (listener != null) {
				listener.onRequestTokenReceived(authUrl);
			}
			
		}
		
	}

	/**
	 * Oauth Message listener
	 */
	public interface OAuthListener {

		/**
		 * Called when the request token is received
		 * @param requestToken requestToken
		 */
		public void onRequestTokenReceived(String requestToken);
		
		/**
		 * Called when the access token is received
		 * @param token token 
		 * @param tokenSecret tokenSecret
		 */
		public void onAccessTokenReceived(String token, String tokenSecret);
		
	}
	
}
