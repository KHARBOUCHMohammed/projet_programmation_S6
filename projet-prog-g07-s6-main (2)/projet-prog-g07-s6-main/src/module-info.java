module Main {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.gluonhq.maps;
	requires java.xml;
	requires java.desktop;
	
	opens com.projet.g07 to javafx.graphics, javafx.fxml;
}
