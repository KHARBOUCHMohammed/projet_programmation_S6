package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import speaker_verification.VoiceUser;
import speaker_verification.WavFileHelper;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class HomeController {

	public static final MainController mc = new MainController();

	@FXML
	public Button stopRecordBtn;
	public Label labelAudioId;
	private boolean hasToFlash = false;

	@FXML
	Button replayBtn;

	@FXML
	Button recordBtn;

	@FXML
	ImageView micIcon;



	public void UnlockApp(ActionEvent event) throws IOException, InterruptedException {

		if(VoiceUser.checkAudioFile( "sample.wav")) {
			mc.setUserName(VoiceUser.nameUser);

			try {
				BorderPane mainPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Sample.fxml")));
				Scene scene = new Scene(mainPane, 1200, 800);
				Stage secondaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

				mainPane.requestFocus();

				scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("application.css")).toExternalForm());

				Canvas canvas = (Canvas) scene.lookup("#canvas");
				mc.setCanvas(canvas);

				GraphicsContext gc = canvas.getGraphicsContext2D();
				mc.setGraphicsContext(gc);

				secondaryStage.setScene(scene);

				mc.setStage(secondaryStage);

				Image iconeApp = new Image("file:icon.png");
				secondaryStage.getIcons().add(iconeApp);

				secondaryStage.setTitle("OSM 117");
				secondaryStage.show();

				File f = new File("src/speaker_verification/data/test/sample.wav");
				f.delete();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		else {
			labelAudioId.setStyle("-fx-text-fill: red ;");
			labelAudioId.setText("L'Authentification à echouée. Veuillez re-enregistrer votre voix.");
		}
	}

	public void onClickRecordBtn(ActionEvent event) throws Exception {
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
		VoiceUser.recordAudio( System.getProperty("user.dir")+"/src/speaker_verification/data/test/"+"sample.wav");
		System.out.println("IN RECORD BTN");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(VoiceUser.durationMs);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void onClickReplayBtn(ActionEvent event) throws Exception {
		System.out.println("IN REPLAY BUTTON");
		String recordedFile = System.getProperty("user.dir")+"/src/speaker_verification/data/test/"+"sample.wav";
		WavFileHelper.WavData wavInputData = new WavFileHelper.WavData();
		wavInputData.read(recordedFile);
		WavFileHelper.WavAudioPlayer player = new WavFileHelper.WavAudioPlayer(wavInputData);
		player.playAudio();
	}

	public void flashesButton() {
		Image mic = new Image("Icons/microphone.png");
		Image micColored = new Image("Icons/microphone_orange.png");
		while(this.hasToFlash) {
			micIcon.setImage(mic);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			micIcon.setImage(micColored);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		micIcon.setImage(mic);
	}
}


