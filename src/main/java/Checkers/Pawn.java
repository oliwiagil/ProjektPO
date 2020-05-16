package Checkers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pawn extends ImageView {

    private int arrX;
    private int arrY;
    private int color; //1 - white, 2 - black
    private boolean canBeMoved;
    private boolean hetman;
    private boolean canHit;

    public Pawn(int y, int x, Image img, int arrY, int arrX, int col){
        super(img);
        this.setY(y-(img.getHeight()/2));
        this.setX(x-(img.getWidth()/2));
        this.arrX = arrX;
        this.arrY = arrY;
        if(col != 1 && col != 2) System.out.println("Color nie dizala");
        this.color = col;
        this.canBeMoved = false;
        this.hetman = false;
        this.canHit = false;
    }

    //Getters
    public int getArrX(){
        return arrX;
    }

    public int getArrY(){
        return arrY;
    }

    public int getColor(){
        return color;
    }

    public boolean isCanBeMoved(){
        return canBeMoved;
    }

    public boolean isHetman(){ return hetman; }

    public boolean isCanHit(){ return canHit; }

    //Setters
    public void setArrX(int x){
        this.arrX = x;
    }

    public void setArrY(int y){
        this.arrY = y;
    }

    public void setColor(int col){
        this.color = col;
    }

    public void setCanBeMoved(boolean c){ this.canBeMoved = c; }

    public void setHetman(boolean h){ this.hetman = h; }

    public void setCanHit(boolean c){ this.canHit = c; }
}
