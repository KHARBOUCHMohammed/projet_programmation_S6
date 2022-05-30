package speaker_verification;


import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VoiceUser {

    public static Mixer mixer;
    public static Clip clip;

    public static void main(String[] args) {
        //recordAudio();
        playAudio("data/source/dorian.wav");
    }

    public static boolean checkAudioFile(String fileAudio) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder()
                .command(
                        "python",
                        "/Users/ml/IdeaProjects/projet-g02-s6-v1/src/speaker_verification/"+fileAudio,
                        "main");

        Process process = pb.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if(Boolean.parseBoolean(line)) {
                return true;
            }
        }

        return false;
    }


    public static void playAudio(String fileAudio){
        // from the tutorial: https://www.youtube.com/watch?v=nUKya2DvYSo
        Mixer.Info[] mixerInformations = AudioSystem.getMixerInfo();

        mixer = AudioSystem.getMixer(mixerInformations[0]);

        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);

        try {
            clip = (Clip) mixer.getLine(dataInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            URL soundURL = VoiceUser.class.getResource(fileAudio);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

        clip.start();

        do {
            try {
                Thread.sleep(50);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(clip.isActive());
    }

    public static void recordAudio() {
        // tutorial : https://www.youtube.com/watch?v=GVtl19L9GxU
        System.out.println("Starting Sound Recoding");

        try {

            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    44100,
                    16,
                    2,
                    4,
                    44100,
                    false
            );

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if(!AudioSystem.isLineSupported(info)) {
                System.err.println("Line not supported");
            }

            final TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(info);
            targetLine.open();

            System.out.println("Starting Recording...");
            targetLine.start();

            Thread thread = new Thread() {
                @Override public void run() {
                    AudioInputStream audioStream = new AudioInputStream(targetLine);
                    File audioFile = new File("src/speaker_verification/data/test/record2.wav");
                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                        System.out.println("Stopped Recording");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
            Thread.sleep(5000);
            targetLine.stop();
            targetLine.close();

            System.out.println("Ended Sound Test");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

