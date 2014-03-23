package bakingcode.io.twitter.exceptions;

public class TwitterException extends RuntimeException {

	/**
	 * Default serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new Twitter Exception
	 * @param msg message to show
	 */
	public TwitterException(String msg) {
		super(msg);
	}
}
