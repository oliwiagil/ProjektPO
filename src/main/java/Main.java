//package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Main extends Application {
    public int WIDTH=400;
    public int HEIGHT=400;
    private int SPEED=30;
    public Color TLO =Color.WHITE;

    public Pane gamePane;
    public Timeline timeline;
    EventHandler<KeyEvent> eventHandler;
    Ship gracz;
    ArrayList<Bullet> bullets=new ArrayList<>();
    ArrayList<Enemy> enemys=new ArrayList<>();

    //zmienne odpowiedzialne za to zeby nie mozliwe bylo strzelanie jednym ciagiem
    boolean canShot=true;
    int time;

    @Override
    public void start(Stage primaryStage) throws Exception{
        gamePane =new Pane();
        gracz=new Ship(Color.GREEN);
        gamePane.getChildren().add(gracz);
        createEnemys();

        Scene scene=new Scene(gamePane,WIDTH,HEIGHT,TLO);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,stworzEH());
        primaryStage.setTitle("SpaceInv");
        primaryStage.setScene(scene);
        primaryStage.show();
        uplywczasu();
    }

    public EventHandler<KeyEvent> stworzEH(){
        eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent wcisnieto) {
                if(wcisnieto.getCode()==KeyCode.ESCAPE){System.exit(0);}
                //tylko gdy minal odpowiednio dlugi czas od ostatniego strzalu mozliwy jest strzal
                if(canShot) {
                    if (wcisnieto.getCode() == KeyCode.SPACE) {
                        gracz.shot();
                        canShot=false;
                    }
                }
                if (wcisnieto.getCode() == KeyCode.LEFT || wcisnieto.getCode() == KeyCode.A) {
                    gracz.moveA();
                } else if (wcisnieto.getCode() == KeyCode.RIGHT || wcisnieto.getCode() == KeyCode.D) {
                    gracz.moveD();
                }
            }
        };
        return eventHandler;
    }

    void uplywczasu(){
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(SPEED),
                        event -> {
                            ruch();
                            //po odpowiednim czasie umozliwia kolejny strzal
                            if(!canShot) {
                                time++;
                                if(time%8==0){
                                    time=0;
                                    canShot=true;
                                }
                            }

                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        //uruchamia uplyw czasu
        timeline.play();
    }

    void ruch(){
        ArrayList<Bullet> removeB=new ArrayList<>();
        ArrayList<Enemy> removeE=new ArrayList<>();
        //przemieszcza pociski statku
        for(Bullet i : bullets){
            if(i.moveUp()) {
                for (Enemy j : enemys) {
                    //jezeli jakis pocisk trafil przeciwnika
                    if (i.getBoundsInParent().intersects(j.getBoundsInParent())) {
                        //ukrywam pocisk i przeciwnika
                        j.setVisible(false);
                        i.setVisible(false);
                        //dodaje do listy elementow do usuniecia
                        removeB.add(i);
                        removeE.add(j);
                    }
                }
            }
            //pocisk wyszedl poza plansze wiec jest usuwany
            else{removeB.add(i);}
        }
        //usuwam wszystkie elementy ktore w tym ruchu zostaly trafione
        bullets.removeAll(removeB);
        enemys.removeAll(removeE);
    //    System.out.println(bullets.size());
    }

    void createEnemys(){
        //liczba rzedow przeciwnikow
        int k=4;
        for(double j=30;j<k*30;j=j+30) {
            for (double i = 10; i < WIDTH; i = i + 50) {
                gamePane.getChildren().add(new Enemy(i, j));
            }
        }
    }

    public class Enemy extends Rectangle{
        double currentX;

        Enemy(double x, double y){
            super(30,20,Color.RED);
            //ustala poczatkowa pozycje
            setTranslateX(x);
            setTranslateY(y);
            currentX=x;
            enemys.add(this);
        }

        void shot(){
            bullets.add(new Bullet(currentX));
            System.out.println("shot");
        }
    }

    public class Ship extends Rectangle{
        //obecna pozycja statku
        double currentX;
        //jednostaka o jaka sie przemieszcza
        double move=5;

        Ship(Color color){
            super(30,20,color);
            //ustala poczatkowa pozycje
            setTranslateX((double) WIDTH/2);
            setTranslateY(HEIGHT-20);
            currentX=(double) WIDTH/2;
        }

        void moveD() {
            //30 to wartosc szerokosci prostokata podana w konstruktorze super
            if (currentX+ move+30<=WIDTH) {
                currentX += move;
                setTranslateX(currentX);
            }
        }
        void moveA(){
            if (currentX-move>=0) {
                currentX -= move;
                setTranslateX(currentX);
            }
        }
        void shot(){
            bullets.add(new Bullet(currentX+10));
            System.out.println("shot");
        }
    }

    public class Bullet extends Rectangle{
        boolean delete=false;
        //obecna wysokosc pocisku
        double currentY;
        //jednostaka o jaka sie przemieszcza
        double move=5;

        Bullet(double x){
            super(10,20,Color.GREY);
            //poczatkowa pozycja x jest taka jak statku ktory wystrzeliwuje
            setTranslateX(x);
            //poczatkowa wysokosc pocisku
            setTranslateY(HEIGHT-10);
            currentY=0.9*HEIGHT;
            gamePane.getChildren().add(this);
        }
        //jak statek strzela do gory
        boolean moveUp(){
            if(currentY-move+20>=0) {
                currentY -= move;
                setTranslateY(currentY);
                return true;
            }
            //zwracam false jesli pocisk wyszedl poza plansze i jest do usuniecia w ruch()
            else{return false;}
        }
        //jak przeciwnicy strzelaja w dol
        void moveDown(){
            currentY+=move;
            setTranslateY(currentY);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}