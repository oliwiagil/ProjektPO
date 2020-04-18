package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

import static java.awt.event.MouseEvent.MOUSE_ENTERED;
import static java.lang.Math.round;

public class Checkers extends Application {
    double pawnOrgX, pawnOrgY;
    private double translateY;
    private double translateX;
    private final int boardSize = 8;
    private int whiteAtBottom = 1;
    private final int frameExtent = 660;
    private final int squareSize = 80;
    private final int frameWidth = 10;
    private final int pawnRadius = 25;
    private int[][] array;
    @Override
    public void start(Stage primaryStage) throws Exception{
            primaryStage.setTitle("Checkers");
            Pane board = performBoard();
            array = new int[8][8];
            setPieces(array, whiteAtBottom);
            Scene scene = new Scene(board);
            primaryStage.setScene(scene);
            primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public Pane performBoard(){
        Pane board = new Pane();
        Rectangle frame = new Rectangle(0, 0, frameExtent, frameExtent);
        board.getChildren().addAll(frame);
        boolean blackOrWhite = true;
        int i = 0,j = 0;
        while (i < boardSize) {
            while (j < boardSize) {
                Rectangle square = new Rectangle(i*squareSize+frameWidth, j*squareSize+frameWidth, squareSize, squareSize);
                if (blackOrWhite)
                    square.setFill(Color.AZURE);
                else
                    square.setFill(Color.GREEN);
                blackOrWhite = !blackOrWhite;
                board.getChildren().add(square);
                j++;
            }
            blackOrWhite = !blackOrWhite;
            j = 0;
            i++;
        }
        i = 0;
        j = 0;
        Color tempColor = Color.BLACK;
        int height = (int) (frameWidth + 1.5*squareSize);
        int width = (int)(frameWidth + 0.5*squareSize);
        while (j < 2) {
            while (i < boardSize) {
                Circle pawn = setPawn(tempColor, board, height, width);
                height += 2*squareSize;
                i += 2;
            }
            height = (int)(frameWidth + 0.5*squareSize);
            width += squareSize;
            i = 1;
            j++;
        }
        i = 0;
        j = 6;
        tempColor = Color.WHITE;
        height = (int)(frameWidth + 1.5*squareSize);
        width = (int)(frameWidth + 6.5*squareSize);
        while (j < boardSize) {
            while (i < boardSize) {
                Circle pawn = setPawn(tempColor, board, height, width);
                height += 2*squareSize;
                i += 2;
            }
            height = (int)(frameWidth + 0.5*squareSize);
            width += squareSize;
            i = 1;
            j++;
        }
        return board;
    }
    Circle setPawn(Color tempColor, Pane board, int height, int width) {
        Circle pawn = new Circle(height, width, 20);
        pawn.setCursor(Cursor.HAND);
        pawn.setOnMousePressed(pawnPositioning);
        pawn.setOnMouseDragged(pawnMovement);
//        pawn.setOnMouseReleased(idkIsAllowed); //powinna służyć do zaokrąglania pozycji, błąd w implementacji
        pawn.setFill(tempColor);
        board.getChildren().add(pawn);
        return pawn;
    }
    void setPieces(int[][] array, int WhiteAtBottom){
        array[0][1] = 2; array[0][3] = 2; array[0][5] =2; array[0][7] = 2;
        array[1][0] = 2; array[1][2] = 2; array[1][4] =2; array[1][6] = 2;
        array[6][1] = 1; array[6][3] = 1; array[6][5] = 2; array[6][7] = 1;
        array[7][0] = 1; array[7][3] = 1; array[7][5] = 1; array[7][7] = 1;
    }
    EventHandler<MouseEvent> pawnPositioning = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            pawnOrgX = mouseEvent.getSceneX();
            pawnOrgY = mouseEvent.getSceneY();
            translateX = ((Circle)(mouseEvent.getSource())).getTranslateX();
            translateY = ((Circle)(mouseEvent.getSource())).getTranslateY();
        }
    };
    EventHandler<MouseEvent> pawnMovement = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            double yDiff = mouseEvent.getSceneY() - pawnOrgY;
            double xDiff = mouseEvent.getSceneX() - pawnOrgX;
            double xToBe = xDiff + translateX-frameWidth;
            double yToBe = yDiff + translateY-frameWidth;
            ((Circle)(mouseEvent.getSource())).setTranslateX(xToBe);
            ((Circle)(mouseEvent.getSource())).setTranslateY(yToBe);
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
