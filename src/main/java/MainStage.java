import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

//MainStage must be initialized with stage
/*
 * Stage given in Constructor is used to:
 * - Main menu
 * - Games
 * - setting scenes(which are planned, there will be settings related to games e.g. difficulty level)
 *
 * But overStage and WinStage have own and new stages.
 *
 * There is option to create own stages for games, it must be discussed.
 */
public class MainStage{
    //Here is stage from Constructor
    private Stage primStage;

    //Here are scenes
    private Scene menuScene;
    private Scene settingsScene;

    //Here are references to all games(at now is just one) and one reference
    //lastGame which holds last played game
    private Game lastGame;
    private SpaceInv spaceInvGame;

    /*Here are  public stages to use in every game
    *it means that we use them when we:
    *       - lost game, then we start overStage
    *       - win game, then we start winStage
    */
    public OverStage overStage;
    public WinStage winStage;

    MainStage(Stage s){
        primStage = s;
        lastGame = null;
        spaceInvGame = null;

        overStage = new OverStage();
        winStage = new WinStage();

        createMainMenuScene();
        setMainMenuScene();
    }

    private void createMainMenuScene(){

        //Vertical column of buttons
        VBox columnOfBtns = new VBox();
        columnOfBtns.setAlignment(Pos.CENTER);
        columnOfBtns.setSpacing(10d);
        columnOfBtns.setPrefWidth(120);

        //Text
        Text txt = new Text();
        txt.setText("Games Manager");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 20));

        //Useless for game
        /*List<String> l = Font.getFontNames();
        ChoiceBox cBox = new ChoiceBox<>(FXCollections.observableArrayList(l));
        SingleSelectionModel<String> model = cBox.getSelectionModel();
        model.selectedItemProperty().addListener((observableValue, s, t1) -> txt.setFont(Font.font(t1, 20)));*/

        //Game Space Invaders Button
        Button spaceBtn = new Button();
        spaceBtn.setText("Space Invaders");
        spaceBtn.setMinWidth(columnOfBtns.getPrefWidth());
        spaceBtn.setOnAction((event)->{
            try {
                if(spaceInvGame == null){
                    spaceInvGame = new SpaceInv(primStage, this);
                }
                lastGame = spaceInvGame;
                lastGame.resetGame();
                lastGame.startGame();
            } catch (Exception e){
                System.out.println(e);
            }
        });

        //Exit Button
        Button exitBtn = new Button("Exit");
        exitBtn.setMinWidth(columnOfBtns.getPrefWidth());
        exitBtn.setOnAction((event)->{
            Platform.exit();
        });

        //Add buttons to columnOfButtons
        columnOfBtns.getChildren().addAll(txt, spaceBtn, exitBtn);
        //Scene
        menuScene = new Scene(columnOfBtns, 350, 400);
    }

    private void setMainMenuScene(){
        primStage.setResizable(false);
        primStage.setScene(menuScene);
        primStage.setTitle("SpaceInv");
        primStage.show();
    }

    //This is that window when we lost the game
    public class OverStage extends Stage{
        Scene scene;

        OverStage(){
            GridPane gP = new GridPane();
            gP.setAlignment(Pos.CENTER);
            gP.setHgap(15);
            gP.setVgap(5);


            Text txt = new Text();
            txt.setText("Game Over");
            txt.setTextAlignment(TextAlignment.CENTER);
            txt.setFont(Font.font("Serif", 25));

            Button btn1 = new Button("Again");
            btn1.setOnAction((event) ->{
                try {
                    SpaceInv.sound3.stop();
                    lastGame.resetGame();
                    lastGame.startGame();
                } catch(Exception e){
                    e.printStackTrace();
                } finally{
                    this.hide();
                }
            });

            Button btn2 = new Button("Menu");
            btn2.setOnAction((event)->{
                SpaceInv.sound3.stop();
                setMainMenuScene();
                this.hide();
            });

            gP.add(txt, 0, 0, 3, 1);
            gP.add(btn1, 0, 1);
            gP.add(btn2, 2, 1);

            scene = new Scene(gP, 200, 100);
            this.setScene(scene);
            this.setTitle("Game Over");
            this.centerOnScreen();
            this.setAlwaysOnTop(true);
        }
    }

    //This will be the window when we win the game
    public class WinStage extends Stage{

    }
}
