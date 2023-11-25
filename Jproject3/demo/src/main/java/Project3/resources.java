package Project3;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.sound.sampled.*; 
public class resources {
    
}

interface MyConstants
{
    static final String RESOURCEPATH        = "/";

    //----- image files
    static final String FILE_SHIP     = RESOURCEPATH + "Ship_1.png";
    static final String FILE_BULLET     = RESOURCEPATH + "whitebullet.png";
    static final String FILE_BG     = RESOURCEPATH + "touhou15.gif";
    static final String FILE_ALIEN1     = RESOURCEPATH + "tf.png";

    //----- audio files
    static final String FILE_SHOOT     = RESOURCEPATH + "plst00.wav";

    
    //----- Sizes and locations
    static final int FRAMEWIDTH  = 1000;
    static final int FRAMEHEIGHT = 600;
    static final int GROUND_Y    = 350;
    static final int SKY_Y       = 50;
}


//for handling audio
