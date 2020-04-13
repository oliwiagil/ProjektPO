import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInv extends Game{

    private EventHandler<KeyEvent> eventHandler;
    private EventHandler<KeyEvent> eventHandler2;
    private Ship gracz;
    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> bulletsEnemy;
    private ArrayList<Enemy> enemies;
    private Scene scene;

    private Random random;

    //zmienne odpowiedzialne za to zeby nie mozliwe bylo strzelanie jednym ciagiem
    private boolean canShot;
    private int time;
    private boolean nextWave;

    //wartosc true mowi w ktorym kierunku porusza sie statek
    private boolean left;
    private boolean right;
    private boolean space;

    //audio
    //https://archive.org/details/RIFLEGUNTIKKAT3TACTICALSHOT01
    //Mattias Michael Lahoud
    static AudioClip sound1 = new AudioClip(new File("media/t2Expl.mp3").toURI().toString());
    //https://archive.org/details/GunSound
    static AudioClip sound2 = new AudioClip(new File("media/shot.mp3").toURI().toString());
    //https://archive.org/details/BigExplosionEffectVideoMp4HDSound
    static AudioClip sound3 = new AudioClip(new File("media/explosion.mp3").toURI().toString());

    //images
    ImagePattern pattern;
    ImagePattern enemy1g = new ImagePattern(new Image("file:media/enemy1g.png"));
    ImagePattern enemy1b = new ImagePattern(new Image("file:media/enemy1b.png"));
    ImagePattern enemy1w = new ImagePattern(new Image("file:media/enemy1w.png"));
    ImagePattern enemy2g = new ImagePattern(new Image("file:media/enemy2g.png"));
    ImagePattern enemy3b = new ImagePattern(new Image("file:media/enemy3b.png"));
    ImagePattern enemy3f = new ImagePattern(new Image("file:media/enemy3f.png"));
    ImagePattern ship3b = new ImagePattern(new Image("file:media/ship3b.png"));
    ImagePattern enemyhit = new ImagePattern(new Image("file:media/explosion2.png"));

    SpaceInv(Stage stage, MainStage st){
        primaryStage = stage;
        returnMain = st;
        //wszystkie wymiary skalowane sa wzgledem stalej WIDTH
        WIDTH = 800;
        HEIGHT = WIDTH;
        SPEED = 30;
        gamePane = new Pane();
        gracz=new Ship();
        gamePane.getChildren().add(gracz);
        bullets = new ArrayList<>();
        bulletsEnemy = new ArrayList<>();
        enemies = new ArrayList<>();
        createEnemies();

        scene = new Scene(gamePane,WIDTH,HEIGHT);

        random = new Random();
        canShot=true;
        nextWave=false;
        left=false;
        right=false;
        space=false;

        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, null,null);
        gamePane.setBackground(new Background(backgroundFill));
    }

    public void startGame() throws Exception{
        //pierwszy EH obsluguje wcisniete klawisze, a drugi puszczone
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,stworzEH());
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED,stworzEH2());

        primaryStage.setTitle("SpaceInvaders");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
        primaryStage.show();
        uplywczasu();
    }

    public void resetGame(){
        for(Bullet b: bullets){
            b.setVisible(false);
        }
        for(Bullet b: bulletsEnemy){
            b.setVisible(false);
        }
        for(Enemy e: enemies){
            e.setVisible(false);
        }
        bullets.clear();
        bulletsEnemy.clear();
        enemies.clear();

        createEnemies();
        gracz.setVisible(true);
        gracz.setPosition();
        canShot=true;
        nextWave=false;
        left=false;
        right=false;
        space=false;
    }

    private EventHandler<KeyEvent> stworzEH(){
        eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent wcisnieto) {
                if(wcisnieto.getCode()== KeyCode.ESCAPE){System.exit(0);}
                //tylko gdy minal odpowiednio dlugi czas od ostatniego strzalu mozliwy jest strzal
                if(canShot) {
                    if (wcisnieto.getCode() == KeyCode.SPACE) {
                        space=true;
                    }
                }
                if(wcisnieto.getCode()==KeyCode.ENTER) {
                    //wszyscy zostali trafieni, nowa rozgrywka
                    if (enemies.isEmpty()) {
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

    private EventHandler<KeyEvent> stworzEH2(){
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
    private void uplywczasu(){
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

    public static class Explosion extends Thread {
        Enemy x;

        public Explosion(Enemy x){this.x=x;}
        @Override
        public void run() {
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                x.setVisible(false);
            }
        }
    }

    private void ruch(){
        //wykonanie ruchu statku
        if(left){gracz.moveA();}
        else if(right){gracz.moveD();}
        if(space&&canShot){gracz.shot(); canShot=false;}

        //jezeli wcisnieto wczesniej enter oraz nie ma juz pociskow i przeciwnikow na planszy to tworzeni sa nowi
        if(nextWave){
            if(bullets.isEmpty()){createEnemies(); nextWave=false;}
        }

        ArrayList<Bullet> removeB=new ArrayList<>();
        ArrayList<Enemy> removeE=new ArrayList<>();
        //przemieszcza pociski statku
        for(Bullet i : bullets){
            if(i.moveUp()) {
                for (Enemy j : enemies) {
                    //jezeli jakis pocisk trafil przeciwnika
                    if (i.getBoundsInParent().intersects(j.getBoundsInParent())) {
                        sound1.play();
                        j.setFill(enemyhit);
                        //ukrywam pocisk i przeciwnika
                        new Explosion(j).start();
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
        enemies.removeAll(removeE);
        removeB.clear();

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
        for(Enemy i : enemies){
            if(random.nextInt(250)<1) {i.shot();}
        }
    }

    private void createEnemies(){
        //liczba rzedow przeciwnikow
        int k=5;
        //odstep w pionie
        int gap=(int) ((WIDTH/10)*0.8);
        int start=(int) ((WIDTH/10)*0.2);
        int end=(int) ((WIDTH/10)*0.9);
        for(double j=gap;j<=k*gap;j=j+gap) {
            for (double i = start; i < WIDTH; i = i + end) {
                gamePane.getChildren().add(new Enemy(i, j,(int) j/gap));
            }
        }
    }

    private class Enemy extends Rectangle {
        double currentX;
        double currentY;

        Enemy(double x, double y, int type){
            super((WIDTH/10.0)*0.6,(WIDTH/10.0)*0.4);
            switch (type){
                case 1: pattern = enemy2g;
                        break;
                case 2: pattern = enemy1g;
                        break;
                case 3: pattern = enemy1b;
                        break;
                case 4: pattern = enemy3b;
                        break;
                case 5: pattern = enemy3f;
                        break;
                default: pattern = enemy1w;
                        break;
            }
            this.setFill(pattern);
            //ustala poczatkowa pozycje
            setTranslateX(x);
            setTranslateY(y);
            currentX=x;
            currentY=y;
            enemies.add(this);
        }

        void shot(){
            //x jest zwiekszony o correction aby strzaly wychodzily ze srodka wrogow
            double correction=(WIDTH/10.0)*0.28;
            bulletsEnemy.add(new Bullet(currentX+correction,currentY));
        }
    }

    private class Ship extends Rectangle{
        //obecna pozycja statku
        double currentX;
        //jednostaka o jaka sie przemieszcza
        double move=(WIDTH/10.0)*0.1;

        Ship(){
            super((WIDTH/10.0)*0.6,(WIDTH/10.0)*0.4);

            pattern = ship3b;
            this.setFill(pattern);

            //ustala poczatkowa pozycje
            setPosition();
        }

        void moveD() {
            //(WIDTH/10.0)*0.6 to wartosc szerokosci prostokata podana w konstruktorze super
            if (currentX+ move+(WIDTH/10.0)*0.6<=WIDTH) {
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
            sound2.play();
            //x jest zwiekszony o correction aby strzaly wychodzily ze srodka wrogow
            double correction=(WIDTH/10.0)*0.28;
            bullets.add(new Bullet(currentX+correction));
        }

        void setPosition(){
            setTranslateX(WIDTH/2);
            setTranslateY(HEIGHT-(HEIGHT/10.0)*0.8);
            currentX=WIDTH/2;
        }
    }

    private class Bullet extends Rectangle{
        //obecna wysokosc pocisku
        double currentY;
        //jednostaka o jaka sie przemieszcza
        double moveU=(WIDTH/10.0)*0.3;
        double moveD=(WIDTH/10.0)*0.1;

        Bullet(double x){
            super((WIDTH/10.0)*0.06,(WIDTH/10.0)*0.4,Color.CHARTREUSE);
            //poczatkowa pozycja x jest taka jak statku ktory wystrzeliwuje
            setTranslateX(x);
            //poczatkowa wysokosc pocisku
            setTranslateY(HEIGHT-(WIDTH/10.0)*0.2);
            currentY=0.9*HEIGHT;
            gamePane.getChildren().add(this);
        }
        Bullet(double x,double y){
            super((WIDTH/10.0)*0.06,(WIDTH/10.0)*0.4,Color.WHITE);
            //poczatkowa pozycja x jest taka jak statku ktory wystrzeliwuje
            setTranslateX(x);
            setTranslateY(y);
            currentY=y;
            gamePane.getChildren().add(this);
        }
        //jak statek strzela do gory
        boolean moveUp(){
            if(currentY-moveU+(WIDTH/10.0)*0.6>=0) {
                currentY -= moveU;
                setTranslateY(currentY);
                return true;
            }
            //zwracam false jesli pocisk wyszedl poza plansze i jest do usuniecia w ruch()
            else{return false;}
        }
        //jak przeciwnicy strzelaja w dol
        boolean moveDown(){
            if(currentY+moveD<HEIGHT) {
                currentY += moveD;
                setTranslateY(currentY);
                return true;
            }
            else{return false;}
        }
    }

    private void gameOver(){
        sound3.play();
        timeline.stop();
        returnMain.overStage.show();
    }
}
