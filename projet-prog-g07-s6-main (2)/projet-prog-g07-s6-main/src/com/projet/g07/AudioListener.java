package com.projet.g07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AudioListener{

public static void main(String[] args) throws IOException  {

    String commandePourExecuter = "cmd.exe /c start python3 C:\\Users\\burge\\PycharmProjects\\pythonProject\\main.py ";
    String commandePourAfficher = "cmd.exe /c python3 C:\\Users\\burge\\PycharmProjects\\pythonProject\\main.py ";
    Process prog1 = Runtime.getRuntime().exec(commandePourExecuter);
    Process p = Runtime.getRuntime().exec(commandePourAfficher);
    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line;

       while((line = reader.readLine()) != null)
    	   if(!line.equals("<class 'speech_recognition.AudioData'>"))
            System.out.println("from python: "+ line);
    System.out.println("ok");
	}
}
