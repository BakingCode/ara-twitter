package bakingcode.io.twitter;

import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Base64;
import bakingcode.io.twitter.exceptions.TwitterCommunicationException;
import bakingcode.io.twitter.model.ResponseString;
import bakingcode.io.twitter.model.TwitterError;
import bakingcode.io.twitter.tools.Tools;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static bakingcode.io.twitter.tools.TwitterLogging.L;
import static bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * This class is the responsable of make Http/s petitions to the twitter API. This class don't knows anything
 * about twitter, it only knows how to make petitions and sign with Oauth you can call makeRequestWithParameters
 * for return the petition response
 */
public final class TwitterHttpClient {
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constants
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * UTF-8 encoding
	 */
	public final static String UTF8_ENCODING = "UTF-8";

    /**
     * Logging tag
     */
    private final static String TAG = "TwitterHttpClient";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Login private members
	// ///////////////////////////////////////////////////////////////////////////
		
	/**
	 * OAuth consumer for provide signing on petitions
	 */
	private OAuthConsumer consumer = null;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Twitter private members
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Http client to make requests
	 */
	private DefaultHttpClient httpClient;
	
	/**
	 * Current requests
	 */
	private static final List<HttpRequestBase> currentRequests = new ArrayList<HttpRequestBase>();
	
	/**
	 *  Enum request generator
	 */
	public enum Request {
		
		GET(0),
		POST(1),
		POST_WITH_MEDIA(2);
		
		/**
		 * Rquest type
		 */
		private int rType = -1;
		
		/**
		 * Default constructor
		 * 
		 * @param rType the request type
		 */
		Request(int rType) {
			
			this.rType = rType;
			
		}
		
		/**
		 * Generates a request of the given type
		 * 
		 * @param reqUrl request URL
		 * @return an empty request
		 */
		public HttpRequestBase generateRequest(String reqUrl, List<NameValuePair> valuePairs, File f) {
		
			HttpRequestBase request = null;
			
			switch (rType) {
			
				case 0: // GET request
					
					String finalUri;
					
					if (Tools.isNotEmpty(valuePairs)) {
					
						// Append query parameters in a safe mode and without "touhing" Strings
						Builder builder = Uri.parse(reqUrl).buildUpon();
						
						for (NameValuePair value : valuePairs) {
							builder.appendQueryParameter(value.getName(), value.getValue());
						}
						
						finalUri = builder.build().toString();
						
					} else {
						
						finalUri = reqUrl;
					}
					
					L(TAG, "Generated GET URL:" + finalUri);
					request = new HttpGet(finalUri);
					
					break;
					
				case 1: // POST request
					
					// Set a new entity with parameters
					HttpPost post = new HttpPost(reqUrl);
					
					if (Tools.isNotEmpty(valuePairs)) {
						
						UrlEncodedFormEntity entity = null;
						
						try {
							
							entity = new UrlEncodedFormEntity(valuePairs, UTF8_ENCODING);
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						
						post.setEntity(entity);
						
					}
					
					request = post;
					break;
					
				case 2: // POST with media request
					
					HttpPost mediaPost = new HttpPost(reqUrl);
					MultipartEntity entity = new MultipartEntity(HttpMultipartMode.STRICT);
					mediaPost.setEntity(entity);
					
					// Append image
					FileBody imageBody = new FileBody(f, "image/png");
					entity.addPart("media[]", imageBody);

					// Apped rest of params
					if (Tools.isNotEmpty(valuePairs)) {
					
						try {
							
							Charset utf8Charset = Charset.forName(UTF8_ENCODING);
							
							for (NameValuePair vp : valuePairs) {
								
								entity.addPart(new FormBodyPart(vp.getName(), new StringBody(vp.getValue(), utf8Charset)));

							}
							
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						
					}
					
					request = mediaPost;
					break;
					
			}
			
			return request;
			
		}
		
	}
	
	/**
	 * Creates a twitter consumer by all the secrets
	 * 
	 * @param consumerKey consumer key
	 * @param consumerSecret consumer secret
	 * @param token token
	 * @param tokenSecret token secret
	 */
	public TwitterHttpClient(String consumerKey, String consumerSecret, String token, String tokenSecret) {
		
		// Create default http client
		HttpParams httpParams = new BasicHttpParams();
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", new PlainSocketFactory(), 80));
		registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        
		httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, registry), httpParams);
		
		// Set consumer
		consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
		consumer.setTokenWithSecret(token, tokenSecret);
		
	}
	
	/**
	 * Makes a request with the given parameters
	 * 
	 * @param rType request type
	 * @param url url to make request
	 * @param valuePairs parameters
	 */
	public ResponseString makeRequestWithParameters(Request rType, String url, List<NameValuePair> valuePairs, Map<String, String> headers) {
	
		return makeRequestWithParameters(rType, url, valuePairs, headers, null);
		
	}
	
	/**
	 * Makes a request with the given parameters
	 * 
	 * @param rType request type
	 * @param url url to make request
	 * @param valuePairs parameters
	 */
	public ResponseString makeRequestWithParameters(Request rType, String url, List<NameValuePair> valuePairs) {
	
		return makeRequestWithParameters(rType, url, valuePairs, null);
		
	}

    /**
     * Makes a request with the given parameters
     *
     * @param rType request type
     * @param url url to make request
     * @param valuePairs parameters
     */
    public ResponseString makeRequestWithParameters(Request rType, Urls url, List<NameValuePair> valuePairs) {

        return makeRequestWithParameters(rType, url.getUrl(), valuePairs, null);

    }
	
	/**
	 * Makes a request with the given parameters
	 * 
	 * @param rType request type
	 * @param url url to make request
	 * @param valuePairs parameters
	 * @param f file to upload
	 */
	public ResponseString makeRequestWithParameters(Request rType, String url, List<NameValuePair> valuePairs, Map<String, String> headers, File f) {
		
		// Create request
		HttpRequestBase baseRequest = rType.generateRequest(url, valuePairs, f);

		// Store connection for later abort
		currentRequests.add(baseRequest);
		
		// Add headers
		boolean signPetition = true;
		if (headers != null && headers.size() > 0) {
			
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				
				if (key.equals("Authorization")) {
					signPetition = false;
				}
				
				baseRequest.addHeader(key, headers.get(key));
				
			}
		}

		// Create response 
		ResponseString resp = new ResponseString();
		
		try {

			if (signPetition) {
				
				// Sign petition
				consumer.sign(baseRequest);
				
			}
			
			// Make request
			HttpResponse response = httpClient.execute(baseRequest);
			
			// Read content from response
			L(TAG, "rType: "+ rType.toString() + ", url: " + url + "\n");
			String responseString = EntityUtils.toString(response.getEntity(), UTF8_ENCODING);
			
			if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
				
				TwitterError e = TwitterError.parse(responseString);
				resp.setError(e);
				
			}
			
			resp.setResponseString(responseString);
			
			L(TAG, responseString);
				
		} catch (Throwable e) {
			
			resp.setError(new TwitterCommunicationException(e.getLocalizedMessage()));
			
			LT(TAG, e);
			
		} finally {
			
			if (currentRequests.contains(baseRequest)) {
				currentRequests.remove(baseRequest);
			}
			
			// Close connection
			baseRequest.abort();
			
		}
		
		return resp;
	}
	
	/**
	 * Aborts all current connections
	 */
	public void abortAllConnections() {
		
		synchronized (currentRequests) {
			
			Iterator<HttpRequestBase> it = currentRequests.iterator();

			while(it.hasNext()) {
	
				try {
					
					it.next().abort();
					it.remove();
					
				} catch (Throwable e) {
                    LT(TAG, e);
                }
				
			}
		}
		
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// OAuth utility methods
	// ///////////////////////////////////////////////////////////////////////////
	
    /**
     * Get oauth consumer
     * @return the ouath consumer
     */
    public OAuthConsumer getConsumer() {
        return consumer;
    }
    
    /**
     * Gets Bearer token of actual consumer
     * @return bearer token
     */
    public String getOauthBearerToken() {
    	
    	try {
			return URLEncoder.encode(consumer.getConsumerKey(), UTF8_ENCODING) + ":" + URLEncoder.encode(consumer.getConsumerSecret(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    /**
     * Gets Base64 encoded Bearer oauth token
     * @return Base64 encoded Bearer oauth token
     */
    public String getOauthBase64BearerToken() {
    	return Base64.encodeToString(getOauthBearerToken().getBytes(), Base64.DEFAULT);
    }

}
