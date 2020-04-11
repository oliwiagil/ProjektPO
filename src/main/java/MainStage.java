import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

//MainStage must be initialized with stage
public class MainStage{
    private Stage primStage;
    private Scene menuScene;
    private SpaceInv gameSpaceInv;

    MainStage(Stage s){
        primStage = s;
        createMainMenuScene();
        setMainMenuScene();
    }

    private void createMainMenuScene(){

        //Vertical column of buttons
        VBox columnOfBtns = new VBox();
        columnOfBtns.setAlignment(Pos.CENTER);
        columnOfBtns.setSpacing(10d);

        //Text
        Text txt = new Text();
        txt.setText("Games Manager");
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setFont(Font.font("Serif", 20));

        List<String> l = Font.getFontNames();

        ChoiceBox cBox = new ChoiceBox<>(FXCollections.observableArrayList(l));
        SingleSelectionModel<String> model = cBox.getSelectionModel();
        model.selectedItemProperty().addListener((observableValue, s, t1) -> txt.setFont(Font.font(t1, 20)));

        //Game Space Invaders Button
        Button spaceBtn = new Button();
        spaceBtn.setText("Space Invaders");
        spaceBtn.setOnAction(event -> {
            try {
                //primaryStage.hide();
                gameSpaceInv = new SpaceInv(primStage);
                gameSpaceInv.startGame();
            } catch (Exception e){
                System.out.println(e);
            }
        });

        //Exit Button
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(event ->{
            Platform.exit();
        });

        //Add buttons to columnOfButtons
        columnOfBtns.getChildren().addAll(txt, spaceBtn, exitBtn);
        //Scene and stage
        menuScene = new Scene(columnOfBtns, 350, 400);
    }

    private void setMainMenuScene(){
        primStage.setResizable(false);
        primStage.setScene(menuScene);
        primStage.setTitle("SpaceInv");
        primStage.show();
    }
}
