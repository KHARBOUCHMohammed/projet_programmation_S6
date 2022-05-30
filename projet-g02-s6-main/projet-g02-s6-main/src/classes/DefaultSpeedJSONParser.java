package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DefaultSpeedJSONParser {

	private JSONObject baseFile;
	
	public DefaultSpeedJSONParser() {
		String path = "src/application/default_speeds.json";
        File f = new File(path);
        
        if (f.exists()){
            InputStream is;
			try {
				is = new FileInputStream(path);
	            String jsonTxt;
	            jsonTxt = IOUtils.toString(is, "UTF-8");
	            this.baseFile = new JSONObject(jsonTxt);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} 

        }
	}

	public int getMaxSpeed(String speedKey) throws JSONException {
		String[] keys = speedKey.split(":");
		JSONArray jsonArray = this.baseFile.getJSONArray(keys[0]);
		for (int i=0 ; i<jsonArray.length() ; i++) {

		    if (jsonArray.get(i) instanceof JSONObject) {
			    JSONObject tempObject = jsonArray.getJSONObject(i);
			    if (tempObject.get("name").equals(keys[1])) {
					return Integer.parseInt(tempObject.getJSONObject("tags").get("maxspeed").toString());
			    }
		    }
		}
		return 50;
	}
}
