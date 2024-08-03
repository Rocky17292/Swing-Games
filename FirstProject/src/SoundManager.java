import javax.sound.sampled.*;
import java.io.*;

public class SoundManager {
    private Clip clip;

    public SoundManager(String filePath) {
        try {
            // Load sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip.isRunning()) {
            clip.stop(); // Stop the sound if it is already playing
        }
        clip.setFramePosition(0); // Rewind to the beginning
        clip.start(); // Play the sound
    }
}
