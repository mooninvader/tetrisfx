package com.logicCorp.games.tetrisfx;

/**
 *
 * @author hakim
 */
public class Gamer implements Comparable{
    String name;
    int score;
    int level;

    
    @Override
    public int compareTo(Object o) {
        if (o instanceof Gamer ) {
            return ((Gamer)o).score-this.score;
        }
        else return -1;
    }

    public Gamer() {
    }
   
    
    
    public Gamer(String name, int score,int level) {
        this.name = name;
        this.score = score;
        this.level=level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    
    
    
    
}
