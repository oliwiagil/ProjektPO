package Checkers;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {

    private boolean canBePositioned;

    public Field(double x, double y, double sizeWidth, double sizeHeight, Paint fill){
        super(x, y, sizeWidth, sizeHeight);
        this.setFill(fill);
        this.canBePositioned = false;
    }

    //Setters
    public void setCanBePositioned(boolean c){
        this.canBePositioned = c;
    }


    //Getters
    public boolean isCanBePositioned(){
        return this.canBePositioned;
    }
}
