package com.logicCorp.games.tetrisfx;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author hakim
 */
public class HiScores {

    public static final int WALL_OF_FAME_LENGTH=5;
    private Gamer[] gamers;
    
    Preferences userPreferences = Preferences.userRoot().node("/tetris/root");

    public HiScores() {
        userPreferences = Preferences.userRoot().node("/tetris/root");
//        try {
//            userPreferences.clear();
//        } catch (BackingStoreException ex) {
//            Logger.getLogger(HiScores.class.getName()).log(Level.SEVERE, null, ex);
//        }
        gamers=new Gamer[WALL_OF_FAME_LENGTH];
        for (int i=0; i<gamers.length;++i) {
            gamers[i]=new Gamer();
        }
    }
    
    public Gamer[] getTopGamers(){
        for (int i = 0; i < WALL_OF_FAME_LENGTH; i++) {
            gamers[i].setScore( userPreferences.getInt("GAMER_"+(i+1) +"_SCORE", 0));
            gamers[i].setName( userPreferences.get("GAMER_"+(i+1) +"_NAME", ""));
            gamers[i].setLevel( userPreferences.getInt("GAMER_"+(i+1) +"_LEVEL", 0));
        }
        
        Arrays.sort(gamers);
        return gamers;
    }
    
    public  int findGamerPosition(int score){
        Gamer[] gamers=getTopGamers();
        for (int i = 0; i < gamers.length; i++) {
            if (gamers[i].getScore()<=score) {
                return i;
            }
        }
        return -1;
    }
    
    
    public void saveTopGamers(){   
        Arrays.sort(gamers);
        for (int i = 1; i <= WALL_OF_FAME_LENGTH; i++) {
            userPreferences.putInt("GAMER_"+i+"_SCORE", gamers[i-1].getScore());
            userPreferences.put("GAMER_"+i+"_NAME", gamers[i-1].getName());
            userPreferences.putInt("GAMER_"+i+"_LEVEL", gamers[i-1].getLevel());
        }       
   }
    
    public int getHiScore(){       
        Arrays.sort(gamers);
        return gamers[WALL_OF_FAME_LENGTH-1].getScore();
    }
    
    public boolean isTopScore(int score){
        getTopGamers();
          for (int i = 0; i < gamers.length; i++) {
              if (score>=gamers[i].getScore()) {
                  return true;
              }
        }      
        return false;
    } 
    
    public void InsertGamer(Gamer gamer){
      getTopGamers();
          for (int i = 0; i < gamers.length; i++) {
              if (gamer.getScore()>=gamers[i].getScore()) {                  
                  for (int j = i+1; j <gamers.length; j++) {                      
                    gamers[j].setScore(gamers[j-1].getScore());
                    gamers[j].setName(gamers[j-1].getName());
                    gamers[j].setLevel(gamers[j-1].getLevel());
                  }
               gamers[i].setName(gamer.getName());
               gamers[i].setLevel(gamer.getLevel());
               gamers[i].setScore(gamer.getScore());   
               return;
              }              
        }        
    }

    public Gamer[] getGamers() {
        return gamers;
    }

    public void setGamers(Gamer[] gamers) {
        this.gamers = gamers;
    }
    
}
