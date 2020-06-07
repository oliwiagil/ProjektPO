package Snake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class Snake {
    //szerokosc i wysokosc liczona w liczbie kwadratow
    public int WID=15;
    public int HEI =15;
    //polowa szerokosci jednego kwadratu
    public int WYMIAR=28;
    private int SPEED=200;
    //szerokosc i wysokosc ekranu
    public double WIDTH;
    public double HEIGHT;
    public Color TLO;

    public boolean dziala;
    boolean over;
    public boolean wejscie;
    int tryb;

    Food food;
    SuperFood superFood;
    public int czas;
    public GridPane gamePane;
    public SnakePlayer waz;
    public Timeline timeline;
    EventHandler<KeyEvent> eventHandler;
    Random random=new Random();
    Rectangle[] tab;

    public int lastX;
    public int lastY;
    public char DIRECTION='0';
    public char lastDIRECTION='0';
    public SnakeMenu snakeMenu;
    Scene scene;
    public Stage gameStage;

    private sGamePauseWindow gamePauseWindow = null;
    private sGameOverWindow gameOverWindow = null;

    ImagePattern foodP = new ImagePattern(new Image("file:media/Snake/food.PNG"));
    ImagePattern foodS = new ImagePattern(new Image("file:media/Snake/superfood.PNG"));
    ImagePattern snakeP = new ImagePattern(new Image("file:media/Snake/tail.PNG"));
  //  ImagePattern headP = new ImagePattern(new Image("file:media/Snake/head.PNG"));
    ImagePattern headW = new ImagePattern(new Image("file:media/Snake/headW.PNG"));
    ImagePattern headS = new ImagePattern(new Image("file:media/Snake/headS.PNG"));
    ImagePattern headA = new ImagePattern(new Image("file:media/Snake/headA.PNG"));
    ImagePattern headD = new ImagePattern(new Image("file:media/Snake/headD.PNG"));

    public Snake(SnakeMenu menu){
        gameStage = new Stage();
        snakeMenu = menu;
        //tryb=1;

        //Read settings from file.cfg and set all variables
        try(Scanner in = new Scanner(Paths.get("settings/Snake.cfg"))){
            while(in.hasNext()){
                String line = in.nextLine();
                int pos = line.indexOf('=');
                String temp1 = line.substring(0, pos);
                String temp2 = line.substring(pos + 1);
                if ("difficulty".equals(temp1)) {
                    switch (temp2) {
                        case "EASY":
                            SPEED = 250;
                            break;
                        case "NORMAL":
                            SPEED = 150;
                            break;
                        case "HARD":
                            SPEED = 80;
                            break;
                    }
                }
                else{
                    if("tryb".equals(temp1)){
                        switch(temp2){
                            case "normal":
                                tryb=0;
                                break;
                            case "walls":
                                tryb=1;
                                break;
                        }
                    }
                }
            }
        } catch(Exception e){
            System.out.println(e);
            SPEED=200;
            tryb=0;
        }

        TLO=Color.YELLOWGREEN;
        WIDTH=2*WYMIAR*WID;
        HEIGHT=2*WYMIAR*HEI;
        gamePane=new GridPane();
        stworzplansze(gamePane);
        tab=new Rectangle[WID*HEI];
        waz=new SnakePlayer();
        food=new Food();
        dziala=false;
        over=false;
        wejscie=true;
        scene=new Scene(gamePane,WIDTH,HEIGHT, TLO);
    }

    public void startGame(){
        gameStage.addEventHandler(KeyEvent.KEY_PRESSED,stworzEH());
        gameStage.setTitle("Snake");
        gameStage.setScene(scene);
        gameStage.centerOnScreen();
        gameStage.setResizable(false);
        gameStage.show();
        //zeby w EH uruchomic timeline
        dziala=false;
        over=false;
        superFood=null;
        czas=1;
        uplywczasu();
    }

    public void resetGame(){
        dziala=false;
        for(int i=waz.size;i>=0;i--){
            gamePane.getChildren().remove(tab[i]);
        }
        waz=new SnakePlayer();
    }

    public EventHandler<KeyEvent> stworzEH(){
        eventHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent wcisnieto) {
                if(wcisnieto.getCode()==KeyCode.ESCAPE){System.exit(0);}
                //albo pausa albo gra zostala przegrana
                if(!dziala){
                    if(over){
                        resetGame();
                        startGame();
                    }
                    dziala=true;
                    timeline.play();
                }
                if (wcisnieto.getCode() == KeyCode.P||wcisnieto.getCode() == KeyCode.SPACE) gamePause();
                else if(wejscie) {
                    if (wcisnieto.getCode() == KeyCode.UP || wcisnieto.getCode() == KeyCode.W) {
                        if (DIRECTION != 's') DIRECTION = 'w'; wejscie=false;
                    } else if (wcisnieto.getCode() == KeyCode.DOWN || wcisnieto.getCode() == KeyCode.S) {
                        if (DIRECTION != 'w') DIRECTION = 's'; wejscie=false;
                    } else if (wcisnieto.getCode() == KeyCode.LEFT || wcisnieto.getCode() == KeyCode.A) {
                        if (DIRECTION != 'd') DIRECTION = 'a'; wejscie=false;
                    } else if (wcisnieto.getCode() == KeyCode.RIGHT || wcisnieto.getCode() == KeyCode.D) {
                        if (DIRECTION != 'a') DIRECTION = 'd'; wejscie=false;
                    }
                }
            }
        };
        return eventHandler;
    }

    void stworzplansze(GridPane gameGrid){
        for(int x=0;x<WID;x++){
            gameGrid.addColumn(x,new Rectangle(2*WYMIAR,2*WYMIAR,TLO));
            for(int y = 0; y< HEI; y++){
                gameGrid.addRow(y,new Rectangle(2*WYMIAR,2*WYMIAR,TLO));
            }
        }

      //  gameGrid.setGridLinesVisible(true);

        if(tryb==1){
            int x;
            int y=4;
            Rectangle a[]=new Rectangle[10];
            for(int i=0;i<10;i++) {
                a[i]=new Rectangle(2*WYMIAR,2*WYMIAR, Color.BLACK);
            }

            for(x=5;x<10;x++) {
                GridPane.setConstraints(a[x-5], x, y);
                gamePane.getChildren().add(a[x-5]);
            }
            y=10;
            for(x=5;x<10;x++) {
                GridPane.setConstraints(a[x], x, y);
                gamePane.getChildren().add(a[x]);
            }
        }
    }

    void uplywczasu(){
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(SPEED),
                        event -> {
                            czas++;
                            //w czasie jednego eventu kazdy element weza (getLayoutX, Y) ma caly czas ta samo pozycje mimo zmian
                            ruch();
                            //tylko jedna zmiana kierunku na ruch
                            //jeszeli pozwolic na wiecej to moze dojsc do oszustwa
                            //np. bylo s, wciskamy d, nastepnie szybko w i idziemy w nidozwolonym kierunku

                            //raz na 35 ruchow pojawia sie superfood
                            if(czas%35==0&&superFood==null){
                                superFood=new SuperFood();
                                czas=1;
                            }
                            //po 30 ruchach znika
                            if(czas%30==0&&superFood!=null){
                                superFood.delete();
                                superFood=null;
                                czas=1;
                            }
/*
                            //   wczesniej byly z tym problemy
                            if(superFood==null) {
                                if (czas %35 == 0) {
                                    superFood = new SuperFood();
                                    czas=1;
                                }
                            }
                            else {
                                if (czas % 30 == 0) {
                                    superFood.foodSq.setVisible(false);
                                    superFood = null;
                                    czas=1;
                                }
                            }
*/

                            wejscie=true;
                        }
                )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void ruch(){
        if(DIRECTION!='0') {
            //poprzednie wzpolrzedne ogona aby moc dodac nowy segment w locateFood()
            lastX = (int) tab[waz.size].getLayoutX() / (2 * WYMIAR);
            lastY = (int) tab[waz.size].getLayoutY() / (2 * WYMIAR);
            //przemieszcza calego weza oprocz glowy
            waz.move();

            if (DIRECTION == 'd') {
                waz.moveD();
            } else if (DIRECTION == 'w') {
                waz.moveW();
            } else if (DIRECTION == 'a') {
                waz.moveA();
            } else if (DIRECTION == 's') {
                waz.moveS();
            }

            food.locateFood();
            if(superFood!=null){
                //zostalo zjedzone
                if(superFood.locateSuperFood()){
                    superFood=null;
                    czas=1;
                }
            }
        }
    }

    public class Food{
        int x;
        int y;
        Rectangle foodSq;

        Food(){
            do{
                x=random.nextInt(WID);
            } while(x==waz.x);
            do {
                y = random.nextInt(HEI);
            } while(y==waz.y);

            foodSq=new Rectangle(2*WYMIAR,2*WYMIAR,Color.YELLOW);
            foodSq.setFill(foodP);
            GridPane.setConstraints(foodSq, x, y);
            gamePane.getChildren().add(foodSq);
            food=this;
        }
        void locateFood(){
            if(x==waz.x&&y==waz.y){
                //dodanie nowego ogona
                for(int i=0;i<3;i++) {
                    waz.size++;
                    tab[waz.size] = new Rectangle(2 * WYMIAR, 2 * WYMIAR);
                    tab[waz.size].setFill(snakeP);
                    GridPane.setConstraints(tab[waz.size], lastX, lastY);
                    gamePane.getChildren().add(tab[waz.size]);
                }

                //nowa pozycja jedzenia
                do {
                    x = random.nextInt(WID);
                    y = random.nextInt(HEI);
                }while(waz.checkZderzenie(x,y)||(x==waz.x&&y==waz.y));
                //checkZderzenie nie sprawdza zderzenia z glowa
                GridPane.setConstraints(foodSq, x, y);
            }
        }
    }

    public class SuperFood{
        int x;
        int y;
        Rectangle foodSq;

        SuperFood(){
            do{
                x=random.nextInt(WID);
                y = random.nextInt(HEI);
            } while(waz.checkZderzenie(x,y)||(x==waz.x&&y==waz.y)||(x==food.x&&y==food.y));

            foodSq=new Rectangle(2*WYMIAR,2*WYMIAR);
            foodSq.setFill(foodS);
            GridPane.setConstraints(foodSq, x, y);
            gamePane.getChildren().add(foodSq);
            superFood =this;
        }
        boolean locateSuperFood(){
            if(x==waz.x&&y==waz.y){
                //dodanie nowego ogona, przyrost o 6
                for(int i=0;i<6;i++) {
                    waz.size++;
                    tab[waz.size] = new Rectangle(2 * WYMIAR, 2 * WYMIAR);
                    tab[waz.size].setFill(snakeP);
                    GridPane.setConstraints(tab[waz.size], lastX, lastY);
                    gamePane.getChildren().add(tab[waz.size]);
                }
            delete();
            return true;
            }
            return false;
        }
        void delete(){foodSq.setVisible(false);}
    }

    public class SnakePlayer {
        int x;
        int y;
        int size;

        SnakePlayer(){
            size=0;
            x=WID/2;
            y= HEI /2;
            tab[0]=new Rectangle(2*WYMIAR,2*WYMIAR);
            GridPane.setConstraints(tab[0], x, y);
            gamePane.getChildren().add(tab[0]);
            for(int i=0; i<3;i++) {
                size++;
                tab[size] = new Rectangle(2 * WYMIAR, 2 * WYMIAR);
                tab[size].setFill(snakeP);
                GridPane.setConstraints(tab[size], x, y);
                gamePane.getChildren().add(tab[size]);
            }
        }

        //przemieszcza calego weza oprocz glowy
        void move(){
            for(int i=size;i>0;i--){
                GridPane.setConstraints(tab[i], (int) tab[i-1].getLayoutX()/(2*WYMIAR), (int) tab[i-1].getLayoutY()/(2*WYMIAR));
            }
        }

        void moveD(){
            x++;
            tab[0].setFill(headD);
            if(x>=WID||checkZderzenie(x,y)){
                gameOver();
            }
            else{
                GridPane.setConstraints(tab[0], x, y);
            }
        }
        void moveW(){
            y--;
            tab[0].setFill(headW);
            if(y<0||checkZderzenie(x,y)){
                gameOver();
            }
            else {
                GridPane.setConstraints(tab[0], x, y);
            }
        }
        void moveA(){
            x--;
            tab[0].setFill(headA);
            if(x<0||checkZderzenie(x,y)){
                gameOver();
            }
            else {
                GridPane.setConstraints(tab[0], x, y);
            }
        }
        void moveS(){
            y++;
            tab[0].setFill(headS);
            if(y>= HEI ||checkZderzenie(x,y)){
                gameOver();
            }
            else {
                GridPane.setConstraints(tab[0], x, y);
            }
        }

        boolean checkZderzenie(int X, int Y){
            for(int i=0;i<size;i++){
                if(X==tab[i].getLayoutX()/(2*WYMIAR)&&Y==tab[i].getLayoutY()/(2*WYMIAR)) {
                    return true;
                }
                if(tryb==1) {
                    if (Y == 4 || Y == 10) {
                        if (X > 4 && X < 10) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public void gamePause(){
        lastDIRECTION=DIRECTION;
        DIRECTION='0';
        dziala=false;
        timeline.stop();
        if(!over) {
            if (gamePauseWindow == null) {
                gamePauseWindow = new sGamePauseWindow(snakeMenu);
            }
            gamePauseWindow.open(lastDIRECTION);
            System.out.println("open");
        }
    }

    public void gameOver(){
        over=true;
        gamePause();
        if(superFood!=null) {
            superFood.delete();
            superFood = null;
        }
        gameOverWindow = new sGameOverWindow(snakeMenu);
        gameOverWindow.open();
        czas=1;
    }
}
