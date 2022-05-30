package relation;

import java.util.ArrayList;

import member.Member;

public class Relation {
	
	private long id;
	ArrayList<member.Member> member;
	ArrayList<String> tag;
	  
	public Relation(long id, ArrayList<Member> member, ArrayList<String> tag) {
		this.id = id;
		this.member = member;
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<member.Member> getMember() {
		return member;
	}

	public void setMember(ArrayList<member.Member> member) {
		this.member = member;
	}

	public ArrayList<String> getTag() {
		return tag;
	}

	public void setTag(ArrayList<String> tag) {
		this.tag = tag;
	}

	

}
