package SpaceInv;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Scores {

    SpaceInvMenu returnMenu;
    Scores scores;

    Stage stage;

    public Scores(SpaceInvMenu menu){
        scores = this;
        returnMenu = menu;
        stage = new Stage();

        createView();
    }

    public void open(){
        stage.show();
    }

    public void close(){
        stage.close();
    }

    private void createView(){
        ArrayList<Score> arr = new ArrayList<>();

        ObservableList<Score> data = FXCollections.observableArrayList();
        try(Scanner in = new Scanner(Paths.get("scores/SpaceInv/scores.txt"))){
            while(in.hasNext()){
                String a = in.nextLine();

                Score tmp = new Score();
                StringBuilder strBuild = new StringBuilder();
                StringBuilder strBuild2 = new StringBuilder();
                StringBuilder strBuild3 = new StringBuilder();
                StringBuilder strBuild4 = new StringBuilder();
                int i = 0;

                while(i<a.length() && a.charAt(i) != ' '){
                        strBuild.append(a.charAt(i++));
                }

                i++;
                tmp.setName(strBuild.toString());


                while(i<a.length() && a.charAt(i) != ' '){
                    strBuild2.append(a.charAt(i++));
                }
                i++;

                tmp.setNumber(strBuild2.toString());


                while(i<a.length() && a.charAt(i) != ' '){
                    strBuild3.append(a.charAt(i++));
                }
                i++;

                tmp.setDiff(strBuild3.toString());


                while(i<a.length()){
                    strBuild4.append(a.charAt(i++));
                }
                i++;

                tmp.setDate(strBuild4.toString());
                arr.add(tmp);
            }
        } catch (Exception e){
            System.out.println("FILE: Scores.java");
        }

        for(int i = arr.size()-1; i>=0; i--){
            data.add(arr.get(i));
        }

        TableView tableView = new TableView();
        //tableView.setPadding(new Insets(50, 30, 50, 30));

        TableColumn<Score, String> column1 = new TableColumn<>("Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Score, String> column2 = new TableColumn<>("Number");
        column2.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Score, String> column3 = new TableColumn<>("Difficulty");
        column3.setCellValueFactory(new PropertyValueFactory<>("diff"));

        TableColumn<Score, String> column4 = new TableColumn<>("Date");
        column4.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(column1, column2, column3, column4);
        tableView.setItems(data);

        Label title = new Label("Scores");
        title.setFont(new Font("Arial", 30));
        title.setPadding(new Insets(5));

        BorderPane bPane = new BorderPane();

        Button back = new Button("Back");
        back.setOnAction((event)->{
            returnMenu.open();
            scores.close();
        });

        bPane.setRight(back);

        VBox vbox = new VBox(title, tableView, bPane);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 20, 15, 15));
        Scene scene = new Scene(vbox, 550, 450);
        scene.setFill(Color.WHITE);

        stage.setScene(scene);
        stage.setTitle("Scores");
    }

    public class Score{
        private String name;
        private String number;
        private String diff;
        private String date;

        Score(){}
        Score(String name, String number, String diff, String date){
            this.name = name;
            this.number = number;
            this.diff = diff;
            this.date = date;
        }

        //Getters
        public String getName(){ return this.name; }
        public String getNumber(){ return this.number; }
        public String getDiff(){ return this.diff; }
        public String getDate(){ return this.date; }

        //Setters
        public void setName(String name){ this.name = name; }
        public void setNumber(String number){ this.number = number; }
        public void setDiff(String diff){ this.diff = diff; }
        public void setDate(String date){ this.date = date; }

    }
}
