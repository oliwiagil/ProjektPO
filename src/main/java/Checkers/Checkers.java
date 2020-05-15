package Checkers;
;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Checkers {


    //Settings of game
    public double WIDTH;
    public double HEIGHT;
    public boolean fullScreen;

    public Stage gameStage;
    protected int SPEED;
    protected Pane gamePane;
    protected Timeline timeline;
    protected CheckersMenu returnMenu;

    Scene scene;
    double pawnOrgX, pawnOrgY;
    private double translateY;
    private double translateX;
    private final int boardSize = 8;
    private int whiteAtBottom = 1;
    private final int frameExtent = 640;
    private final int squareSize = 80;
    private final int frameWidth = 10;
    private final int pawnRadius = 25;
    private int[][] array;

    private Rectangle[][] fields;
    private ArrayList<Pawn> blackPawns;
    private ArrayList<Pawn> whitePawns;
    private Rectangle frame;
    private Image blackPawn = new Image("file:media/Checkers/blackPawn.png");
    private Image whitePawn = new Image("file:media/Checkers/whitePawn.png");

    //konstruktor, tutaj tworzymy grę: jej stage(każdy obiekt gry ma swój), scene, i jakieś zmienne potrzebne
    Checkers(CheckersMenu cM){
        returnMenu = cM;
        gameStage = new Stage();

        array = new int[8][8];
        fields = new Rectangle[8][8];
        blackPawns = new ArrayList<>();
        whitePawns = new ArrayList<>();
        gamePane = performBoard();
        setPieces(array, whiteAtBottom);
        scene = new Scene(gamePane);
        gameStage.setTitle("Checkers");
        gameStage.setScene(scene);
    }

    //odpalamy grę, pokazujemy stage i włączamy czas

    public void startGame() throws Exception {
        gameStage.show();
    }

    //tutaj resetujemy grę tak aby można było grać od nowa bez tworzenia gry na nowo

    public void resetGame() {

    }

    //gdy przegramy wywołujemy tą metodę
    //w przypadku warcabów można ustalić, że będzie wywoływana zawsze jedna metoda
    // albo gameOver albo gameWin i w niej będzie pokazany wynik rozgrywki

    public void gameOver() {

    }

    //wywołujemy gdy wygramy grę

    public void gameWin() {

    }

    //wywołujemy gdy chcemy zapauzować grę

    public void gamePause() {

    }

    public Pane performBoard(){
        Pane board = new Pane();
        frame = new Rectangle(0, 0, frameExtent, frameExtent); //tam gdzie ustawimy frame tam będzie cała plansza, pionki i pola są ustawiane względem frame
        board.getChildren().addAll(frame);

        boolean blackOrWhite = true;
        int i = 0, j = 0;
        while (i < boardSize) {
            while (j < boardSize) {
                if (blackOrWhite)
                    fields[i][j] = new Field(i*squareSize+frame.getX(), j*squareSize+frame.getY(), squareSize, squareSize, Color.AZURE);
                else
                    fields[i][j] = new Field(i*squareSize+frame.getX(), j*squareSize+frame.getY(), squareSize, squareSize, Color.GREEN);
                blackOrWhite = !blackOrWhite;
                board.getChildren().add(fields[i][j]);
                j++;
            }
            blackOrWhite = !blackOrWhite;
            j = 0;
            i++;
        }

        i = 0;
        j = 0;
        Color tempColor = Color.BLACK;
        int height = (int) (frame.getX() + 1.5*squareSize);
        int width = (int)(frame.getY() + 0.5*squareSize);
        while (j < 2) {
            while (i < boardSize) {
                blackPawns.add(setPawn(blackPawn, height, width, j, i, true));
                height += 2*squareSize;
                i += 2;
            }
            height = (int)(frame.getX() + 0.5*squareSize);
            width += squareSize;
            i = 1;
            j++;
        }

        i = 0;
        j = 6;
        tempColor = Color.WHITE;
        height = (int)(frame.getX() + 1.5*squareSize);
        width = (int)(frame.getY() + 6.5*squareSize);
        while (j < boardSize) {
            while (i < boardSize) {
                whitePawns.add(setPawn(whitePawn, height, width, j, i, false));
                height += 2*squareSize;
                i += 2;
            }
            height = (int)(frame.getX() + 0.5*squareSize);
            width += squareSize;
            i = 1;
            j++;
        }

        for(Pawn a: blackPawns){
            board.getChildren().add(a);
        }

        for(Pawn a: whitePawns){
            board.getChildren().add(a);
        }

        return board;
    }


    Pawn setPawn(Image img, int width, int height, int arrX, int arrY, boolean col){
        Pawn tempPawn = new Pawn(height, width, img, arrY, arrX, col);
        tempPawn.setCursor(Cursor.HAND);
        tempPawn.setOnMousePressed(pawnGetPosition);
        tempPawn.setOnMouseDragged(pawnMovement);
        tempPawn.setOnMouseReleased(pawnSetPosition);
        return tempPawn;
    }

    void setPieces(int[][] array, int WhiteAtBottom){
        array[0][1] = 2; array[0][3] = 2; array[0][5] =2; array[0][7] = 2;
        array[1][0] = 2; array[1][2] = 2; array[1][4] =2; array[1][6] = 2;
        array[6][1] = 1; array[6][3] = 1; array[6][5] = 2; array[6][7] = 1;
        array[7][0] = 1; array[7][3] = 1; array[7][5] = 1; array[7][7] = 1;
    }

    EventHandler<MouseEvent> pawnGetPosition = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            pawnOrgX = mouseEvent.getSceneX();
            pawnOrgY = mouseEvent.getSceneY();
            translateX = ((ImageView)(mouseEvent.getSource())).getTranslateX();
            translateY = ((ImageView)(mouseEvent.getSource())).getTranslateY();
            //System.out.println(translateX + " " + translateY);
        }
    };

    EventHandler<MouseEvent> pawnMovement = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double yDiff = mouseEvent.getSceneY()-pawnOrgY;
            double xDiff = mouseEvent.getSceneX()-pawnOrgX;
            double xToBe = xDiff + translateX;
            double yToBe = yDiff + translateY;
            ((ImageView)(mouseEvent.getSource())).setTranslateX(xToBe);
            ((ImageView)(mouseEvent.getSource())).setTranslateY(yToBe);
        }
    };

    EventHandler<MouseEvent> pawnSetPosition = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            //double x = mouseEvent.getSceneX();
            //double y = mouseEvent.getSceneY();
            //System.out.println(x + " " + y);
            //System.out.println(ix + " " + iy);
            double centerX = ((Pawn)(mouseEvent.getSource())).getX() + ((Pawn)(mouseEvent.getSource())).getImage().getWidth()/2
                    + ((Pawn)(mouseEvent.getSource())).getTranslateX();
            double centerY = ((Pawn)(mouseEvent.getSource())).getY() + ((Pawn)(mouseEvent.getSource())).getImage().getHeight()/2
                    + ((Pawn)(mouseEvent.getSource())).getTranslateY();
            int ix = (int)(centerX/squareSize);
            int iy = (int)(centerY/squareSize);
            //System.out.println( centerX - fields[ix][iy].getX());
            ((Pawn)(mouseEvent.getSource())).setTranslateX(fields[ix][iy].getWidth()/2 + fields[ix][iy].getX() - ((Pawn)(mouseEvent.getSource())).getX()
                    - ((Pawn)(mouseEvent.getSource())).getImage().getWidth()/2);
            ((Pawn)(mouseEvent.getSource())).setTranslateY(fields[ix][iy].getHeight()/2 + fields[ix][iy].getY() - ((Pawn)(mouseEvent.getSource())).getY()
                    - ((Pawn)(mouseEvent.getSource())).getImage().getHeight()/2);
        }
    };



   /* EventHandler<MouseEvent> idkIsAllowed = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            double xPresent = mouseEvent.getSceneX();
            double yPresent = mouseEvent.getSceneY();
            int columns = (int) (xPresent-frameWidth)/squareSize;
            int verse = (int) (yPresent-frameWidth)/squareSize;
            double xToBe = (columns+0.5)*squareSize+frameWidth;
            double yToBe = (verse + 0.5)*squareSize+frameWidth;
            double diffX = xToBe - xPresent + translateX;
            double diffY = yToBe - yPresent + translateY;
            System.out.println("XToBe:" + xToBe + " " + "yToBe" + yToBe + " "+ xPresent + " " + yPresent + " ");
            ((Circle)(mouseEvent.getSource())).setTranslateX(diffX);
            ((Circle)(mouseEvent.getSource())).setTranslateY(diffY);
        }
    }; */
 /*   EventHandler<MouseEvent> isAllowed = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {
            double presentX = mouseEvent.getSceneX();
            double presentY = mouseEvent.getSceneY();
            double verse = (presentX-10)/80;
            double columns = (presentY -10)/80;
            ((Circle)(mouseEvent.getSource())).setTranslateX(presentX);
            ((Circle)(mouseEvent.getSource())).setTranslateY(presentY);
        }
    }; */
}
