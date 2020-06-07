package Checkers;
;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


public class Checkers {


    //Settings of game
    public double WIDTH;
    public double HEIGHT;
    public boolean fullScreen;

    public Stage gameStage;
    public boolean pause;
    protected int SPEED;
    protected Pane gamePane;
    protected Timeline timeline;
    protected CheckersMenu returnMenu;

    Scene scene;
    double pawnOrgX, pawnOrgY;
    private double translateY;
    private double translateX;
    private final int boardSize = 8;
    private final int frameExtent = 640;
    private final int squareSize = 80;
    private final int frameWidth = 10;
    private final int pawnRadius = 25;
    private int[][] array; //[x][y]

    public String winnedPlayer;
    public String lostPlayer;

    private String whitePlayerName;
    private String blackPlayerName;

    private int time;

    //Effects
    private DropShadow glow;

    private boolean turn; //true - white, false - black
    AtomicBoolean check = new AtomicBoolean(false); // true - informs whether it gives an effect and prepared board for next move of player
    private Field[][] fields;
    private ArrayList<Pawn> blackPawns;
    private ArrayList<Pawn> whitePawns;
    Label winner = null;
    Label whosMove = null;

    private ArrayList<Pawn> effectOnPawn;
    private ArrayList<Field> effectOnField;

    private Rectangle frame;
    private Image blackPawn = new Image("file:media/Checkers/blackPawn.png");
    private Image whitePawn = new Image("file:media/Checkers/whitePawn.png");
    private Image blackPawnHetman = new Image("file:media/Checkers/blackPawnHetman.png");
    private Image whitePawnHetman = new Image("file:media/Checkers/whitePawnHetman.png");
    private ImagePattern blackField;
    private ImagePattern whiteField;

    private ResultWindow resultWindow;
    private PauseWindow pauseWindow;

    //konstruktor, tutaj tworzymy grę: jej stage(każdy obiekt gry ma swój), scene, i jakieś zmienne potrzebne
    Checkers(CheckersMenu cM){
        returnMenu = cM;
        gameStage = new Stage();
        gameStage.addEventHandler(KeyEvent.KEY_PRESSED, (event)->{
            if(event.getCode() == KeyCode.P){
                gamePause();
            }
        });

        if(returnMenu.settings.firstStyle){
            blackField = new ImagePattern(new Image("file:media/Checkers/blackField.png"));
            whiteField = new ImagePattern(new Image("file:media/Checkers/whiteField.png"));
        } else{
            blackField = new ImagePattern(new Image("file:media/Checkers/blackField2.png"));
            whiteField = new ImagePattern(new Image("file:media/Checkers/whiteField2.png"));
        }

        this.whitePlayerName = returnMenu.settings.whitePlayerName;
        this.blackPlayerName = returnMenu.settings.blackPlayerName;

        if(returnMenu.settings.min instanceof String) {
            if (returnMenu.settings.min.equals("5 min")) {
                time = 5;
            } else if (returnMenu.settings.min.equals("10 min")) {
                time = 10;
            } else if (returnMenu.settings.min.equals("15 min")) {
                time = 15;
            } else if (returnMenu.settings.min.equals("30 min")) {
                time = 30;
            }
        }
        else{
            time = 5;
        }

        setEffects();
        SPEED = 50;
        array = new int[8][8];
        fields = new Field[8][8];
        blackPawns = new ArrayList<>();
        whitePawns = new ArrayList<>();
        effectOnField = new ArrayList<>();
        effectOnPawn = new ArrayList<>();
        turn = true;
        gamePane = performBoard();
        gamePane.setPadding(new Insets(5));

        BorderPane borderPane = new BorderPane();

        winner = new Label();
        winner.setFont(Font.font("Calibri", 15));

        whosMove = new Label();
        whosMove.setFont(Font.font("Calibri", 15));

        VBox vBoxInfo = new VBox();

        Label playerWhite = new Label("Player white: " + whitePlayerName);
        Label whiteTime = new Label("Time: ");
        Label playerBlack = new Label("Player black: " + blackPlayerName);
        Label blackTime = new Label("Time: ");

        vBoxInfo.getChildren().addAll(whosMove, playerWhite, whiteTime, playerBlack, blackTime);
        vBoxInfo.setSpacing(10);

        Label pause = new Label("P - pause game.");
        HBox hBoxBottom = new HBox();

        Button btnBack = new Button("Exit");
        btnBack.setPadding(new Insets(10));
        btnBack.setOnAction((event)->{
            returnMenu.open();
            gameStage.hide();
            returnMenu.game = null;
        });

        hBoxBottom.getChildren().addAll(btnBack, pause);
        hBoxBottom.setSpacing(10);

        borderPane.setTop(vBoxInfo);
        borderPane.setBottom(hBoxBottom);
        borderPane.setCenter(winner);
        borderPane.setPrefWidth(250);
        borderPane.setMaxWidth(250);
        borderPane.setMinWidth(250);
        borderPane.setPadding(new Insets(10));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(gamePane, borderPane);
        hBox.setSpacing(10);

        scene = new Scene(hBox);
        gameStage.setTitle("Checkers");
        gameStage.setScene(scene);
    }

    private void setWhoseMove(){
        if(turn){
            whosMove.setText("Move has: White");
        }
        else whosMove.setText("Move has: Black");
    }

    public void close(){
        gameStage.close();
    }

    //odpalamy grę, pokazujemy stage i włączamy czas

    public void startGame() throws Exception {
        gameStage.show();
        start();
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
        pauseWindow = new PauseWindow(returnMenu);
        pauseWindow.open();
    }

    private void start(){
        int blackAmt = blackPawns.size();
        int whiteAmt = whitePawns.size();


        timeline = new Timeline(
                new KeyFrame(Duration.millis(SPEED),
                        (event ) ->{
                            if(turn){
                                if(!check.get()) {
                                    if(checkBlackWin()){
                                        blackWin();
                                        check.set(true);
                                    }else if(checkAndSetHit(true)){
                                        check.set(true);
                                    }
                                    else if (checkAndSetMove(true)) {
                                        check.set(true);
                                    } else{
                                        if(blackWins()){
                                            blackWin();
                                        } else{
                                            whiteWin();
                                        }
                                        check.set(true);
                                    }
                                }
                            }
                            else{
                                if(!check.get()) {
                                    if(checkWhiteWin()){
                                        whiteWin();
                                        check.set(true);
                                    } else if(checkAndSetHit(false)){
                                        check.set(true);
                                    }
                                    else if (checkAndSetMove(false)) {
                                        check.set(true);
                                    }else{
                                        if(blackWins()){
                                            blackWin();
                                        } else{
                                            whiteWin();
                                        }
                                        check.set(true);
                                    }
                                }
                            }
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        setWhoseMove();
        timeline.play();
    }

    private void blackWin(){
        winner.setText("Black has won!");
        winnedPlayer = blackPlayerName;
        lostPlayer = whitePlayerName;
        timeline.stop();
        if(resultWindow == null) resultWindow = new ResultWindow(returnMenu);
        resultWindow.open();
    }

    private  void whiteWin(){
        winner.setText("White has won!");
        winnedPlayer = whitePlayerName;
        lostPlayer = blackPlayerName;
        timeline.stop();
        if(resultWindow == null) resultWindow = new ResultWindow(returnMenu);
        resultWindow.open();
    }

    private boolean blackWins(){

        int b = 0, w = 0;
        for(Pawn p: blackPawns){
            if(!p.isErased()) b++;
        }
        for(Pawn p: whitePawns){
            if(!p.isErased()) w++;
        }
        return b>w;
    }

    private boolean checkWhiteWin(){
        for(Pawn p: blackPawns){
            if(!(p.isErased())){
                return false;
            }
        }
        return true;
    }

    private boolean checkBlackWin(){
        for(Pawn p: whitePawns){
            if(!(p.isErased())){
                return false;
            }
        }
        return true;
    }

    private boolean checkAndSetMove(boolean isWhite){
        boolean var = false;
        if(isWhite){
            for(Pawn p: whitePawns){
                int x = p.getArrX();
                int y = p.getArrY();

                if(p.isHetman()){
                    if (y != 0) {
                        if (x > 0 && x < 7) {
                            if (array[x - 1][y - 1] == 0 || array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            } else if (array[x - 1][y + 1] == 0 || array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else if (x == 0) {
                            if (array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            } else if (array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else {
                            if (array[x - 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            } else if (array[x - 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        }
                    } else {
                        if (x > 0 && x < 7) {
                            if (array[x - 1][y + 1] == 0 || array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else if (x == 0) {
                            if (array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else {
                            if (array[x - 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        }
                    }
                } else {
                    if (y != 0) {
                        if (x > 0 && x < 7) {
                            if (array[x - 1][y - 1] == 0 || array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else if (x == 0) {
                            if (array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else {
                            if (array[x - 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        }
                    } else {

                    }
                }
            }
        }
        else{
            for(Pawn p: blackPawns){
                int x = p.getArrX();
                int y = p.getArrY();

                if(p.isHetman()){
                    if (y != 7) {
                        if (x > 0 && x < 7) {
                            if (array[x - 1][y + 1] == 0 || array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            } else if (array[x - 1][y - 1] == 0 || array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else if (x == 0) {
                            if (array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            } else if (array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else {
                            if (array[x - 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            } else if (array[x - 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        }
                    } else {
                        if (x > 0 && x < 7) {
                            if (array[x - 1][y - 1] == 0 || array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else if (x == 0) {
                            if (array[x + 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else {
                            if (array[x - 1][y - 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        }
                    }
                } else {
                    if (y != 7) {
                        if (x > 0 && x < 7) {
                            if (array[x - 1][y + 1] == 0 || array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else if (x == 0) {
                            if (array[x + 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        } else {
                            if (array[x - 1][y + 1] == 0) {
                                var = true;
                                p.setEffect(glow);
                                effectOnPawn.add(p);
                                p.setCanBeMoved(true);
                            }
                        }
                    } else {

                    }
                }
            }
        }
        return var;
    }


    private boolean checkAndSetHit(boolean isWhite){
        boolean var = false;

        if(isWhite){
            for(Pawn p: whitePawns){
                if(p.isHetman()){
                    if (hitTopRightHetman(p, p.getArrX(), p.getArrY()) || hitTopLeftHetman(p, p.getArrX(), p.getArrY()) ||
                            hitBotRightHetman(p, p.getArrX(), p.getArrY()) || hitBotLeftHetman(p, p.getArrX(), p.getArrY())) {
                        var = true;
                        p.setEffect(glow);
                        effectOnPawn.add(p);
                        p.setCanHit(true);
                    }
                } else {
                    ArrayList<Point> arrL = new ArrayList<>();
                    if (hitTopRight(p, p.getArrX(), p.getArrY(), arrL) || hitTopLeft(p, p.getArrX(), p.getArrY(), arrL) ||
                            hitBotRight(p, p.getArrX(), p.getArrY(), arrL) || hitBotLeft(p, p.getArrX(), p.getArrY(), arrL)) {
                        var = true;
                        p.setEffect(glow);
                        effectOnPawn.add(p);
                        p.setCanHit(true);
                    }
                }
            }
        }
        else{
            for(Pawn p: blackPawns){
                if(p.isHetman()){
                    if (hitTopRightHetman(p, p.getArrX(), p.getArrY()) || hitTopLeftHetman(p, p.getArrX(), p.getArrY()) ||
                            hitBotRightHetman(p, p.getArrX(), p.getArrY()) || hitBotLeftHetman(p, p.getArrX(), p.getArrY())) {
                        var = true;
                        p.setEffect(glow);
                        effectOnPawn.add(p);
                        p.setCanHit(true);
                    }
                } else {
                    ArrayList<Point> arrL = new ArrayList<>();
                    if (hitTopRight(p, p.getArrX(), p.getArrY(), arrL) || hitTopLeft(p, p.getArrX(), p.getArrY(), arrL) ||
                            hitBotRight(p, p.getArrX(), p.getArrY(), arrL) || hitBotLeft(p, p.getArrX(), p.getArrY(), arrL)) {
                        var = true;
                        p.setEffect(glow);
                        effectOnPawn.add(p);
                        p.setCanHit(true);
                    }
                }
            }

        }

        return var;
    }

    private class Point{
        int x, y;
        Point(int a, int b){
            this.x = a;
            this.y = b;
        }
    }

    private boolean hitTopRight(Pawn p, int x, int y, ArrayList<Point> arr){
        if(y>1 && x<6){
            if(array[x+1][y-1] != 0 && array[x][y] != array[x+1][y-1]){
                if(array[x+2][y-2] == 0){
                    for(Point point: arr){
                        if(x+2 == point.x && y-2 == point.y) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitTopLeft(Pawn p, int x, int y, ArrayList<Point> arr){
        if(y>1 && x>1){
            if(array[x-1][y-1] != 0 && array[x][y] != array[x-1][y-1]){
                if(array[x-2][y-2] == 0){
                    for(Point point: arr){
                        if(x-2 == point.x && y-2 == point.y) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitBotRight(Pawn p, int x, int y, ArrayList<Point> arr){
        if(y<6 && x<6){
            if(x+1 < boardSize && y+1 < boardSize && array[x+1][y+1] != 0 && array[x][y] != array[x+1][y+1]){
                if(array[x+2][y+2] == 0){
                    for(Point point: arr){
                        if(x+2 == point.x && y+2 == point.y) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitBotLeft(Pawn p, int x, int y, ArrayList<Point> arr){
        if(y<6 && x>1){
            if(x-1 >= 0 && y+1 < boardSize &&  array[x-1][y+1] != 0 && array[x][y] != array[x-1][y+1]){
                if(array[x-2][y+2] == 0){
                    for(Point point: arr){
                        if(x-2 == point.x && y+2 == point.y) return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitTopRightHetman(Pawn p, int x, int y){
        if(y>1 && x<6){

            int _x = x+1, _y = y-1;
            while(_x<6 && _y>1 && array[_x][_y] == 0){
                _x+=1;
                _y-=1;
            }
            if(array[_x][_y] != 0 && array[x][y] != array[_x][_y]){
                if(array[_x+1][_y-1] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitTopLeftHetman(Pawn p, int x, int y){
        if(y>1 && x>1){
            int _x = x-1, _y = y-1;
            while(_x>1 && _y>1 && array[_x][_y] == 0){
                _x-=1;
                _y-=1;
            }
            if(array[_x][_y] != 0 && array[x][y] != array[_x][_y]){
                if(array[_x-1][_y-1] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitBotRightHetman(Pawn p, int x, int y){
        if(y<6 && x<6){
            int _x = x+1, _y = y+1;
            while(_x<6 && _y<6 && array[_x][_y] == 0){
                _x+=1;
                _y+=1;
            }
            if(array[_x][_y] != 0 && array[x][y] != array[_x][_y]){
                if(array[_x+1][_y+1] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hitBotLeftHetman(Pawn p, int x, int y){
        if(y<6 && x>1){
            int _x = x-1, _y = y+1;
            while(_x>1 && _y<6 && array[_x][_y] == 0){
                _x-=1;
                _y+=1;
            }
            if(array[_x][_y] != 0 && array[x][y] != array[_x][_y]){
                if(array[_x-1][_y+1] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public Pane performBoard(){
        Pane board = new Pane();
        frame = new Rectangle(10, 10, frameExtent, frameExtent); //tam gdzie ustawimy frame tam będzie cała plansza, pionki i pola są ustawiane względem frame

        board.getChildren().addAll(frame);

        boolean blackOrWhite = true;
        int i = 0, j = 0;
        while (i < boardSize) {
            while (j < boardSize) {
                if (blackOrWhite)
                    fields[i][j] = new Field(i*squareSize+frame.getX(), j*squareSize+frame.getY(), squareSize, squareSize, whiteField);
                else
                    fields[i][j] = new Field(i*squareSize+frame.getX(), j*squareSize+frame.getY(), squareSize, squareSize, blackField);
                blackOrWhite = !blackOrWhite;
                board.getChildren().add(fields[i][j]);
                j++;
            }
            blackOrWhite = !blackOrWhite;
            j = 0;
            i++;
        }

        //First row of black
        int width = (int) (frame.getX() + 1.5*squareSize);
        int height = (int)(frame.getY() + 0.5*squareSize);
        setRowOfPawns(1, 0, width, height, false);
        //Second row of black
        width = (int)(frame.getX() + 0.5*squareSize);
        height += squareSize;
        setRowOfPawns(0, 1, width, height, false);
        //Third row of black
        width = (int)(frame.getX() + 1.5*squareSize);
        height += squareSize;
        setRowOfPawns(1, 2, width, height, false);

        //First row of white
        width = (int)(frame.getX() + 0.5*squareSize);
        height = (int)(frame.getY() + 5.5*squareSize);
        setRowOfPawns(0, 5, width, height, true);
        //Second row of white
        width = (int)(frame.getX() + 1.5*squareSize);
        height += squareSize;
        setRowOfPawns(1, 6, width, height, true);
        //Third row of white
        width = (int)(frame.getX() + 0.5*squareSize);
        height += squareSize;
        setRowOfPawns(0, 7, width, height, true);

        for(Pawn a: blackPawns){
            board.getChildren().add(a);
        }

        for(Pawn a: whitePawns){
            board.getChildren().add(a);
        }

        return board;
    }

    private void setRowOfPawns(int x, int y, int width, int height, boolean white){
        while (x < boardSize) {
            if(white) {
                whitePawns.add(setPawn(whitePawn, width, height, x, y, 1));
                array[x][y] = 1;
            }
            else {
                blackPawns.add(setPawn(blackPawn, width, height, x, y, 2));
                array[x][y] = 2;
            }
            width += 2*squareSize;
            x += 2;
        }
    }


    Pawn setPawn(Image img, int width, int height, int arrX, int arrY, int col){
        Pawn tempPawn = new Pawn(height, width, img, arrY, arrX, col);
        tempPawn.setCursor(Cursor.HAND);
        tempPawn.setOnMousePressed(pawnGetPosition);
        tempPawn.setOnMouseDragged(pawnMovement);
        tempPawn.setOnMouseReleased(pawnSetPosition);
        return tempPawn;
    }

    EventHandler<MouseEvent> pawnGetPosition = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            if(((Pawn)(mouseEvent.getSource())).isCanBeMoved()) {

                array[((Pawn) (mouseEvent.getSource())).getArrX()][((Pawn) (mouseEvent.getSource())).getArrY()] = 0;
                //System.out.println(((Pawn) (mouseEvent.getSource())).getArrX() + " "+
                //                ((Pawn) (mouseEvent.getSource())).getArrY() );
                pawnOrgX = mouseEvent.getSceneX();
                pawnOrgY = mouseEvent.getSceneY();
                translateX = ((ImageView) (mouseEvent.getSource())).getTranslateX();
                translateY = ((ImageView) (mouseEvent.getSource())).getTranslateY();

                for(Field f: effectOnField){
                    f.setCanBePositioned(false);
                    f.setEffect(null);
                }
                if(((Pawn) (mouseEvent.getSource())).isHetman()){
                    effectFieldsHetman(((Pawn) (mouseEvent.getSource())));
                } else {
                    effectFields(((Pawn) (mouseEvent.getSource())));
                }
                //System.out.println(translateX + " " + translateY);
            }
            else if(((Pawn)(mouseEvent.getSource())).isCanHit()) {

                array[((Pawn) (mouseEvent.getSource())).getArrX()][((Pawn) (mouseEvent.getSource())).getArrY()] = 0;
                //System.out.println(((Pawn) (mouseEvent.getSource())).getArrX() + " "+
                //                ((Pawn) (mouseEvent.getSource())).getArrY() );
                pawnOrgX = mouseEvent.getSceneX();
                pawnOrgY = mouseEvent.getSceneY();
                translateX = ((ImageView) (mouseEvent.getSource())).getTranslateX();
                translateY = ((ImageView) (mouseEvent.getSource())).getTranslateY();

                for(Field f: effectOnField){
                    f.setCanBePositioned(false);
                    f.setEffect(null);
                }
                if(((Pawn) (mouseEvent.getSource())).isHetman()){
                    effectFieldsDuringHitHetman(((Pawn) (mouseEvent.getSource())));
                } else {
                    effectFieldsDuringHit(((Pawn) (mouseEvent.getSource())));
                }
                //System.out.println(translateX + " " + translateY);
            }
        }
    };

    EventHandler<MouseEvent> pawnMovement = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(((Pawn)(mouseEvent.getSource())).isCanBeMoved()) {
                double yDiff = mouseEvent.getSceneY() - pawnOrgY;
                double xDiff = mouseEvent.getSceneX() - pawnOrgX;
                double xToBe = xDiff + translateX;
                double yToBe = yDiff + translateY;
                ((ImageView) (mouseEvent.getSource())).setTranslateX(xToBe);
                ((ImageView) (mouseEvent.getSource())).setTranslateY(yToBe);
            } else if (((Pawn)(mouseEvent.getSource())).isCanHit()){
                double yDiff = mouseEvent.getSceneY() - pawnOrgY;
                double xDiff = mouseEvent.getSceneX() - pawnOrgX;
                double xToBe = xDiff + translateX;
                double yToBe = yDiff + translateY;
                ((ImageView) (mouseEvent.getSource())).setTranslateX(xToBe);
                ((ImageView) (mouseEvent.getSource())).setTranslateY(yToBe);
            }
        }
    };

    EventHandler<MouseEvent> pawnSetPosition = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if(((Pawn)(mouseEvent.getSource())).isCanBeMoved()) {
                //double x = mouseEvent.getSceneX();
                //double y = mouseEvent.getSceneY();
                //System.out.println(x + " " + y);
                //System.out.println(ix + " " + iy);
                double centerX = ((Pawn) (mouseEvent.getSource())).getX() + ((Pawn) (mouseEvent.getSource())).getImage().getWidth() / 2
                        + ((Pawn) (mouseEvent.getSource())).getTranslateX();
                double centerY = ((Pawn) (mouseEvent.getSource())).getY() + ((Pawn) (mouseEvent.getSource())).getImage().getHeight() / 2
                        + ((Pawn) (mouseEvent.getSource())).getTranslateY();
                int ix = (int) (centerX / squareSize);
                int iy = (int) (centerY / squareSize);

                if(!(ix>=0 && ix<=7 && iy>=0 && iy<=7)){
                    array[((Pawn) (mouseEvent.getSource())).getArrX()][((Pawn) (mouseEvent.getSource())).getArrY()] = ((Pawn) (mouseEvent.getSource())).getColor();
                    ((Pawn) (mouseEvent.getSource())).setTranslateX(translateX);
                    ((Pawn) (mouseEvent.getSource())).setTranslateY(translateY);
                } else if(fields[ix][iy].isCanBePositioned()) {

                    array[ix][iy] = ((Pawn) (mouseEvent.getSource())).getColor();

                    if(iy == 0 && ((Pawn) (mouseEvent.getSource())).getColor() == 1){
                        makeHetman(((Pawn) (mouseEvent.getSource())));
                    } else if(iy == 7 && ((Pawn) (mouseEvent.getSource())).getColor() == 2) {
                        makeHetman(((Pawn) (mouseEvent.getSource())));
                    }

                    ((Pawn) (mouseEvent.getSource())).setArrX(ix);
                    ((Pawn) (mouseEvent.getSource())).setArrY(iy);

                    //System.out.println( centerX - fields[ix][iy].getX());
                    ((Pawn) (mouseEvent.getSource())).setTranslateX(fields[ix][iy].getWidth() / 2 + fields[ix][iy].getX() - ((Pawn) (mouseEvent.getSource())).getX()
                            - ((Pawn) (mouseEvent.getSource())).getImage().getWidth() / 2);
                    ((Pawn) (mouseEvent.getSource())).setTranslateY(fields[ix][iy].getHeight() / 2 + fields[ix][iy].getY() - ((Pawn) (mouseEvent.getSource())).getY()
                            - ((Pawn) (mouseEvent.getSource())).getImage().getHeight() / 2);

                    endOfPlayerTurn();

                }
                else{
                    array[((Pawn) (mouseEvent.getSource())).getArrX()][((Pawn) (mouseEvent.getSource())).getArrY()] = ((Pawn) (mouseEvent.getSource())).getColor();
                    ((Pawn) (mouseEvent.getSource())).setTranslateX(translateX);
                    ((Pawn) (mouseEvent.getSource())).setTranslateY(translateY);
                }
            } else if (((Pawn)(mouseEvent.getSource())).isCanHit()) {
                //double x = mouseEvent.getSceneX();
                //double y = mouseEvent.getSceneY();
                //System.out.println(x + " " + y);
                //System.out.println(ix + " " + iy);
                double centerX = ((Pawn) (mouseEvent.getSource())).getX() + ((Pawn) (mouseEvent.getSource())).getImage().getWidth() / 2
                        + ((Pawn) (mouseEvent.getSource())).getTranslateX();
                double centerY = ((Pawn) (mouseEvent.getSource())).getY() + ((Pawn) (mouseEvent.getSource())).getImage().getHeight() / 2
                        + ((Pawn) (mouseEvent.getSource())).getTranslateY();
                int ix = (int) (centerX / squareSize);
                int iy = (int) (centerY / squareSize);

                if(!(ix>=0 && ix<=7 && iy>=0 && iy<=7)){
                    ((Pawn) (mouseEvent.getSource())).setTranslateX(translateX);
                    ((Pawn) (mouseEvent.getSource())).setTranslateY(translateY);
                } else if(fields[ix][iy].isCanBePositioned()) {

                    array[ix][iy] = ((Pawn) (mouseEvent.getSource())).getColor();
                    
                    int begX = ((Pawn) (mouseEvent.getSource())).getArrX();
                    int begY = ((Pawn) (mouseEvent.getSource())).getArrY();
                    int vecX = ix-begX < 0 ? -1 : 1;
                    int vecY = iy-begY < 0 ? -1 : 1;
                    begX += vecX;
                    begY += vecY;
                    while(array[begX][begY] == 0){
                        begX += vecX;
                        begY += vecY;
                    }
                    if(((Pawn) (mouseEvent.getSource())).getColor() != array[begX][begY]) {
                        eraseEnemy(begX, begY);
                    }

                    ((Pawn) (mouseEvent.getSource())).setArrX(ix);
                    ((Pawn) (mouseEvent.getSource())).setArrY(iy);

                    //System.out.println( centerX - fields[ix][iy].getX());
                    ((Pawn) (mouseEvent.getSource())).setTranslateX(fields[ix][iy].getWidth() / 2 + fields[ix][iy].getX() - ((Pawn) (mouseEvent.getSource())).getX()
                            - ((Pawn) (mouseEvent.getSource())).getImage().getWidth() / 2);
                    ((Pawn) (mouseEvent.getSource())).setTranslateY(fields[ix][iy].getHeight() / 2 + fields[ix][iy].getY() - ((Pawn) (mouseEvent.getSource())).getY()
                            - ((Pawn) (mouseEvent.getSource())).getImage().getHeight() / 2);

                    if(!checkHit((Pawn) (mouseEvent.getSource()))){
                        endOfPlayerTurn();
                    }

                    if(!(((Pawn) (mouseEvent.getSource())).isHetman())) {
                        if (iy == 0 && ((Pawn) (mouseEvent.getSource())).getColor() == 1) {
                            makeHetman(((Pawn) (mouseEvent.getSource())));
                        } else if (iy == 7 && ((Pawn) (mouseEvent.getSource())).getColor() == 2) {
                            makeHetman(((Pawn) (mouseEvent.getSource())));
                        }
                    }

                    for(Field f: effectOnField){
                        f.setCanBePositioned(false);
                        f.setEffect(null);
                    }
                }
                else{
                    //array[ix][iy] = 0;
                    //array[((Pawn) (mouseEvent.getSource())).getArrX()][((Pawn) (mouseEvent.getSource())).getArrY()] = ((Pawn) (mouseEvent.getSource())).getColor();
                    ((Pawn) (mouseEvent.getSource())).setTranslateX(translateX);
                    ((Pawn) (mouseEvent.getSource())).setTranslateY(translateY);
                }
            }
        }
    };

    private void showBoard(){
        for(int i = 0; i<8; i++){
            System.out.print("| ");
            for(int j = 0; j<8; j++){
                System.out.print(array[j][i]);
                System.out.print(" |");
            }
            System.out.println("");
            for(int j = 0; j<8;j++){
                System.out.print("___");
            }
            System.out.println("");
        }
        for(int j = 0; j<8;j++){
            System.out.print("***");
        }
        System.out.println("");
    }

    private void setEffects(){
        glow = new DropShadow(15, Color.GOLD);
    }

    private void effectFields(Pawn p){
        int x = p.getArrX();
        int y = p.getArrY();
        //System.out.println(x + " " + y);
        if(p.getColor() == 1) {
            if (x > 0 && x < 7) {
                setEffect(x-1, y-1);
                setEffect(x+1, y-1);
            } else if (x == 0) {
                setEffect(x+1, y-1);
            } else {
                setEffect(x-1, y-1);
            }
        }
        else{
            if(x>0 && x<7)
            {
                setEffect(x-1, y+1);
                setEffect(x+1, y+1);
            }
            else if(x == 0){
                setEffect(x+1, y+1);
            }
            else{
                setEffect(x-1, y+1);
            }
        }
    }

    private void effectFieldsHetman(Pawn p){

        int x = p.getArrX()+1;
        int y = p.getArrY()-1;
        while(x<8 && y>-1 && array[x][y] == 0){
            setEffect(x++, y--);
        }

        x = p.getArrX()-1;
        y = p.getArrY()-1;
        while(x>-1 && y>-1 && array[x][y] == 0){
            setEffect(x--, y--);
        }

        x = p.getArrX()-1;
        y = p.getArrY()+1;
        while(x>-1 && y<8 && array[x][y] == 0){
            setEffect(x--, y++);
        }

        x = p.getArrX()+1;
        y = p.getArrY()+1;
        while(x<8 && y<8 && array[x][y] == 0){
            setEffect(x++, y++);
        }
    }

    private void effectFieldsDuringHit(Pawn p){
        int x = p.getArrX();
        int y = p.getArrY();
        //System.out.println(x + " " + y);
        if(p.getColor() == 1) {
            ArrayList<Point> arrL = new ArrayList<>();

            if(hitTopRight(p, x, y, arrL)){
                setEffect(x+2,y-2);
            }
            if(hitTopLeft(p, x, y, arrL)){
                setEffect(x-2, y-2);
            }
            if(hitBotRight(p, x, y, arrL)){
                setEffect(x+2, y+2);
            }
            if(hitBotLeft(p, x, y, arrL)){
                setEffect(x-2, y+2);
            }
        }
        else{
            ArrayList<Point> arrL = new ArrayList<>();

            if(hitTopRight(p, x, y, arrL)){

                setEffect(x+2,y-2);
            }
            if(hitTopLeft(p, x, y, arrL)){
                setEffect(x-2, y-2);
            }
            if(hitBotRight(p, x, y, arrL)){
                setEffect(x+2, y+2);
            }
            if(hitBotLeft(p, x, y, arrL)){
                setEffect(x-2, y+2);
            }
        }
    }

    private void effectFieldsDuringHitHetman(Pawn p){

        int x = p.getArrX()+1;
        int y = p.getArrY()-1;
        while(x<8 && y>-1 && array[x][y] == 0){
            x++; y--;
        }
        if(x<8 && y>-1 && array[x][y] != p.getColor()){
            x++; y--;
        }
        while(x<8 && y>-1 && array[x][y] == 0){
            setEffect(x++, y--);
        }

        x = p.getArrX()-1;
        y = p.getArrY()-1;
        while(x>-1 && y>-1 && array[x][y] == 0){
            x--; y--;
        }
        if(x>-1 && y>-1 && array[x][y] != p.getColor()){
            x--; y--;
        }
        while(x>-1 && y>-1 && array[x][y] == 0){
            setEffect(x--, y--);
        }

        x = p.getArrX()-1;
        y = p.getArrY()+1;
        while(x>-1 && y<8 && array[x][y] == 0){
            x--; y++;
        }
        if(x>-1 && y<8 && array[x][y] != p.getColor()){
            x--; y++;
        }
        while(x>-1 && y<8 && array[x][y] == 0){
            setEffect(x--, y++);
        }

        x = p.getArrX()+1;
        y = p.getArrY()+1;
        while(x<8 && y<8 && array[x][y] == 0){
            x++; y++;
        }
        if(x<8 && y<8 && array[x][y] != p.getColor()){
            x++; y++;
        }
        while(x<8 && y<8 && array[x][y] == 0){
            setEffect(x++, y++);
        }
    }

    private void setEffect(int x, int y){
        if(array[x][y] == 0){
            fields[x][y].setEffect(glow);
            effectOnField.add(fields[x][y]);
            fields[x][y].setCanBePositioned(true);
        }
    }

    private void endOfPlayerTurn(){
        for(Pawn p: effectOnPawn){
            p.setCanBeMoved(false);
            p.setCanHit(false);
            p.setEffect(null);
        }

        for(Field f: effectOnField){
            f.setCanBePositioned(false);
            f.setEffect(null);
        }
        effectOnField.clear();
        effectOnPawn.clear();
        turn = !turn;
        setWhoseMove();
        check.set(false);
    }

    private void eraseEnemy(int x, int y){
        array[x][y] = 0;
        for(Pawn p: blackPawns){
            if(p.getArrX() ==x && p.getArrY() == y){
                p.hide();
                p.setEffect(null);
                p.setErased(true);
            }
        }
        for(Pawn p: whitePawns){
            if(p.getArrX() ==x && p.getArrY() == y){
                p.hide();
                p.setEffect(null);
                p.setErased(true);
            }
        }
    }

    private boolean checkHit(Pawn p){
        if(!p.isHetman()) {
            ArrayList<Point> arrL = new ArrayList<>();
            if (hitTopRight(p, p.getArrX(), p.getArrY(), arrL) || hitTopLeft(p, p.getArrX(), p.getArrY(), arrL) ||
                    hitBotRight(p, p.getArrX(), p.getArrY(), arrL) || hitBotLeft(p, p.getArrX(), p.getArrY(), arrL)) {
                p.setEffect(glow);
                effectOnPawn.add(p);
                p.setCanHit(true);
                return true;
            }
            return false;
        } else {
            if (hitTopRightHetman(p, p.getArrX(), p.getArrY()) || hitTopLeftHetman(p, p.getArrX(), p.getArrY()) ||
                    hitBotRightHetman(p, p.getArrX(), p.getArrY()) || hitBotLeftHetman(p, p.getArrX(), p.getArrY())) {
                p.setEffect(glow);
                effectOnPawn.add(p);
                p.setCanHit(true);
                return true;
            }
            return false;
        }
    }

    private void makeHetman(Pawn p){
        p.setHetman(true);
        if(p.getColor() == 1) p.setImage(whitePawnHetman);
        else p.setImage(blackPawnHetman);
    }
}