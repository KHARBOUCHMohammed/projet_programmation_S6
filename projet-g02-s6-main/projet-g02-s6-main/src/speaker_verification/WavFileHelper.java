package speaker_verification;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
// Copy from https://stackoverflow.com/questions/10397272/wav-file-convert-to-byte-array-in-java
/**
 * This class reads a .wav file and converts it to a bunch of byte arrays.
 *
 * The info represented by these byte arrays is then printed out.
 *
 * An example of playing these byte arrays with the speakers is used.
 *
 * It also converts the byte arrays to a .wav file.
 *
 * An extension of this concept can record from a microphone.
 * In this case, some values like sampling rate would need to be assumed.
 *
 * See https://ccrma.stanford.edu/courses/422/projects/WaveFormat/ for .wav file spec
 *
 * @author sizu
 */
public class WavFileHelper {

    public static void main(String[] args) throws InterruptedException {
        final String NEWLINE = "\n";
        int recordingSampleRate = 44100;
        short recordingBitsPerSample = 16;
        short recordingNumChannels = 2;

        String recordedFile = System.getProperty("user.dir")+"/src/speaker_verification/data/test/"+"capture2.wav";

        System.out.println("START");
        try {
            WavData wavInputData = new WavData();
            WavData wavRecordData = new WavData();
            wavRecordData.put(WaveSection.SAMPLE_RATE, recordingSampleRate);
            wavRecordData.put(WaveSection.BITS_PER_SAMPLE, recordingBitsPerSample);
            wavRecordData.put(WaveSection.NUM_CHANNELS, recordingNumChannels);

            System.out.println(NEWLINE+"START RECORDING - You can connect the microphone to the speakers");
            WavAudioRecorder recorder = new WavFileHelper.WavAudioRecorder(wavRecordData);
            recorder.startRecording();

            Thread.sleep(3000);
            System.out.println(NEWLINE+"STOP RECORDING FOR RECORDING");
            recorder.stopRecording();

            System.out.println(NEWLINE+"DISPLAY BYTE ARRAY INFORMATION");
            wavRecordData.printByteInfo();

            System.out.println(NEWLINE+"SAVE RECORDING IN WAV FILE");
            wavRecordData.write(recordedFile);

            System.out.println(NEWLINE+"PLAY BYTE ARRAY (THIS WILL BE RECORDED)");
            wavInputData.read(recordedFile);
            WavAudioPlayer player = new WavFileHelper.WavAudioPlayer(wavInputData);
            player.playAudio();

            System.out.println(VoiceUser.checkAudioFile("capture2.wav"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("FINISH");

    }

    public static enum WaveSection {
        // 12 Bytes
        CHUNK_ID(4, ByteOrder.BIG_ENDIAN),
        CHUNK_SIZE(4, ByteOrder.LITTLE_ENDIAN),
        FORMAT(4, ByteOrder.BIG_ENDIAN),

        // 24 Bytes
        SUBCHUNK1_ID(4, ByteOrder.BIG_ENDIAN),
        SUBCHUNK1_SIZE(4, ByteOrder.LITTLE_ENDIAN),
        AUDIO_FORMAT(2, ByteOrder.LITTLE_ENDIAN),
        NUM_CHANNELS(2, ByteOrder.LITTLE_ENDIAN),
        SAMPLE_RATE(4, ByteOrder.LITTLE_ENDIAN),
        BYTE_RATE(4, ByteOrder.LITTLE_ENDIAN),
        BLOCK_ALIGN(2, ByteOrder.LITTLE_ENDIAN),
        BITS_PER_SAMPLE(2, ByteOrder.LITTLE_ENDIAN),

        // 8 Bytes
        SUBCHUNK2_ID(4, ByteOrder.BIG_ENDIAN),
        SUBCHUNK2_SIZE(4, ByteOrder.LITTLE_ENDIAN),
        DATA(0, ByteOrder.LITTLE_ENDIAN),
        ;

        private Integer numBytes;
        private ByteOrder endian;
        WaveSection(Integer numBytes, ByteOrder endian){
            this.numBytes = numBytes;
            this.endian = endian;
        }
    }

    public static class WavData extends LinkedHashMap<WaveSection, byte[]>{
        static int HEADER_SIZE = 44; // There are 44 bits before the data section
        static int DEFAULT_SUBCHUNK1_SIZE = 16;
        static short DEFAULT_AUDIO_FORMAT = 1;
        static short DEFAULT_BLOCK_ALIGN = 4;
        static String DEFAULT_CHUNK_ID = "RIFF";
        static String DEFAULT_FORMAT = "WAVE";
        static String DEFAULT_SUBCHUNK1_ID = "fmt ";
        static String DEFAULT_SUBCHUNK2_ID = "data";

        public WavData(){
            this.put(WaveSection.CHUNK_ID, DEFAULT_CHUNK_ID);
            this.put(WaveSection.FORMAT, DEFAULT_FORMAT);
            this.put(WaveSection.SUBCHUNK1_ID, DEFAULT_SUBCHUNK1_ID);
            this.put(WaveSection.SUBCHUNK1_SIZE, DEFAULT_SUBCHUNK1_SIZE);
            this.put(WaveSection.AUDIO_FORMAT, DEFAULT_AUDIO_FORMAT);
            this.put(WaveSection.BLOCK_ALIGN, DEFAULT_BLOCK_ALIGN);
            this.put(WaveSection.SUBCHUNK2_ID, DEFAULT_SUBCHUNK2_ID);

            this.put(WaveSection.CHUNK_SIZE, 0);
            this.put(WaveSection.SUBCHUNK2_SIZE, 0);
            this.put(WaveSection.BYTE_RATE, 0);
        }

        public void put(WaveSection waveSection, String value){
            byte[] bytes = value.getBytes();
            this.put(waveSection, bytes);
        }

        public void put(WaveSection waveSection, int value) {
            byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
            this.put(waveSection, bytes);
        }

        public void put(WaveSection waveSection, short value) {
            byte[] bytes = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
            this.put(waveSection, bytes);
        }

        public byte[] getBytes(WaveSection waveSection) {
            return this.get(waveSection);
        }

        public String getString(WaveSection waveSection) {
            byte[] bytes = this.get(waveSection);
            return new String(bytes);
        }

        public int getInt(WaveSection waveSection) {
            byte[] bytes = this.get(waveSection);
            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
        }

        public short getShort(WaveSection waveSection) {
            byte[] bytes = this.get(waveSection);
            return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
        }

        public void printByteInfo() {
            for (WaveSection waveSection : WaveSection.values()) {
                if (waveSection.numBytes == 4
                        && waveSection.endian == ByteOrder.BIG_ENDIAN) {
                    System.out.println("SECTION:" + waveSection + ":STRING:"
                            + this.getString(waveSection));
                } else if (waveSection.numBytes == 4
                        && waveSection.endian == ByteOrder.LITTLE_ENDIAN) {
                    System.out.println("SECTION:" + waveSection + ":INTEGER:"
                            + this.getInt(waveSection));
                } else if (waveSection.numBytes == 2
                        && waveSection.endian == ByteOrder.LITTLE_ENDIAN) {
                    System.out.println("SECTION:" + waveSection + ":SHORT:"
                            + this.getShort(waveSection));
                } else {
                    // Data Section
                }
            }
        }

        public void read(String inputPath) throws Exception {
            // Analyze redundant info
            int dataSize = (int) new File(inputPath).length() - HEADER_SIZE;
            WaveSection.DATA.numBytes  = dataSize; // Can't have two threads using this at the same time

            // Read from File
            FileInputStream test = new FileInputStream(inputPath);
            DataInputStream inFile = new DataInputStream(test);

            for (WaveSection waveSection : WaveSection.values()) {
                byte[] readBytes = new byte[waveSection.numBytes];
                for (int i = 0; i < waveSection.numBytes; i++) {
                    readBytes[i] = inFile.readByte();
                }
                this.put(waveSection, readBytes);
            }

            test.close();
            inFile.close();
        }

        public void write(String outputPath) throws Exception {
            // Analyze redundant info
            int dataSize = this.get(WaveSection.DATA).length;
            this.put(WaveSection.CHUNK_SIZE, dataSize+36);
            this.put(WaveSection.SUBCHUNK2_SIZE, dataSize);

            int byteRate = this.getInt(WaveSection.SAMPLE_RATE)*this.getShort(WaveSection.BLOCK_ALIGN);
            this.put(WaveSection.BYTE_RATE, byteRate);

            // Write to File
            File test = new File(outputPath);
            FileOutputStream outputFile =new FileOutputStream(test);
            DataOutputStream dataOutputStream = new DataOutputStream(outputFile);

            for (WaveSection waveSection : WaveSection.values()) {
                dataOutputStream.write(this.getBytes(waveSection));
            }


            dataOutputStream.close();
            outputFile.close();
            //dataOutputStream = new DataOutputStream(outputFile);
            //outputFile = new FileOutputStream(test);


            //dataOutputStream = new DataOutputStream(outputFile);
        }

        public AudioFormat createAudioFormat() {
            boolean audioSignedSamples = true; // Samples are signed
            boolean audioBigEndian = false;
            float sampleRate = (float) this.getInt(WaveSection.SAMPLE_RATE);
            int bitsPerSample = (int) this.getShort(WaveSection.BITS_PER_SAMPLE);
            int numChannels = (int) this.getShort(WaveSection.NUM_CHANNELS);
            return new AudioFormat(sampleRate, bitsPerSample,
                    numChannels, audioSignedSamples, audioBigEndian);
        }
    }

    public static class WavAudioPlayer {
        WavData waveData = new WavData();

        public WavAudioPlayer(WavData waveData){
            this.waveData = waveData;
        }

        public void playAudio() throws Exception {
            byte[] data = waveData.getBytes(WaveSection.DATA);
            System.out.println("PLAY AUDIO");
            // Create an audio input stream from byte array
            AudioFormat audioFormat = waveData.createAudioFormat();
            InputStream byteArrayInputStream = new ByteArrayInputStream(data);
            AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream,
                    audioFormat, data.length / audioFormat.getFrameSize());

            // Write audio input stream to speaker source data line
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class,
                    audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            // Loop through input stream to write to source data line
            byte[] tempBuffer = new byte[10000];
            int cnt;
            while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
                sourceDataLine.write(tempBuffer, 0, cnt);
            }

            // Cleanup
            sourceDataLine.drain();
            sourceDataLine.close();
            byteArrayInputStream.close();
        }
    }

    public static class WavAudioRecorder implements Runnable {
        WavData waveData = new WavData();
        boolean recording = true;
        Thread runningThread;
        ByteArrayOutputStream byteArrayOutputStream;

        public WavAudioRecorder(WavData waveData){
            this.waveData = waveData;
        }

        public void startRecording(){
            this.recording = true;
            this.runningThread = new Thread(this);
            runningThread.start();
        }

        public WavData stopRecording() throws Exception{
            this.recording = false;
            runningThread.stop();

            waveData.put(WaveSection.DATA, byteArrayOutputStream.toByteArray());

            return waveData;
        }

        public void run() {
            try {
                // Create an audio output stream for byte array
                byteArrayOutputStream  = new ByteArrayOutputStream();

                // Write audio input stream to speaker source data line
                AudioFormat audioFormat = waveData.createAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
                TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                // Loop through target data line to write to output stream
                int numBytesRead;
                byte[] data = new byte[targetDataLine.getBufferSize() / 5];
                while(recording) {
                    numBytesRead =  targetDataLine.read(data, 0, data.length);
                    byteArrayOutputStream.write(data, 0, numBytesRead);
                }

                // Cleanup
                targetDataLine.stop();
                targetDataLine.close();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}