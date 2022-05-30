package nodeInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RelationInformation extends Information{
	
	
	ArrayList<relation.Relation> ListOfRelation;

	public RelationInformation() {
		// TODO Auto-generated constructor stub
		ListOfRelation = new ArrayList<>();
		
	}

	public void initialise(ArrayList<relation.Relation> r) throws NumberFormatException, IOException {
		
        File file = new File("src/com/projet/g07/Carte.fxml");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);
        String line;
        member.Member elementMember = null;
        relation.Relation elementRelation = null;
        
        while ((line = reader.readLine()) != null) {
        	if(line.contains("<relation")) {
        		String idRelation = splitString(line,"id=");
        		
        		ArrayList<member.Member> member = new ArrayList<>();
        		line = reader.readLine();
        		while(line.contains("<member")) {
        			String ref = splitString(line,"ref=");
	      			String role = splitString(line,"role=");
	      			String type = splitString(line,"type=");
	      			line = reader.readLine();
	      			elementMember = new member.Member(Long.parseLong(ref),role,type);
	      			member.add(elementMember);
	      		}
        		
                ArrayList<String> temporyTag = new ArrayList<>();
                line = reader.readLine();
	      		while(line.contains("<tag")) {
	      			String k = splitString(line,"k=");
	      			temporyTag.add(k);
	      			String v = splitString(line,"v=");
	      			line = reader.readLine();
	      			temporyTag.add(v);
	      		}
	      		elementRelation = new relation.Relation(Long.parseLong(idRelation),member,temporyTag);
            	r.add(elementRelation);
        	}
        }
        reader.close();
    }
    
	
	public static void main(String[] args) throws IOException {
		
		RelationInformation rI = new RelationInformation();
		rI.initialise(rI.ListOfRelation);
		int x = 0;
		
		while(x < rI.ListOfRelation.size()){
			System.out.println("id :"+ rI.ListOfRelation.get(x).getId());
			int y=0, z=0;
			while(y<rI.ListOfRelation.get(x).getMember().size()) {
				System.out.println("member ref : " + rI.ListOfRelation.get(x).getMember().get(y).getRef());
				System.out.println("member role : " + rI.ListOfRelation.get(x).getMember().get(y).getRole());
				System.out.println("member type : " + rI.ListOfRelation.get(x).getMember().get(y).getType());
				y++;
			}
			while(z<rI.ListOfRelation.get(x).getTag().size()) {
				 if(z%2==0) {
					 System.out.println("tag k : " + rI.ListOfRelation.get(x).getTag().get(z));
				 }
				 else {
					 System.out.println("tag v : " + rI.ListOfRelation.get(x).getTag().get(z));
				 }
				 
				 z++;
			}
			x++;
		}
	}

}
