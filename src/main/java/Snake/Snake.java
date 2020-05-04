package Snake;

import Menu.MainMenu;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Snake {
    //szerokosc i wysokosc liczona w liczbie kwadratow
    public int WID=15;
    public int HEI =15;
    //polowa szerokosci jednego kwadratu
    public int WYMIAR=30;
    private int SPEED=200;
    //szerokosc i wysokosc ekranu
    public double WIDTH;
    public double HEIGHT;
    public Color TLO;

    public boolean dziala=false;
    boolean over=false;
    public boolean wejscie=true;

    Food food;
    //  public int czas=0;
    public GridPane gamePane;
    public SnakePlayer waz;
    public Timeline timeline;
    EventHandler<KeyEvent> eventHandler;
    Random random=new Random();
    Rectangle[] tab;

    public int lastX;
    public int lastY;
    public char DIRECTION='0';

    Scene scene;
    public Stage gameStage;

    public Snake(MainMenu menu){
        gameStage = new Stage();
        TLO=Color.YELLOWGREEN;
        WIDTH=2*WYMIAR*WID;
        HEIGHT=2*WYMIAR*HEI;
        gamePane=new GridPane();
        stworzplansze(gamePane);
        tab=new Rectangle[WID*HEI];
        waz=new SnakePlayer();
        food=new Food();
        scene=new Scene(gamePane,WIDTH,HEIGHT, TLO);
    }

    public void startGame(){
        gameStage.addEventHandler(KeyEvent.KEY_PRESSED,stworzEH());
        gameStage.setTitle("Snake");
        gameStage.setScene(scene);
        gameStage.centerOnScreen();
        gameStage.setResizable(true);
        gameStage.show();
        uplywczasu();
    }

    public EventHandler<KeyEvent> stworzEH(){
        eventHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent wcisnieto) {
                if(wcisnieto.getCode()==KeyCode.ESCAPE){System.exit(0);}
                if(!dziala){
                    if(over){
                        over=false;
                        for(int i=waz.size;i>=0;i--){
                            gamePane.getChildren().remove(tab[i]);
                        }
                        waz=new SnakePlayer();
                        uplywczasu();
                    }
                    dziala=true;
                    timeline.play();
                }
                if (wcisnieto.getCode() == KeyCode.P) gamePause();
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
        //   gameGrid.setGridLinesVisible(true);
    }

    void uplywczasu(){
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(SPEED),
                        event -> {
                            //czas++
                            //w czasie jednego eventu kazdy element weza (getLayoutX, Y) ma caly czas ta samo pozycje mimo zmian
                            ruch();
                            //tylko jedna zmiana kierunku na ruch
                            //jeszeli pozwolic na wiecej to moze dojsc do oszustwa
                            //np. bylo s, wciskamy d, nastepnie szybko w i idziemy w nidozwolonym kierunku
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
            GridPane.setConstraints(foodSq, x, y);
            gamePane.getChildren().add(foodSq);
            food=this;
        }
        void locateFood(){
            if(x==waz.x&&y==waz.y){
                //dodanie nowego ogona
                for(int i=0;i<3;i++) {
                    waz.size++;
                    tab[waz.size] = new Rectangle(2 * WYMIAR, 2 * WYMIAR, Color.BLACK);
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

    public class SnakePlayer {
        int x;
        int y;
        int size;

        SnakePlayer(){
            size=0;
            x=WID/2;
            y= HEI /2;
            tab[0]=new Rectangle(2*WYMIAR,2*WYMIAR,Color.BLUE);
            GridPane.setConstraints(tab[0], x, y);
            gamePane.getChildren().add(tab[0]);
            for(int i=0; i<3;i++) {
                size++;
                tab[size] = new Rectangle(2 * WYMIAR, 2 * WYMIAR, Color.BLACK);
                GridPane.setConstraints(tab[size], x, y);
                gamePane.getChildren().add(tab[size]);
            }
        }

        void wypisz(){
            System.out.println("START");
            for(int i=0;i<=size;i++){
                System.out.println((int) tab[i].getLayoutX()/(2*WYMIAR)+" "+ (int) tab[i].getLayoutY()/(2*WYMIAR));
            }
            System.out.println("KONIEC");
        }
        //przemieszcza calego weza oprocz glowy
        void move(){
            for(int i=size;i>0;i--){
                GridPane.setConstraints(tab[i], (int) tab[i-1].getLayoutX()/(2*WYMIAR), (int) tab[i-1].getLayoutY()/(2*WYMIAR));
            }
        }

        void moveD(){
            x++;
            if(x>=WID||checkZderzenie(x,y)){
                gameOver();
            }
            else{
                GridPane.setConstraints(tab[0], x, y);
            }
        }
        void moveW(){
            y--;
            if(y<0||checkZderzenie(x,y)){
                gameOver();
            }
            else {
                GridPane.setConstraints(tab[0], x, y);
            }
        }
        void moveA(){
            x--;
            if(x<0||checkZderzenie(x,y)){
                gameOver();
            }
            else {
                GridPane.setConstraints(tab[0], x, y);
            }
        }
        void moveS(){
            y++;
            if(y>= HEI ||checkZderzenie(x,y)){
                gameOver();
            }
            else {
                GridPane.setConstraints(tab[0], x, y);
            }
        }

        boolean checkZderzenie(int X, int Y){
            for(int i=0;i<size;i++){
                //   System.out.println(lastX+" last y: "+lastY);
                //   if(X==lastX&&Y==lastY)return false;
                if(X==tab[i].getLayoutX()/(2*WYMIAR)&&Y==tab[i].getLayoutY()/(2*WYMIAR)) {
                    //sluzy do wykrycia bledow w powstawianiu jedzenia
                    System.out.println("Zderzono z  "+i);
                    return true;
                }
            }
            return false;
        }
    }

    public void gamePause(){
        DIRECTION='0';
        dziala=false;
        timeline.stop();
    }

    public void gameOver(){
        gamePause();
        over=true;
    }
}
