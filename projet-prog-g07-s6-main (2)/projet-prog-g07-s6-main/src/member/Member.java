package member;

public class Member {
	
	private long ref;
	private String role;
	private String type;

	public Member(long ref, String role, String type) {
		this.ref = ref;
		this.role = role;
		this.type = type;
	}

	public long getRef() {
		return ref;
	}
 
	public void setRef(long ref) {
		this.ref = ref;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
