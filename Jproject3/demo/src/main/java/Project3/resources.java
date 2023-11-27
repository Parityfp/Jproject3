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
    static final String FILE_BG2     = RESOURCEPATH + "space.jpeg";
    static final String FILE_BG3     = RESOURCEPATH + "backGround1.gif";
    static final String FILE_ALIEN1     = RESOURCEPATH + "tf.png";
    static final String FILE_ALIEN2     = RESOURCEPATH + "whitewoman.png";
    static final String FILE_BOSS     = RESOURCEPATH + "kururing.gif";
    static final String FILE_ENEMYBULLET1     = RESOURCEPATH + "enemybullet.png";
    static final String FILE_ENEMYBULLET2     = RESOURCEPATH + "enemybullet2.png";
    static final String FILE_PLASMA     = RESOURCEPATH + "redplasma.png";


//////////////////////////////////// ITEMS ////////////////////////////////////

    static final String FILE_STAR     = RESOURCEPATH + "star.png";
    static final String FILE_POINT     = RESOURCEPATH + "point.png";
    static final String FILE_POWER     = RESOURCEPATH + "power.png";

    //----- audio files
    static final String FILE_SHOOT     = RESOURCEPATH + "plst00.wav";
    static final String FILE_HIT     = RESOURCEPATH + "graze.wav";
    static final String FILE_BOMB     = RESOURCEPATH + "lazer01.wav";
    static final String FILE_READY     = RESOURCEPATH + "ok00.wav";

    //----- IMPORTANT
    static final String NON_SUSPICIOUS_LINK     = RESOURCEPATH + "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
    
    //----- Sizes and locations
    static final int FRAMEWIDTH  = 1000;
    static final int FRAMEHEIGHT = 600;
    static final int GROUND_Y    = 350;
    static final int SKY_Y       = 50;
}


//for handling audio
