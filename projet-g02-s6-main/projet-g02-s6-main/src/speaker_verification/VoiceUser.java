package speaker_verification;


import javax.sound.sampled.*;
import java.io.*;


public class VoiceUser {

    public static Mixer mixer;
    public static Clip clip;
    public static String nameUser = null;
    public static int durationMs = 5000;

    public static boolean checkAudioFile(String fileAudio) throws IOException, InterruptedException {
        Process process = Runtime
                .getRuntime()
                .exec(
                        "python " +
                                System.getProperty("user.dir")+"/src/speaker_verification/ECAPA-TDNN_model.py " + fileAudio + " 0.40"
                );


        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line_lr;
        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        while((line_lr = br.readLine()) != null) {
            System.out.println(line_lr);
        }

        String line;
        while ((line = reader.readLine()) != null) {

            if(Boolean.parseBoolean(line)) {
                return true;
            }
            nameUser = line;
        }

        return false;
    }

    public static void recordAudio(String nameFile) {
        // tutorial : https://www.youtube.com/watch?v=GVtl19L9GxU
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

            File audioFile = new File(nameFile);


            Thread thread = new Thread() {
                @Override public void run() {
                    AudioInputStream audioStream = new AudioInputStream(targetLine);

                    try {
                        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);

                        System.out.println("Stopped Recording");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };


            Thread stopper = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(durationMs);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    targetLine.stop();
                    targetLine.close();
                }
            });

            stopper.start();
            thread.start();

            System.out.println("Ended Sound Test");

        } catch (Exception e) {
            System.out.println("ERROR ? ");
            e.printStackTrace();
        }
    }
}


