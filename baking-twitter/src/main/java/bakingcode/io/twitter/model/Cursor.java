package bakingcode.io.twitter.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Cursor of a generic class provides cursoring capabilities around a list of instances returned by
 * Twitter API as a list. https://dev.twitter.com/docs/misc/cursoring
 */
public class Cursor {

    /**
     * Previous cursor
     */
	protected long previousCursor;

    /**
     * Next cursor
     */
	protected long nextCursor;

    /**
     * Default constructor
     * @param json
     */
	public Cursor(JSONObject json) {
				
		try {
			
			previousCursor = json.getLong("previous_cursor");
			nextCursor = json.getLong("next_cursor");

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    /**
     * Gets the previous cursor
     * @return
     */
	public long getPreviousCursor() {
		return previousCursor;
	}

    /**
     * Gets the next cursor
     * @return
     */
	public long getNextCursor() {
		return nextCursor;
	}

}
