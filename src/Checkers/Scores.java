package Checkers;

import SpaceInv.SpaceInvMenu;
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


    CheckersMenu returnMenu;
    Scores scores;

    Stage stage;

    public Scores(CheckersMenu menu){
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
        ArrayList<Scores.Score> arr = new ArrayList<>();

        ObservableList<Scores.Score> data = FXCollections.observableArrayList();
        try(Scanner in = new Scanner(Paths.get("scores/Checkers/scores.txt"))){
            while(in.hasNext()){
                String a = in.nextLine();

                Scores.Score tmp = new Scores.Score();
                StringBuilder strBuild = new StringBuilder();
                StringBuilder strBuild2 = new StringBuilder();
                StringBuilder strBuild4 = new StringBuilder();
                int i = 0;

                while(i<a.length() && a.charAt(i) != ' '){
                    strBuild.append(a.charAt(i++));
                }
                i++;
                tmp.setWin(strBuild.toString());


                while(i<a.length() && a.charAt(i) != ' '){
                    strBuild2.append(a.charAt(i++));
                }
                i++;
                tmp.setLost(strBuild2.toString());


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

        TableColumn<Scores.Score, String> column1 = new TableColumn<>("Winner");
        column1.setCellValueFactory(new PropertyValueFactory<>("win"));

        TableColumn<Scores.Score, String> column2 = new TableColumn<>("Loser");
        column2.setCellValueFactory(new PropertyValueFactory<>("lost"));

        TableColumn<Scores.Score, String> column4 = new TableColumn<>("Date");
        column4.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(column1, column2, column4);
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
        private String win;
        private String lost;
        private String date;

        Score(){}
        Score(String name, String number, String date){
            this.win = name;
            this.lost = number;
            this.date = date;
        }

        //Getters
        public String getWin(){ return this.win; }
        public String getLost(){ return this.lost; }
        public String getDate(){ return this.date; }

        //Setters
        public void setWin(String name){ this.win = name; }
        public void setLost(String number){ this.lost = number; }
        public void setDate(String date){ this.date = date; }

    }

}
