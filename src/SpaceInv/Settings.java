package SpaceInv;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

public class Settings {

    Settings settings = null;
    SpaceInvMenu spaceInvMenu = null;

    Stage stage = null;
    GridPane grid = null;
    Scene scene = null;

    Settings(SpaceInvMenu sIM){
        settings = this;
        spaceInvMenu = sIM;

        createWindow();
    }

    public void open(){
        stage.show();
    }

    public void close(){
        stage.hide();
    }

    private void createWindow(){

        stage = new Stage();

        grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(5);
        grid.setVgap(5);


        Label lblDiff = new Label("_Difficulty:");
        Label lblRes = new Label("_Size:");

        ChoiceBox chbDiff = new ChoiceBox();
        chbDiff.getItems().addAll("Easy", "Normal", "Hard");
        lblDiff.setLabelFor(chbDiff);
        lblDiff.setMnemonicParsing(true);


        ChoiceBox chbRes = new ChoiceBox();
        chbRes.getItems().addAll(Arrays.asList("1920x1080", "1680x1050", "1400x1050",
                "1600x900", "1280x1024", "1440x900", "1280x800", "1152x864",
                "1280x720", "1024x768", "800x600"));
        lblRes.setLabelFor(chbRes);
        lblRes.setMnemonicParsing(true);

        CheckBox checkFullScr = new CheckBox();
        checkFullScr.setText("Full Screen");

        Button btnBack = new Button("Back");
        btnBack.setOnAction((event) ->{
            settings.close();
            spaceInvMenu.open();
        });

        Button btnStart = new Button("Apply");
        btnStart.setOnAction((event)->{
            String res, diff;
            if(chbRes.getValue() != null) { res = chbRes.getValue().toString(); }
            else{ res = null; }

            if(chbDiff.getValue() != null){ diff = chbDiff.getValue().toString(); }
            else{ diff = null; }

            applyChanges(res, diff, checkFullScr.isSelected());
            spaceInvMenu.game = null;
        });

        grid.add(lblDiff, 0, 0);
        grid.add(chbDiff, 2, 0);
        grid.add(lblRes, 0,1);
        grid.add(chbRes, 2,1);
        grid.add(checkFullScr, 3, 1);
        grid.add(btnBack, 0, 2);
        grid.add(btnStart, 2, 2);

        scene = new Scene(grid);

        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.setResizable(false);
    }

    private void applyChanges(String resolution, String diff, boolean isFullScreen){

        File fl = new File("settings/SpaceInv.cfg");

        try(PrintWriter pw = new PrintWriter(fl)){

            //Resolution
            if(resolution != null && !isFullScreen) {
                int x = resolution.indexOf('x');
                pw.println("width=" + resolution.substring(0, x));
                pw.println("height=" + resolution.substring(x + 1));
            }
            else if(isFullScreen){
                pw.println("width=" + Screen.getPrimary().getBounds().getWidth());
                pw.println("height=" + Screen.getPrimary().getBounds().getHeight());
            }
            else{
                pw.println("width=" + 1000);
                pw.println("height=" + 1000);
            }

            //Difficulty level
            if(diff != null) {
                switch (diff) {
                    case "Easy":
                        pw.println("difficulty="+"EASY");
                        break;
                    case "Normal":
                        pw.println("difficulty="+"NORMAL");
                        break;
                    case "Hard":
                        pw.println("difficulty="+"HARD");
                        break;
                }
            }
            else{
                pw.println("difficulty="+"NORMAL");
            }

            //FullScreen mode
            pw.println("fullscreen="+isFullScreen);
        } catch( Exception e){
            System.out.println(e);
            System.out.println("File: Settings.java");
        }
    }
}
