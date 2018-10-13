package sample;

import javafx.geometry.Point2D;


import java.util.ArrayList;
import java.util.List;

public class Snake {

    private Direction direction= Direction.RIGHT;
    private Point2D head;
    private Point2D tail;
    private List<Point2D> body = new ArrayList<>();



    public Snake(Point2D start){
        this.head = start;
        this.tail = start;
        body.add(head);
    }
    public void setDirection( Direction d){
        this.direction = d;
    }
    public void update(){
        head = head.add(direction.vector);
        tail = body.remove(body.size()-1);
        body.add(0,head);
    }

    public List<Point2D> getBody(){
        return  this.body;
    }
    public boolean hasHitWall(int size){

        return head.getX() < 0 ||  head.getY() <0 || head.getX()>= size || head.getY() >= size;

    }
    public boolean hasEaten(Food apple){
       return head.equals(apple.getPosition());

    }

    public void grow(){
        body.add(tail);
    }

    public Point2D getTail(){
        return tail;
    }
    public Point2D getHead(){
        return head;
    }
    public void reset(Point2D resetxy){
        body.clear();
        this.head = resetxy;
        this.tail = resetxy;
        body.add(head);
        direction = Direction.RIGHT;

    }
    public Direction getDirection(){
        return this.direction;
    }
    public int getBodySize(){
        return body.size();

    }


}
