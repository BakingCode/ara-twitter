package bakingcode.io.twitter;

import org.apache.http.message.BasicNameValuePair;

/**
 * Paramters to make request with twitter
 */
enum Parameters {

    /**
     * Status parameter
     */
    STATUS("status"),

    /**
     * In reply to status id parameter
     */
    IN_REPLY_TO_STATUS_ID("in_reply_to_status_id"),

    /**
     * Latitude parameter
     */
    LATITUDE("lat"),

    /**
     * Longitude parameter
     */
    LONGITUDE("long"),

    /**
     * Display coordinates parameter
     */
    DISPLAY_COORDINATES("display_coordinates"),

    /**
     * Query parameter
     */
    QUERY("q"),

    /**
     * Geocode parameter
     */
    GEOCODE("geocode"),

    /**
     * Since id parameter
     */
    SINCE_ID("since_id"),

    /**
     * Max id parameter
     */
    MAX_ID("max_id"),

    /**
     * Screen name parameter
     */
    SCREEN_NAME("screen_name"),

    /**
     * User id parameter
     */
    USER_ID("user_id"),

    /**
     * Include entities parameter
     */
    INCLUDE_ENTITIES("include_entities"),

    /**
     * ID parameter
     */
    ID("id"),

    /**
     * Skip status parameter
     */
    SKIP_STATUS("skip_status"),

    /**
     * Count parameter
     */
    COUNT("count"),

    /**
     * Cursor parameter
     */
    CURSOR("cursor"),

    /**
     * Count parameter
     */
    STRINGIFY_IDS("stringify_ids"),

    /**
     * Follow parameter
     */
    FOLLOW("follow"),

    /**
     * exclude replies parameter
     */
    EXCLUDE_REPLIES("exclude_replies"),

    /**
     * Include rts parameter
     */
    INCLUDE_RTS("include_rts"),

    /**
     * Exclude parameter
     */
    EXCLUDE("exclude"),

    /**
     * Resources parameter
     */
    RESOURCES("resources");

    /**
     * Parameter
     */
    private String param;

    /**
     * Default constructor
     * @param param parameter
     */
    private Parameters(String param) {
        this.param = param;
    }

    /**
     * Gets the parameter to set in twitter requests
     * @return the parameter to set in twitter requests
     */
    public String getParam() {
        return param;
    }

    /**
     * Gets a BasicNameValuePair with this parameter and a value
     * @param value the value to set
     * @return a BasicNameValuePair with this parameter and a value
     */
    public BasicNameValuePair valuePairWith(String value) {
        return new BasicNameValuePair(param, value);
    }

    @Override
    public String toString() {
        return param;
    }
}
