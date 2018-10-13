package sample;
/* John Barnett
 */
//Sounds were posted on this forum for free under creative commons license https://opengameart.org/content/512-sound-effects-8-bit-style
//'https://www.freepik.com/free-vector/jungle-vegetation-background_765995.htm' Designed by Freepik is the background creative commons
//the music is under creative commons licence http://freemusicarchive.org/music/Visager/Songs_From_An_Unmade_World_2/

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;


import java.util.*;

public class Controller {
    //fxml nodes that need to be changed in controller
    public Canvas canvas;
    public Pane gameWindow;
    public Pane startMenu;
    public Pane gameOver;
    public Slider gameMusicSlider;
    public Slider gameSoundsSlider;
    public HBox hBoxScore;
    public TextField scoreNameEntry;
    public Label scoreOnEntry;
    public TextArea scoreTextField;

    //numbers need to create a grid and blocks
    private int blockSize = 20;
    private static final int appSize = 1000;
    private int appScale = appSize /blockSize;
    private Random random = new Random();

    //instantiating game objects
    private Snake snake;
    private Food food = new Food(new Point2D(random.nextInt(appScale),random.nextInt(appScale)));
    private GameSounds gameSounds = new GameSounds();

    //score fields
    private String scoreBoardString;
    private String scoreDisplay;
    private int score = 0;
    private int pointValue = 10;
    //gameloop timeline
    private Timeline timeline = new Timeline();
    //contains the scores
    private ArrayList<Integer> scoreList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();



    //initializes the objects contained in it
    public void initialize(){

        snake = new Snake(new Point2D(10, 10));
        // graphics context allows me to draw to the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect((food.getPosition().getX()*blockSize),(food.getPosition().getY()*blockSize),blockSize,blockSize);

        // key controls with a switch sets directions of snake
        gameWindow.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case LEFT:
                    if (snake.getDirection() != Direction.RIGHT)
                        snake.setDirection(Direction.LEFT);
                    break;

                case RIGHT:
                    if (snake.getDirection() != Direction.LEFT)
                        snake.setDirection(Direction.RIGHT);
                    break;
                case DOWN:
                    if (snake.getDirection() != Direction.UP)
                        snake.setDirection(Direction.DOWN);
                    break;
                case UP:
                    if (snake.getDirection() != Direction.DOWN)
                        snake.setDirection(Direction.UP);
                    break;
                case A:
                    if (snake.getDirection() != Direction.RIGHT)
                        snake.setDirection(Direction.LEFT);
                    break;
                case D:
                    if (snake.getDirection() != Direction.LEFT)
                        snake.setDirection(Direction.RIGHT);
                    break;
                case S:
                    if (snake.getDirection() != Direction.UP)
                        snake.setDirection(Direction.DOWN);
                    break;
                case W:
                    if (snake.getDirection() != Direction.DOWN)
                        snake.setDirection(Direction.UP);
                    break;

            }

        });

        //code in the keyframe gets run every .2 seconds
        KeyFrame gameLoop = new KeyFrame(Duration.seconds(.2),frame ->{



            //renders the each segment of the snake
            gc.setFill(Color.GREEN);
            snake.getBody().forEach(f->{
                gc.fillRect(f.getX()*blockSize,f.getY()*blockSize,blockSize,blockSize);
                gc.clearRect(snake.getTail().getX()*blockSize,snake.getTail().getY()*blockSize,blockSize,blockSize);

            });

            //gameOver logic if run into a wall you die
            if (snake.hasHitWall(appScale)){
                gc.clearRect(0,0,appSize,appSize);
                gc.setFill(Color.RED);
                gc.fillRect(food.getPosition().getX()*blockSize,food.getPosition().getY()*blockSize,blockSize,blockSize);
                timeline.pause();
                gameSounds.playDeathSound();
                youDied();
            }
            //game logic that if you run into your self you die
            if (snake.getBodySize()>3){
                for(int i = 2; i < snake.getBodySize(); i++){
                    if(snake.getBody().get(i).equals(snake.getHead())){
                        gc.clearRect(0,0,1000,1000);
                        gc.setFill(Color.RED);
                        gc.fillRect(food.getPosition().getX()*blockSize,food.getPosition().getY()*blockSize,blockSize,blockSize);
                        timeline.pause();
                        gameSounds.playDeathSound();
                        youDied();
                    }
                }
            }


            // food logic upon eating food the snakes grows
            if(snake.hasEaten(food)){
                snake.grow();
                food.setPosition(getRandPosition());

                //score handles the score seen on screen
                gc.clearRect(10,10,30,20);
                score += pointValue;
                scoreDisplay =  String.valueOf(score);
                //draws score on canvas
                gc.setFont(new Font(16));
                gc.setFill(Color.WHITE);
                gc.fillText(scoreDisplay,20,20);

                // draws food it is important that food gets drawn second so none of it gets erased.
                gc.setFill(Color.RED);
                gc.fillRect(food.getPosition().getX()*blockSize,food.getPosition().getY()*blockSize,blockSize,blockSize);
                gameSounds.playEatingSound();

            }

            // updates the snake position
            snake.update();
        });

        //sense a change in the slide then sets the volume in the game sounds object
        gameMusicSlider.valueProperty().addListener(e-> gameSounds.setGameMusicVol(gameMusicSlider.getValue()));
        gameSoundsSlider.valueProperty().addListener(e-> gameSounds.setGameSoundsVol(gameSoundsSlider.getValue()));

        //adding keyframe to timeline settings to make the game loop infinite
        timeline.getKeyFrames().add(gameLoop);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.pause();

    }


    public void easy(){
        timeline.setRate(1);
        pointValue =10;

    }
    public void medium(){
        timeline.setRate(1.5);
        pointValue = 15;

    }
    public void hard(){
        timeline.setRate(1.7);
        pointValue = 20;

    }
    public void start(){
        startMenu.setOpacity(0);
        timeline.play();
        gameSounds.playGameMusic();

    }
    public void goToStartMenu(){
        startMenu.setOpacity(1);

        if(gameOver.getOpacity()!=0) {
            gameOver.setOpacity(0);
            scoreTextField.setOpacity(0);
        }
        if(!snake.getHead().equals(new Point2D(10,10))){
            snake.reset(new Point2D(10,10));
            score = 0;
            pointValue =10;

        }


    }
    private void youDied(){
        gameOver.setOpacity(1.0);
        scoreTextField.setOpacity(1.0);
        timeline.stop();
        gameSounds.stopGameMusic();
        scoreOnEntry.textProperty().setValue(String.valueOf(score));
        scoreBoardString ="";
        scoreList.forEach(s-> {
            nameList.forEach(n -> {
                if (s != null && n != null)
                    scoreBoardString += (s.toString() + "  " + n + "\n");

            });
        });
        scoreTextField.setText(scoreBoardString);
    }

    public void exit(){
        Platform.exit();

    }

    private Point2D getRandPosition(){
        return  new Point2D(random.nextInt(appScale), random.nextInt(appScale));
    }



    public void saveScore() {

        if(scoreNameEntry.getText()!= null||scoreList.get(scoreList.size())!= score) {
            //scoreBoard.put(score,scoreNameEntry.getText());
            scoreList.add(score);
            nameList.add(scoreNameEntry.getText());
        }
    }


}
