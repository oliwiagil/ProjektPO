import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        new MainStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class OverStage extends Stage{ //Now is useless
        GridPane gP;
        //AnchorPane anch;
        Scene scene;
        //HBox hb;
        Text txt;
        Button btn1;
        Button btn2;
       // Label lbl;

        OverStage(){
            //hb = new HBox();
            gP = new GridPane();
            gP.setAlignment(Pos.CENTER);
            gP.setHgap(15);
            gP.setVgap(5);

            //anch = new AnchorPane();

            txt = new Text();
            txt.setText("Game Over");
            txt.setTextAlignment(TextAlignment.CENTER);
            txt.setFont(Font.font("Serif", 25));
            //xt.setLineSpacing();

            /*lbl = new Label("Game Over");
            lbl.setTextAlignment(TextAlignment.CENTER);
            lbl.setFont(Font.font("Serif", 25));*/

            btn1 = new Button("Again");
            btn1.setOnAction((event) ->{
                try {
                    //startGame(primStage);
                } catch(Exception e){
                    System.out.println(e);
                } finally{
                    this.hide();
                }
            });

            btn2 = new Button("Menu");
            btn2.setOnAction((event)->{
                //startMenu(primStage);
                this.hide();
            });

           // hb.getChildren().addAll(btn1, btn2);

            gP.add(txt, 0, 0, 3, 1);
            gP.add(btn1, 0, 1);
            gP.add(btn2, 2, 1);
           /* AnchorPane.setTopAnchor(txt, 20d);
            AnchorPane.setBottomAnchor(btn1, 10d);
            AnchorPane.setRightAnchor(btn1, 10d);
            AnchorPane.setBottomAnchor(btn2, 10d);
            AnchorPane.setLeftAnchor(btn2, 10d);*/
            /*AnchorPane.setTopAnchor(lbl, 20d);
            AnchorPane.setLeftAnchor(lbl, 20d);
            AnchorPane.setBottomAnchor(hb, 15d);
            AnchorPane.setLeftAnchor(hb, 15d);*/

            scene = new Scene(gP, 200, 100);
            this.setScene(scene);
            this.setTitle("Game Over");
            //gP.setGridLinesVisible(true);
            this.centerOnScreen();
            this.show();
        }
    }
}