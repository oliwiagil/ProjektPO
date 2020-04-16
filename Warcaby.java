package sample;

import javafx.application.Application;
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

import java.awt.event.MouseEvent;

import static java.awt.event.MouseEvent.MOUSE_ENTERED;

public class Warcaby extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
            primaryStage.setTitle("Checkers");
            Pane board = performBoard();

            Scene scene = new Scene(board);
            primaryStage.setScene(scene);
            primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    public Pane performBoard(){
        Pane board = new Pane();
        boolean blackOrWhite = true;
        int boardSize = 8;
        int i = 0,j = 0;
        while (i < boardSize) {
            while (j < boardSize) {
                Rectangle square = new Rectangle(i*80, j*80, 80, 80);
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
        int height = 120;
        int width = 40;
        while (j < 2) {
            while (i < boardSize) {
                Circle pawn = new Circle(height, width, 20);
                height += 160;
                pawn.setFill(tempColor);
                board.getChildren().add(pawn);
                i += 2;
            }
            height = 40;
            width += 80;
            i = 1;
            j++;
        }
        i = 0;
        j = 6;
        tempColor = Color.WHITE;
        height = 120;
        width = 520;
        while (j < boardSize) {
            while (i < boardSize) {
                Circle pawn = new Circle(height, width, 20);
                height += 160;
                pawn.setFill(tempColor);
                board.getChildren().add(pawn);
                i += 2;
            }
            height = 40;
            width += 80;
            i = 1;
            j++;
        }
        return board;
    }
}
