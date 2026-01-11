package game;

import javax.sound.sampled.*;
import java.io.File;

public class SoundPlayer {

    public static void playSound(String path) {
        try {
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(new File(path));

            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            // Volume control
            FloatControl volume =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-10.0f); // lower volume

            clip.start();

        } catch (Exception e) {
            System.out.println("Sound error: " + path);
        }
    }
}
