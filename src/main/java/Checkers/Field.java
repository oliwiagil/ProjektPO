package Checkers;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {

    Field(double x, double y, double sizeWidth, double sizeHeight){
        super(x, y, sizeWidth, sizeHeight);
    }

    Field(double x, double y, double sizeWidth, double sizeHeight, Paint fill){
        super(x, y, sizeWidth, sizeHeight);
        this.setFill(fill);
    }
}
