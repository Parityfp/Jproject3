package Project3;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
public class source {
    
}

interface MyConstants
{
    static final String LOCAL_ABS           = "/Users/person/Jproject3/Jproject3/demo";
    static final String RESOURCEPATH        = LOCAL_ABS + "/src/main/java/Project3/resources/";
    
    static final String FILE_SCORES     = LOCAL_ABS + "/src/main/java/Project3/scores.txt";


    //----- image files
    static final String FILE_SHIP     = RESOURCEPATH + "Ship_1.png";
    static final String FILE_BULLET     = RESOURCEPATH + "whitebullet.png";
    static final String FILE_BG     = RESOURCEPATH + "touhou15.gif";
    static final String FILE_BG2     = RESOURCEPATH + "space.jpeg";
    static final String FILE_BG3     = RESOURCEPATH + "backGround1.gif";
    static final String FILE_ALIEN1     = RESOURCEPATH + "tako.gif";
    static final String FILE_ALIEN2     = RESOURCEPATH + "whitewoman.png";
    static final String FILE_SPECIAL     = RESOURCEPATH + "kuru2.gif";
    static final String FILE_ENEMYBULLET1     = RESOURCEPATH + "enemybullet.png";
    static final String FILE_ENEMYBULLET2     = RESOURCEPATH + "enemybullet2.png";
    static final String FILE_PLASMA     = RESOURCEPATH + "redplasma.png";
    static final String FILE_STARTBUTTON     = RESOURCEPATH + "newStartButton.png";
    static final String FILE_STARTBG     = RESOURCEPATH + "backGround5.gif";


    static final String FILE_SLIDE1     = RESOURCEPATH + "mspaint1.png";
    static final String FILE_SLIDE2     = RESOURCEPATH + "mspaint2.png";
    static final String FILE_SLIDE3     = RESOURCEPATH + "mspaint3.png";
    static final String FILE_SLIDE4     = RESOURCEPATH + "mspaint4.png";
    static final String FILE_SLIDE5     = RESOURCEPATH + "mspaint5.png";
    static final String FILE_SLIDE6     = RESOURCEPATH + "mspaint6.png";
    static final String FILE_SLIDE7     = RESOURCEPATH + "mspaint7.png";
    static final String FILE_SLIDE8     = RESOURCEPATH + "mspaint8.png";
    static final String FILE_SLIDE9     = RESOURCEPATH + "mspaint9.png";


//////////////////////////////////// ITEMS ////////////////////////////////////

    static final String FILE_STAR     = RESOURCEPATH + "star.png";
    static final String FILE_POINT     = RESOURCEPATH + "point.png";
    static final String FILE_POWER     = RESOURCEPATH + "power.png";

    //----- audio files
    static final String FILE_SHOOT     = RESOURCEPATH + "plst00.wav";
    static final String FILE_HIT     = RESOURCEPATH + "graze.wav";
    static final String FILE_BOMB     = RESOURCEPATH + "lazer01.wav";
    static final String FILE_READY     = RESOURCEPATH + "ok00.wav";
    static final String FILE_TITLE     = RESOURCEPATH + "titlescreen.wav";
    static final String FILE_BLING     = RESOURCEPATH + "kira00.wav";
    static final String FILE_OK     = RESOURCEPATH + "ok00.wav";
    static final String FILE_ITEM     = RESOURCEPATH + "item00.wav";
    static final String FILE_THEME     = RESOURCEPATH + "discolor.wav";
    static final String FILE_KURUKURU     = RESOURCEPATH + "kurukuru.wav";

    //----- IMPORTANT
    static final String NON_SUSPICIOUS_LINK     = RESOURCEPATH + "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    
    //----- Sizes and locations
    static final int FRAMEWIDTH  = 1000;
    static final int FRAMEHEIGHT = 600;
    static final int GROUND_Y    = 350;
    static final int SKY_Y       = 50;
}


//for handling audio
class MySoundEffect
{
    private Clip clip;
    private long PausePosition; 
    public synchronized void SFX(String soundFileName, boolean loop, float volume) {
    try {
        File soundFile = new File(soundFileName);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        setVolume(volume);
        clip.setMicrosecondPosition(0);

        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume(float volume) { // Volume is a value between 0 and 1
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            if (volume < 0.0f)  volume = 0.0f;
            if (volume > 1.0f)  volume = 1.0f;
            float dB = (float)(Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
    public synchronized void stopSound() {
        if (clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0); 
        }
    }
    public synchronized void pauseSound() {
        if (clip != null && clip.isRunning()) {
            PausePosition = clip.getMicrosecondPosition(); 
            clip.stop();
        }
    }
    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.setMicrosecondPosition(PausePosition);
            clip.start();
        }
    }
    //dk if this is utilized
    // public synchronized void setPositon(long position) {
    //     if (clip != null) {
    //         clip.setMicrosecondPosition(position); 
    //     }
    // }

}