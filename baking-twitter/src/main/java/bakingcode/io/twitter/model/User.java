package bakingcode.io.twitter.model;

import bakingcode.io.twitter.tools.Tools;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

/**
 * Users can be anyone or anything. They tweet, follow, create lists, have a home_timeline, can be mentioned, and can be looked up in bulk.
 */
public class User implements Serializable {

    // ///////////////////////////////////////////////////////////////////////////
    // Constants
    // ///////////////////////////////////////////////////////////////////////////

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = -1265462438314717670L;
	
	/**
	 * Private log tag
	 */
	private static final String TAG = "User";

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The UTC datetime that the user account was created on Twitter.
	 */
	private Date createdAt;
	
	/**
	 * When true, indicates that the user has not altered the theme or background of their user profile.
	 */
	private boolean defaultProfile;
	
	/**
	 * When true, indicates that the user has not uploaded their own avatar and a default egg avatar is used instead.
	 */
	private boolean defaultProfileImage;
	
	/**
	 * Nullable. The user-defined UTF-8 string describing their account.
	 */
	private String description;
	
	/**
	 * The number of tweets this user has favorited in the account's lifetime. British spelling used in the field name for historical reasons.
	 */
	private int favouritesCount;
	
	/**
	 * Nullable. Perspectival. Deprecated. When true, indicates that the authenticating user is following this user. Some false negatives are possible when set to "false," but these false negatives are increasingly being represented as "null" instead. 
	 */
	private Boolean following;
	
	/**
	 * The number of followers this account currently has. Under certain conditions of duress, this field will temporarily indicate "0."
	 */
	private int followersCount;
	
	/**
	 * The number of users this account is following (AKA their "followings"). Under certain conditions of duress, this field will temporarily indicate "0."
	 */
	private int friendsCount;
	
	/**
	 * The integer representation of the unique identifier for this User. This number is greater than 53 bits and some programming languages may have difficulty/silent defects in interpreting it. Using a signed 64 bit integer for storing this identifier is safe. Use id_str for fetching the identifier to stay on the safe side. See Twitter IDs, JSON and Snowflake.
	 */
	private long id;
	
	/**
	 * The number of public lists that this user is a member of.
	 */
	private int listedCount;
	
	/**
	 * Nullable. The user-defined location for this account's profile. Not necessarily a location nor parseable. This field will occasionally be fuzzily interpreted by the Search service.
	 */
	private String location;
	
	/**
	 * The name of the user, as they've defined it. Not necessarily a person's name. Typically capped at 20 characters, but subject to change.
	 */
	private String name;
	
	/**
	 * A HTTP-based URL pointing to the user's avatar image. See User Profile Images and Avatars.
	 */
	private String profileImageUrl;
	
	/**
	 * When true, indicates that this user has chosen to protect their Tweets. See About Public and Protected Tweets.
	 */
	private boolean protectedAccount;
	
	/**
	 * The screen name, handle, or alias that this user identifies themselves with. screen_names are unique but subject to change. Use id_str as a user identifier whenever possible. Typically a maximum of 15 characters long, but some historical accounts may exist with longer names.
	 */
	private String screenName;
	
	/**
	 * The number of tweets (including retweets) issued by the user.
	 */
	private int statusesCount;
	
	/**
	 * Nullable. A URL provided by the user in association with their profile.
	 */
	private String url;

    /**
     * Profile background color
     */
    private String profileBackgroundColor;

    /**
     * Profile background image url
     */
    private String profileBackgroundImageUrl;

    /**
     * Profile background image url in https
     */
    private String profileBackgroundImageUrlHttps;

    /**
     * Profile banner url
     */
    private String profileBannerUrl;

    /**
     * Profile background tile
     */
    private Boolean profileBackgroundTile;

	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Default constructor. It creates a new empty user instance. 
	 */
	public User() { }
	
	/**
	 * Parses a Tweet object and returns a new instance of user
	 * 
	 * @param response JSON response
	 * @return a new User instance
	 */
	public static User parse(String response) {
		
		if (Tools.isNotEmpty(response)) {
			
			try {
				
				JSONObject jsonObject = new JSONObject(response);
				User u = new User(jsonObject);
				return u;
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * JSON parser constructor. It parses a json object to make a new user instance
	 * 
	 * @param json jsonObject to parse
	 */
	public User(JSONObject json) {
		
		try {
			
			createdAt = Tools.parseLargeTweetDate(json.getString("created_at"));
			defaultProfile = json.getBoolean("default_profile");
			defaultProfileImage = json.getBoolean("default_profile_image");

            if (!json.isNull("description")) {
			    description = json.getString("description");
            }

			favouritesCount = json.getInt("favourites_count");
			
			if (!json.isNull("following")) {
				following = json.getBoolean("following");
			}
			
			followersCount = json.getInt("followers_count");
			friendsCount = json.getInt("friends_count");
			id = json.getLong("id");
			listedCount = json.getInt("listed_count");
			location = json.getString("location");

            if (!json.isNull("name")) {
			    name = json.getString("name");
            }

			profileImageUrl = json.getString("profile_image_url");
			screenName = json.getString("screen_name");
			statusesCount = json.getInt("statuses_count");

            if (!json.isNull("url")) {
			    url = json.getString("url");
            }

            profileBackgroundTile = json.getBoolean("profile_background_tile");

            if (!json.isNull("profile_background_image_url")) {
                profileBackgroundImageUrl = json.getString("profile_background_image_url");
            }

            if (!json.isNull("profile_background_image_url_https")) {
                profileBackgroundImageUrlHttps = json.getString("profile_background_image_url_https");
            }

            if (!json.isNull("profile_banner_url")) {
                profileBannerUrl = json.getString("profile_banner_url");
            }

            if (!json.isNull("profile_background_color")) {
                profileBackgroundColor = json.getString("profile_background_color");
            }

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the defaultProfile
	 */
	public boolean isDefaultProfile() {
		return defaultProfile;
	}

	/**
	 * @param defaultProfile the defaultProfile to set
	 */
	public void setDefaultProfile(boolean defaultProfile) {
		this.defaultProfile = defaultProfile;
	}

	/**
	 * @return the defaultProfileImage
	 */
	public boolean isDefaultProfileImage() {
		return defaultProfileImage;
	}

	/**
	 * @param defaultProfileImage the defaultProfileImage to set
	 */
	public void setDefaultProfileImage(boolean defaultProfileImage) {
		this.defaultProfileImage = defaultProfileImage;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the favouritesCount
	 */
	public int getFavouritesCount() {
		return favouritesCount;
	}

	/**
	 * @param favouritesCount the favouritesCount to set
	 */
	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	/**
	 * @return the following
	 */
	public Boolean getFollowing() {
		return following;
	}

	/**
	 * @param following the following to set
	 */
	public void setFollowing(Boolean following) {
		this.following = following;
	}

	/**
	 * @return the followersCount
	 */
	public int getFollowersCount() {
		return followersCount;
	}

	/**
	 * @param followersCount the followersCount to set
	 */
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	/**
	 * @return the friendsCount
	 */
	public int getFriendsCount() {
		return friendsCount;
	}

	/**
	 * @param friendsCount the friendsCount to set
	 */
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the listedCount
	 */
	public int getListedCount() {
		return listedCount;
	}

	/**
	 * @param listedCount the listedCount to set
	 */
	public void setListedCount(int listedCount) {
		this.listedCount = listedCount;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the protectedAccount
	 */
	public boolean isProtectedAccount() {
		return protectedAccount;
	}

	/**
	 * @param protectedAccount the protectedAccount to set
	 */
	public void setProtectedAccount(boolean protectedAccount) {
		this.protectedAccount = protectedAccount;
	}

	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the statusesCount
	 */
	public int getStatusesCount() {
		return statusesCount;
	}

	/**
	 * @param statusesCount the statusesCount to set
	 */
	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the profileImageUrl
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	/**
	 * @param profileImageUrl the profileImageUrl to set
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

    public Boolean getProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    public void setProfileBackgroundTile(Boolean profileBackgroundTile) {
        this.profileBackgroundTile = profileBackgroundTile;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public void setProfileBackgroundColor(String profileBackgroundColor) {
        this.profileBackgroundColor = profileBackgroundColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getProfileBackgroundImageUrlHttps() {
        return profileBackgroundImageUrlHttps;
    }

    public void setProfileBackgroundImageUrlHttps(String profileBackgroundImageUrlHttps) {
        this.profileBackgroundImageUrlHttps = profileBackgroundImageUrlHttps;
    }

    public String getProfileBannerUrl() { return profileBannerUrl; }

    public void setProfileBannerUrl(String profileBannerUrl) { this.profileBannerUrl = profileBannerUrl; }



}
