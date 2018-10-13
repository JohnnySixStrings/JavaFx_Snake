package sample;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameSounds {
    private final static Media music = new Media(sample.Controller.class.getResource("game.mp3").toString());
    private MediaPlayer gameMusic = new MediaPlayer(music);
    private AudioClip deathSound = new AudioClip(sample.Controller.class.getResource("SnakeDeath.wav").toString());
    private AudioClip eatingSound = new AudioClip(sample.Controller.class.getResource("food.wav").toString());




    public void playGameMusic(){
        gameMusic.play();
    }
    public void stopGameMusic(){
        gameMusic.stop();
    }
    public void playDeathSound(){
        deathSound.play();
    }
    public void playEatingSound(){
        eatingSound.play();
    }
    public void setGameSoundsVol(double volLevel){
        deathSound.setVolume(volLevel);
        eatingSound.setVolume(volLevel);
    }
    public void setGameMusicVol(double volLevel){
        gameMusic.setVolume(volLevel);

    }
}
