package sample;

import javafx.geometry.Point2D;

public class Food {
    private Point2D position;

    Food(Point2D point2D){
        this.position = point2D;

    }
    public Point2D getPosition(){
        return position;
    }
    public void setPosition(Point2D pos){
        this.position = pos;
    }
}
