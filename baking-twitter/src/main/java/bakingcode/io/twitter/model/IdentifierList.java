package bakingcode.io.twitter.model;

import java.util.ArrayList;

import static bakingcode.io.twitter.tools.TwitterLogging.LT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IdentifierList extends Cursor{

	/**
	 * Logging tag
	 */
	private static final String TAG = "IdentifierList";
	
	private ArrayList<Id> idList;
	private long[] idArray;
	
	public IdentifierList(JSONObject json) {
		super(json);
		if (json != null) {
			idList = new ArrayList<Id>();
			try {
				JSONArray array = json.getJSONArray("ids");
				if (array != null) {
					int size = array.length();
					idArray = new long[size];
					Id id;
					for (int i = 0; i < size; i++) {
						id = null;
						Object item = array.get(i);
						if (item instanceof String) {
							String idStr = (String) item;
							id = new Id(idStr);							
						} else if (item instanceof Long) {
							long idLong = (Long) item;
							id = new Id(idLong);							
						} else if (item instanceof Integer) {							
							long idLong = (Integer) item;
							id = new Id(idLong);
							
						}
						if (id != null) {
							idList.add(id);			
							idArray[i] = id.getId();
						}
						
					}
				}				
				
			} catch (JSONException e) {
				LT(TAG, e);
			}
		}
	}
	
	public void add(Id id) {
		if (idList != null) {
			idList.add(id);
		}
	}
	
	public void add(IdentifierList anotherList) {
		if (idList == null) {
			idList = new ArrayList<Id>();
		}
		
		if (anotherList != null) {
			idList.addAll(anotherList.idList);
		}
	}
	
	
	public int size() {
		return (idList != null) ? idList.size() : 0;
	}
	
	public Id elementAt(int index) {
		return (idList != null) ? idList.get(index) : null;
	}
	
	public int indexOf(Id object) {
		return (idList != null) ? idList.indexOf(object) : -1;
	}
	
	public void clear() {
		if (idList != null) {
			idList.clear();
		}
	}
	
	public long[] getIDs() {
		return this.idArray;
	}

}
