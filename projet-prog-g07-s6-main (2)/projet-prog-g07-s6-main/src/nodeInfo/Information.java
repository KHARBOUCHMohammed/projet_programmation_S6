package nodeInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public abstract class Information {
	
	public static String splitString(String Tag, String TagName) {
		String valueOfTag = Tag;
		valueOfTag = valueOfTag.substring(valueOfTag.indexOf(TagName,0));
		valueOfTag = valueOfTag.substring(valueOfTag.indexOf("\"",0)+1);
		valueOfTag = valueOfTag.substring(0,valueOfTag.indexOf("\"",0));
		return valueOfTag;
	}
}
