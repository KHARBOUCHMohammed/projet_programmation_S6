package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import classes.DefaultSpeedJSONParser;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

// @Authors Mohammed Kharbouch, Reda Merbah, Dorian Vabre

public class Main extends Application {


	/** The main controller of the application */
	private static MainController mc;
    private static HomeController homeController;
	
    @Override
    public void start(Stage primaryStage) {
        try {
        	// Using the Sample.fxml file for the display
        	BorderPane HomePane = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(HomePane,1200,800);

            primaryStage.setScene(scene);
            
            Image iconeApp = new Image("file:icon.png");
            primaryStage.getIcons().add(iconeApp);
            primaryStage.setTitle("OSM 117");
            
            //primaryStage.setMaximized(true);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void main(String[] args) {
    	
    	// Redirecting the errors in a file
		File file = new File("err.txt");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			PrintStream ps = new PrintStream(fos);
			System.setErr(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

    	mc = new MainController();
    	
    	// Launching the app
        launch(args);
    }
 }
