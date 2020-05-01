package Menu;

import Checkers.CheckersMenu;
import SpaceInv.*;
import Snake.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class MainMenu {

    Stage thisStage;
    Scene scene;
    Pane pane;
    MainMenu thisMainMenu;
    double windowX, windowY;
    final int sceneWidth = 600, sceneHeight = 600;

    SpaceInvMenu spaceInvMenu = null;
    CheckersMenu checkersMenu = null;
    SnakeMenu snakeMenu = null;

    MainMenu(Stage s){

        thisMainMenu = this;
        thisStage = s;
        thisStage.initStyle(StageStyle.TRANSPARENT);
        createScene();
        thisStage.setScene(scene);
        open();
    }

    public void open(){
        thisStage.show();
    }

    public void close(){
        thisStage.hide();
    }

    private void createScene(){

        pane = new MyPane(thisStage);

        pane.getChildren().add(new Buttons(pane));
        pane.getChildren().addAll(new Views());
        pane.setBackground(null);
        scene = new Scene(pane, sceneWidth, sceneHeight);
        scene.setFill(null);
    }

    private class MyPane extends Pane{

        MyPane(Stage stage){

            this.setOnMouseClicked(mouseEvent->{
                if (mouseEvent.getTarget().getClass().getName() == "Menu.MainMenu$MyPane") {
                    stage.toBack();
                }
            });

            this.setOnMousePressed(mouseEvent -> {
                if(mouseEvent.getTarget().getClass().getName()!="Menu.MainMenu$MyPane") {
                    windowX = stage.getX() - mouseEvent.getScreenX();
                    windowY = stage.getY() - mouseEvent.getScreenY();
                }
            });

            this.setOnMouseDragged(mouseEvent -> {
                if(mouseEvent.getTarget().getClass().getName()!="Menu.MainMenu$MyPane") {
                    stage.setX(mouseEvent.getScreenX() + windowX);
                    stage.setY(mouseEvent.getScreenY() + windowY);
                }
            });

        }
    }

    private class Buttons extends HBox {

        public Buttons(Pane pane){
            Button closeBtn = new Button("X");
            closeBtn.setStyle("-fx-background-color: black;");
            closeBtn.setOnAction( event -> Platform.exit() );

            Button miniBtn = new Button("_");
            miniBtn.setStyle("-fx-background-color: black;");
            miniBtn.setOnAction((event) -> {
                Stage stage = (Stage)pane.getScene().getWindow();
                stage.setIconified(true);
            });

            Tooltip exit = new Tooltip("Exit");
            Tooltip mini = new Tooltip("Minimize");
            Tooltip.install(closeBtn, exit);
            Tooltip.install(miniBtn, mini);

            this.getChildren().add(closeBtn);
            this.getChildren().add(miniBtn);

            this.setLayoutX(265);
            this.setLayoutY(20);
            this.setPadding(new Insets(5));
            this.setSpacing(5);
        }
    }

    public class Views extends ArrayList<ImageView> {

        Views(){
            DropShadow ds = new DropShadow(10, Color.AQUA);
            Image SpaceInv = new Image("file:media/Menu/SpaceInvBtn.png");
            Image SpaceInvMono = new Image("file:media/Menu/SpaceInvBtnMono.png");
            Image GMenager = new Image("file:media/Menu/GMenagerBtn.png");
            Image GMenagerMono = new Image("file:media/Menu/GMenagerBtnMono.png");
            Image Checkers = new Image("file:media/Menu/CheckersBtn.png");
            Image CheckersMono = new Image("file:media/Menu/CheckersBtnMono.png");
            Image Snake = new Image("file:media/Menu/SnakeBtn.png");
            Image SnakeMono = new Image("file:media/Menu/SnakeBtnMono.png");

            ImageView imageViewSpaceInv = new ImageView(SpaceInvMono);
            ImageView imageViewGMenager = new ImageView(GMenagerMono);
            ImageView imageViewCheckers = new ImageView(CheckersMono);
            ImageView imageViewSnake = new ImageView(SnakeMono);


            DropShadow dSFirst = new DropShadow(10, Color.DIMGRAY);
            dSFirst.setOffsetX(-3);
            dSFirst.setOffsetY(3);

            //SpaceInvaders Button //--------------------------------------------------
            imageViewSpaceInv.setEffect(dSFirst);

            imageViewSpaceInv.setOnMouseEntered((MouseEvent event)->{
                imageViewSpaceInv.setEffect(ds);
                imageViewSpaceInv.setImage(SpaceInv);
            });

            imageViewSpaceInv.setOnMouseExited((MouseEvent event)->{
                imageViewSpaceInv.setEffect(dSFirst);
                imageViewSpaceInv.setImage(SpaceInvMono);
            });

            imageViewSpaceInv.setOnMouseClicked(MouseEvent ->{
                if(spaceInvMenu == null){
                    spaceInvMenu = new SpaceInvMenu(thisMainMenu);
                }
                spaceInvMenu.open();
                thisMainMenu.close();
            });

            imageViewSpaceInv.setX(10);
            imageViewSpaceInv.setY(10);

            Tooltip tipSpaceInv = new Tooltip("Play Space Invaders");
            Tooltip.install(imageViewSpaceInv, tipSpaceInv);

            this.add(imageViewSpaceInv);

            //GamesMenager Button //---------------------------------------------------
            imageViewGMenager.setEffect(dSFirst);

            imageViewGMenager.setOnMouseEntered((MouseEvent event)->{
                imageViewGMenager.setEffect(ds);
                imageViewGMenager.setImage(GMenager);
            });

            imageViewGMenager.setOnMouseExited((MouseEvent event)->{
                imageViewGMenager.setEffect(dSFirst);
                imageViewGMenager.setImage(GMenagerMono);
            });

            imageViewGMenager.setOnMouseClicked(MouseEvent ->{

            });

            imageViewGMenager.setX(200);
            imageViewGMenager.setY(200);

            Tooltip tipGMenager = new Tooltip("Additional information");
            Tooltip.install(imageViewGMenager, tipGMenager);

            this.add(imageViewGMenager);

            //Checkers Button //--------------------------------------------------------
            imageViewCheckers.setEffect(dSFirst);

            imageViewCheckers.setOnMouseEntered((MouseEvent event)->{
                imageViewCheckers.setEffect(ds);
                imageViewCheckers.setImage(Checkers);
            });

            imageViewCheckers.setOnMouseExited((MouseEvent event)->{
                imageViewCheckers.setEffect(dSFirst);
                imageViewCheckers.setImage(CheckersMono);
            });

            imageViewCheckers.setOnMouseClicked(MouseEvent->{

            });

            imageViewCheckers.setX(77);
            imageViewCheckers.setY(395);

            Tooltip tipCheckers = new Tooltip("Play Checkers");
            Tooltip.install(imageViewCheckers, tipCheckers);

            this.add(imageViewCheckers);

            //Snake Button //------------------------------------------------------------
            imageViewSnake.setEffect(dSFirst);

            imageViewSnake.setOnMouseEntered((MouseEvent event)->{
                imageViewSnake.setEffect(ds);
                imageViewSnake.setImage(Snake);
            });

            imageViewSnake.setOnMouseExited((MouseEvent event)->{
                imageViewSnake.setEffect(dSFirst);
                imageViewSnake.setImage(SnakeMono);
            });

            imageViewSnake.setOnMouseClicked(MouseEvent->{

            });

            imageViewSnake.setX(326);
            imageViewSnake.setY(10);

            Tooltip tipSnake = new Tooltip("Play Snake");
            Tooltip.install(imageViewSnake, tipSnake);

            this.add(imageViewSnake);
        }
    }
}
