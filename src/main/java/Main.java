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
import java.util.Random;

public class Main extends Application {
    public int WIDTH=400;
    public int HEIGHT=400;
    private int SPEED=30;
    public Color TLO =Color.WHITE;

    public Pane gamePane;
    public Timeline timeline;
    EventHandler<KeyEvent> eventHandler;
    EventHandler<KeyEvent> eventHandler2;
    Ship gracz;
    ArrayList<Bullet> bullets=new ArrayList<>();
    ArrayList<Bullet> bulletsEnemy=new ArrayList<>();
    ArrayList<Enemy> enemys=new ArrayList<>();

    Random random=new Random();

    //zmienne odpowiedzialne za to zeby nie mozliwe bylo strzelanie jednym ciagiem
    boolean canShot=true;
    int time;
    boolean nextWave=false;

    //wartosc true mowi w ktorym kierunku porusza sie statek
    boolean left=false;
    boolean right=false;
    boolean space=false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        gamePane =new Pane();
        gracz=new Ship(Color.GREEN);
        gamePane.getChildren().add(gracz);
        createEnemys();

        Scene scene=new Scene(gamePane,WIDTH,HEIGHT,TLO);

        //pierwszy EH obsluguje wcisniete klawisze, a drugi puszczone
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,stworzEH());
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED,stworzEH2());

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
                        space=true;
                      //  gracz.shot();
                     //   canShot=false;
                    }
                }
                if(wcisnieto.getCode()==KeyCode.ENTER) {
                    //wszyscy zostali trafieni, nowa rozgrywka
                    if (enemys.isEmpty()) {
                        nextWave=true;
                    }
                }
                else if (wcisnieto.getCode() == KeyCode.LEFT || wcisnieto.getCode() == KeyCode.A) {
                    //kierunkiem poruszania sie statku jest lewo
                    left=true;
                } else if (wcisnieto.getCode() == KeyCode.RIGHT || wcisnieto.getCode() == KeyCode.D) {
                    //kierunkiem poruszania sie statku jest prawo
                    right=true;
                }
            }
        };
        return eventHandler;
    }

    public EventHandler<KeyEvent> stworzEH2(){
        eventHandler2 = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent puszczono) {
                if (puszczono.getCode() == KeyCode.LEFT || puszczono.getCode() == KeyCode.A) {
                    //puszczono klawisz, zatem statek przestaje poruszac sie w lewo
                    left=false;
                }
                else if (puszczono.getCode() == KeyCode.RIGHT || puszczono.getCode() == KeyCode.D) {
                    //puszczono klawisz, zatem statek przestaje poruszac sie w prawo
                    right=false;
                }
                else if (puszczono.getCode() == KeyCode.SPACE) {
                    //puszczono spacje, statek przestaje strzelac
                    space=false;
                }
            }
        };
        return eventHandler2;
    }

    //wywoluje ruch i ewentualni umozliwia strzal
    void uplywczasu(){
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(SPEED),
                        event -> {
                            ruch();
                            //po odpowiednim czasie umozliwia kolejny strzal
                            if(!canShot) {
                                time++;
                                //po uplynieciu osmiu cykli mozliwy jest kolejny strzal
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
        //wykonanie ruchu statku
        if(left){gracz.moveA();}
        else if(right){gracz.moveD();}
        if(space&&canShot){gracz.shot(); canShot=false;}

        //jezeli wcisnieto wczesniej enter oraz nie ma juz pociskow i przeciwnikow na planszy to tworzeni sa nowi
        if(nextWave){
            if(bullets.isEmpty()){createEnemys(); nextWave=false;}
        }

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
        removeE.clear();

        for(Bullet i : bulletsEnemy){
            if(i.moveDown()) {
                //jakis pocisk przeciwnika trafil statek
                if (i.getBoundsInParent().intersects(gracz.getBoundsInParent())) {
                    gracz.setVisible(false);
                    i.setVisible(false);
                    //dodaje do listy elementow do usuniecia
                    removeB.add(i);
                    gameOver();
                }
            }
            //pocisk przeciwnika wyszedl poza plansze
            else {
                i.setVisible(false);
                removeB.add(i);
            }
        }
        bulletsEnemy.removeAll(removeB);

        //losowo wybierani sa przeciwnicy ktorzy w tym ruchu strzelaja
        for(Enemy i : enemys){
            if(random.nextInt(250)<1) {i.shot();}
        }
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
        double currentY;

        Enemy(double x, double y){
            super(30,20,Color.RED);
            //ustala poczatkowa pozycje
            setTranslateX(x);
            setTranslateY(y);
            currentX=x;
            currentY=y;
            enemys.add(this);
        }

        void shot(){
            //x jest zwiekszony o 10 aby strzaly wychodzily ze srodka wrogow
            bulletsEnemy.add(new Bullet(currentX+10,currentY));
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
            //x zwiekszony o 10 aby strzaly wychodzily ze srodka statku
            bullets.add(new Bullet(currentX+10));
        }
    }

    public class Bullet extends Rectangle{
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
        Bullet(double x,double y){
            super(10,20,Color.BLACK);
            //poczatkowa pozycja x jest taka jak statku ktory wystrzeliwuje
            setTranslateX(x);
            setTranslateY(y);
            currentY=y;
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
        boolean moveDown(){
            if(currentY+move<HEIGHT) {
                currentY += move;
                setTranslateY(currentY);
                return true;
            }
            else{return false;}
        }
    }

    void gameOver(){
        timeline.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}