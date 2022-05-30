package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import speaker_verification.VoiceUser;

import java.io.File;
import java.util.Objects;


public class signInController {

    public Label errorId;
    @FXML
    TextField pseudo;

    @FXML
    Label signInValidation;

    @FXML
    ImageView micFlash;

    private boolean hasToFlash = false;

    public boolean pseudoIsAvailable() {
        File dir = new File(System.getProperty("user.dir")+"/src/speaker_verification/data/source/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                String[] parts = child.getName().split("_");
                if(parts[0].equals(pseudo.getText())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onClickRecord() {
        if(!pseudo.getText().equals("") && pseudoIsAvailable()) {
            this.hasToFlash = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(VoiceUser.durationMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hasToFlash = false;
                }
            }).start();
            new Thread(new Runnable() {
                @Override public void run() {
                    flashesButton();
                }
            }).start();
            VoiceUser.recordAudio(System.getProperty("user.dir")+"/src/speaker_verification/data/source/"+pseudo.getText()+"_source.wav");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(VoiceUser.durationMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    signInValidation.setVisible(true);
                }
            }).start();

        }
        else{
            errorId.setStyle("-fx-text-fill: red ;");
            errorId.setText("Vous devez saisir un pseudo pour l'inscription ou le pseudo saisit n'était pas valide car " +
                    "déjà existant.");
        }
    }

    public void flashesButton() {
        Image mic = new Image("Icons/mic.png");
        Image micColored = new Image("Icons/micColored.png");
        while(this.hasToFlash) {
            micFlash.setImage(mic);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            micFlash.setImage(micColored);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getBackToMap(ActionEvent event) {
        try {
            BorderPane mainPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Sample.fxml")));
            Scene scene = new Scene(mainPane, 1200, 800);
            Stage secondaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            mainPane.requestFocus();

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());

            Canvas canvas = (Canvas) scene.lookup("#canvas");
            HomeController.mc.setCanvas(canvas);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            HomeController.mc.setGraphicsContext(gc);

            secondaryStage.setScene(scene);

            HomeController.mc.setStage(secondaryStage);

            Image iconeApp = new Image("file:icon.png");
            secondaryStage.getIcons().add(iconeApp);

            secondaryStage.setTitle("OSM 117");
            secondaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
