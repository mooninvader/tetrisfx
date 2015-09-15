package com.logicCorp.games.tetrisfx;

import javafx.scene.media.AudioClip;


/**
 *
 * @author hakim
 */
public class SoundPlayer implements AutoCloseable{


    AudioClip au;


    public SoundPlayer(String file) {
       au=new AudioClip(SoundPlayer.class.getResource(file).toExternalForm());
       au.setCycleCount(AudioClip.INDEFINITE);
    }

    public void play() {       
      au.play();       
    }

   

    public void pause() {
     
    }


    public void close()  {
     au.stop();
    }

    public void resume(){
      au.play();
    }
    
    public void suspend(){
     au.stop();
    }
}