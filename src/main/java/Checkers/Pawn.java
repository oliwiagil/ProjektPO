package Checkers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends ImageView {

    private int arrX;
    private int arrY;
    private boolean color; //0 - white, 1 - black

    public Pawn(int y, int x, Image img, int arrY, int arrX, boolean col){
        super(img);
        this.setY(y-(img.getHeight()/2));
        this.setX(x-(img.getWidth()/2));
        this.arrX = arrX;
        this.arrY = arrY;
        this.color = col;
    }

    //Getters
    public int getArrX(){
        return arrX;
    }

    public int getArrY(){
        return arrY;
    }

    public boolean getColor(){
        return color;
    }

    //Setters
    public void setArrX(int x){
        this.arrX = x;
    }

    public void setArrY(int y){
        this.arrY = y;
    }

    public void setColor(boolean col){
        this.color = col;
    }

}
