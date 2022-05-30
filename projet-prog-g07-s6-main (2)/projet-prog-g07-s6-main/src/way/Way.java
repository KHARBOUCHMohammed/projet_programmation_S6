package way;

import java.util.ArrayList;

public class Way {
	
	private long id;
	ArrayList<Long> ref;
	ArrayList<String> tag;
	 
	public Way(long id, ArrayList<Long> ref, ArrayList<String> tag) {
		super();
		this.id = id;
		this.ref = ref;
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<Long> getRef() {
		return ref;
	}

	public void setRef(ArrayList<Long> ref) {
		this.ref = ref;
	}

	public ArrayList<String> getTag() {
		return tag;
	}

	public void setTag(ArrayList<String> tag) {
		this.tag = tag;
	}
	
	

}
