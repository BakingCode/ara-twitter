package bakingcode.io.twitter;

/**
 * Twitter Urls
 */
enum Urls {

    /**
     * https://dev.twitter.com/docs/api/1.1/post/statuses/update
     */
    URL_STATUSES_UPDATE("https://api.twitter.com/1.1/statuses/update.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/search/tweets
     */
    URL_SEARCH("https://api.twitter.com/1.1/search/tweets.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/users/lookup
     */
    URL_USER_LOOKUP("https://api.twitter.com/1.1/users/lookup.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/help/configuration
     */
    URL_API_CONFIGURATION("https://api.twitter.com/1.1/help/configuration.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/statuses/show/%3Aid
     */
    URL_STATUSES_SHOW("https://api.twitter.com/1.1/statuses/show.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/users/show
     */
    URL_USER_SHOW("https://api.twitter.com/1.1/users/show.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/account/verify_credentials
     */
    URL_VERIFY_CREDENTIALS("https://api.twitter.com/1.1/account/verify_credentials.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/statuses/home_timeline
     */
    URL_HOME_TIMELINE("https://api.twitter.com/1.1/statuses/home_timeline.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/statuses/user_timeline
     */
    URL_USER_TIMELINE("https://api.twitter.com/1.1/statuses/user_timeline.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/statuses/mentions_timeline
     */
    URL_MENTIONS_TIMELINE("https://api.twitter.com/1.1/statuses/mentions_timeline.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/favorites/list
     */
    URL_FAVORITES_LIST("https://api.twitter.com/1.1/favorites/list.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/favorites/destroy
     */
    URL_DESTROY_FAVORITE("https://api.twitter.com/1.1/favorites/destroy.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/favorites/create
     */
    URL_CREATE_FAVORITE("https://api.twitter.com/1.1/favorites/create.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/statuses/retweet/%3Aid
     */
    URL_RETWEET("https://api.twitter.com/1.1/statuses/retweet/%s.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/statuses/retweets/%3Aid
     */
    URL_RETWEETS("https://api.twitter.com/1.1/statuses/retweets/%s.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/statuses/update_with_media
     */
    URL_UPDATE_WITH_MEDIA("https://api.twitter.com/1.1/statuses/update_with_media.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/friends/ids
     */
    URL_GET_FRIENDS("https://api.twitter.com/1.1/friends/ids.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/friendships/lookup
     */
    URL_LOOKUP_FRIENDSHIPS("https://api.twitter.com/1.1/friendships/lookup.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/followers/ids
     */
    URL_GET_FOLLOWERS("https://api.twitter.com/1.1/followers/ids.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/friendships/create
     */
    URL_CREATE_FRIENDSHIP("https://api.twitter.com/1.1/friendships/create.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/friendships/destroy
     */
    URL_DESTROY_FRIENDSHIP("https://api.twitter.com/1.1/friendships/destroy.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/trends/closest
     */
    URL_CLOSEST_PLACES("https://api.twitter.com/1.1/trends/closest.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/trends/place
     */
    URL_TRENDS_PLACE("https://api.twitter.com/1.1/trends/place.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/statuses/retweets_of_me
     */
    URL_RETWEETS_OF_ME("https://api.twitter.com/1.1/statuses/retweets_of_me.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/get/application/rate_limit_status
     */
    URL_RATE_LIMIT_STATUS("https://api.twitter.com/1.1/application/rate_limit_status.json"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/oauth2/token
     */
    URL_OBTAIN_BEARER_TOKEN("https://api.twitter.com/oauth2/token"),

    /**
     * https://dev.twitter.com/docs/api/1.1/post/oauth2/invalidate_token
     */
    URL_INVALIDATE_BEARER_TOKEN("https://api.twitter.com/oauth2/invalidate_token");

    /**
     * Url
     */
    private String url;

    /**
     * Default constructor
     * @param url url to set
     */
    private Urls(String url) {
        this.url = url;
    }

    /**
     * Gets the url to consume
     * @return the url to consume
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }
}
