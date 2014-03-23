package com.bakingcode.io.twitter.model;

import com.bakingcode.io.twitter.model.ApiMethod.Method;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * Api Resource Family representing a resource family of the Twitter API.
 */
public class ApiResourceFamily{

	/**
	 * Logging tag
	 */
	private static final String TAG = "ApiResourceFamily";
	
	// ///////////////////////////////////////////////////////////////////////////
	// Logic
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Resource families
	 */
	public enum ResourceFamily {

		ACCOUNT("account"),
		APPLICATION("application"),
		BLOCKS("blocks"),
		DIRECT_MESSAGES("direct_messages"),
		FAVOURITES("favorites"),
		FOLLOWERS("followers"),
		FRIENDS("friends"),
		FRIENDSHIPS("friendships"),
		GEO("geo"),
		HELP("help"),
		LISTS("lists"),
		SAVED_SEARCHES("saved_searches"),
		SEARCH("search"),
		STATUSES("statuses"),
		TRENDS("trends"),
		USERS("users"),
		NULL(null);
		
		/**
		 * Name of the resource family
		 */
		private String name;
		
		/**
		 * Methods associated to the family
		 */
		private List<Method> methods;

		/**
		 * Constructor. Initializes the methods of the resource family.
		 * 
		 * @param name Name of the resource family
		 */
		private ResourceFamily(String name) {
			this.name = name;
			this.methods = Method.getMethodsStartWith(name);
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the methods
		 */
		public List<Method> getMethods() {
			return methods;
		}
		
	}

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The resource family value
	 */
	private ResourceFamily resourceFamily;
	
	/**
	 * The list of methods
	 */
	private List<ApiMethod> methods;

	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor
	 * 
	 * @param json Json object
	 * @param resource Resource family
	 */
	public ApiResourceFamily(JSONObject json, ResourceFamily resource) {

		resourceFamily = resource;
		if (json != null) {
			methods = new ArrayList<ApiMethod>();
			try {
				// Get the resource family object
				JSONObject jsonRes = json.getJSONObject(resource.getName());

				JSONObject jsonMeth;
				ApiMethod apiMethod;
				// For all methods in the resource family..
				for (Method meth : resource.getMethods()) {
					// ..add a new method
					jsonMeth = jsonRes.getJSONObject(meth.getName());
					apiMethod = new ApiMethod(jsonMeth, meth);
					methods.add(apiMethod);
				}
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
		}
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Methods
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * Parses a JSON string to a ApiResourceFamily list
	 * @param response Json string
	 * @param resourceFamilies List resource families used in the request
	 * @return ApiResourceFamily list
	 */
	public static List<ApiResourceFamily> parseList(String response, List<String> resourceFamilies) {
		
		try {
			
			JSONObject json = new JSONObject(response);
			List<ApiResourceFamily> resources = new ArrayList<ApiResourceFamily>();
			
			// Get "resources" object
			JSONObject jsonResources = json.getJSONObject("resources");
			ApiResourceFamily newRes;
			// For each resource family used in the request..
			Locale locale = Locale.getDefault();
			for (String res : resourceFamilies) {
				// ..get its object from the json object and create a new ApiResourceFamily
				newRes = new ApiResourceFamily(jsonResources.getJSONObject(res),
						ResourceFamily.valueOf(res.toUpperCase(locale)));
				resources.add(newRes);
			}
			
			return resources;
			
		} catch (JSONException e) {
			LT(TAG, e);
		}
		
		return null;
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * @return the resourceFamily
	 */
	public ResourceFamily getResourceFamily() {
		return resourceFamily;
	}

	/**
	 * @param resourceFamily the resourceFamily to set
	 */
	public void setResourceFamily(ResourceFamily resourceFamily) {
		this.resourceFamily = resourceFamily;
	}

	/**
	 * @return the methods
	 */
	public List<ApiMethod> getMethods() {
		return methods;
	}

	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<ApiMethod> methods) {
		this.methods = methods;
	}

}
