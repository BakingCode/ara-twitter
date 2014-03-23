package bakingcode.io.twitter.model;

import java.io.Serializable;

/**
 * Id holds a user identifier that can be expressed as a long or/and as an integer
 */
public class Id implements Serializable {

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 57029327663780150L;

	// ///////////////////////////////////////////////////////////////////////////
	// Fields
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * The long representation
	 */
	private long id;
	
	/**
	 * The string representation
	 */
	private String idStr;
	
	// ///////////////////////////////////////////////////////////////////////////
	// Constructors
	// ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructor from long
	 * @param id the user identifier as a long
	 */
	public Id(long id) {
		
		this.id = id;
		this.idStr = String.valueOf(id);
		
	}
	
	/**
	 * Constructor from string
	 * @param idStr the user identifier as a string
	 */
	public Id(String idStr) {
		this.id = Long.parseLong(idStr);
		this.idStr = idStr;
	}		
	
	// ///////////////////////////////////////////////////////////////////////////
	// Get & Set
	// ///////////////////////////////////////////////////////////////////////////

	/**
	 * Get the identifier as a long
	 * @return the identifier as a long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the identifier as a long
	 * @param id the identifier as a long
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get the identifier as a string
	 * @return the identifier as a string
	 */
	public String getIdStr() {
		return idStr;
	}

	/**
	 * Set the identifier as a string
	 * @param idStr the identifier as a string
	 */
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	


	
	
	
}
