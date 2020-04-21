//import com.sun.glass.ui.Screen;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SpaceInv extends Game{

    private EventHandler<KeyEvent> eventHandler;
    private EventHandler<KeyEvent> eventHandler2;
    private Ship gracz;
    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> bulletsEnemy;
    private ArrayList<Enemy> enemies;
    private ArrayList<Ship> lives;
    private ArrayList<Rectangle> score;
    private Scene scene;

    private Random random;

    //zmienne odpowiedzialne za to zeby nie mozliwe bylo strzelanie jednym ciagiem
    private boolean canShot;
    private int time;
    private boolean nextWave;
    private boolean pause;

    //wartosc true mowi w ktorym kierunku porusza sie statek
    private boolean left;
    private boolean right;
    private boolean space;

    //sterownie poruszaniem wrogow
    private boolean moveR =true;
    private boolean moveS=false;
    private double moveEnemy;
    private double increaseSpeed;
    private double sizeE;

    private int life;
    private double korekta;
    private int scoreI;

    //audio
    //https://archive.org/details/RIFLEGUNTIKKAT3TACTICALSHOT01
    //Mattias Michael Lahoud
    static AudioClip sound1 = new AudioClip(new File("media/SpaceInv/t2Expl.mp3").toURI().toString());
    //https://archive.org/details/GunSound
    static AudioClip sound2 = new AudioClip(new File("media/SpaceInv/shot.mp3").toURI().toString());
    //https://archive.org/details/BigExplosionEffectVideoMp4HDSound
    static AudioClip sound3 = new AudioClip(new File("media/SpaceInv/explosion.mp3").toURI().toString());
    //https://archive.org/details/RoyaltyFanfareHornsSoundEffectLoudTronix.meSQ1
    static AudioClip soundWin = new AudioClip(new File("media/SpaceInv/fanfare.mp3").toURI().toString());

    //images
    ImagePattern pattern;
    ImagePattern enemy1g = new ImagePattern(new Image("file:media/SpaceInv/enemy1g.PNG"));
    ImagePattern enemy1b = new ImagePattern(new Image("file:media/SpaceInv/enemy1b.PNG"));
    ImagePattern enemy2g = new ImagePattern(new Image("file:media/SpaceInv/enemy2g.PNG"));
    ImagePattern enemy3b = new ImagePattern(new Image("file:media/SpaceInv/enemy3b.PNG"));
    ImagePattern enemy3f = new ImagePattern(new Image("file:media/SpaceInv/enemy3f.PNG"));
    ImagePattern ship3b = new ImagePattern(new Image("file:media/SpaceInv/ship3b.PNG"));
    static ImagePattern explosion = new ImagePattern(new Image("file:media/SpaceInv/explosion2.PNG"));
    static ImagePattern explosionB = new ImagePattern(new Image("file:media/SpaceInv/explosion2b.PNG"));
    static ImagePattern explosionG = new ImagePattern(new Image("file:media/SpaceInv/explosion2g.PNG"));
    static ImagePattern explosionF = new ImagePattern(new Image("file:media/SpaceInv/explosion2f.PNG"));
    ImagePattern zero = new ImagePattern(new Image("file:media/SpaceInv/zero.PNG"));
    ImagePattern jeden = new ImagePattern(new Image("file:media/SpaceInv/jeden.PNG"));
    ImagePattern dwa = new ImagePattern(new Image("file:media/SpaceInv/dwa.PNG"));
    ImagePattern trzy = new ImagePattern(new Image("file:media/SpaceInv/trzy.PNG"));
    ImagePattern cztery = new ImagePattern(new Image("file:media/SpaceInv/cztery.PNG"));
    ImagePattern piec = new ImagePattern(new Image("file:media/SpaceInv/piec.PNG"));
    ImagePattern szesc = new ImagePattern(new Image("file:media/SpaceInv/szesc.PNG"));
    ImagePattern siedem = new ImagePattern(new Image("file:media/SpaceInv/siedem.PNG"));
    ImagePattern osiem = new ImagePattern(new Image("file:media/SpaceInv/osiem.PNG"));
    ImagePattern dziewiec = new ImagePattern(new Image("file:media/SpaceInv/dziewiec.PNG"));

    SpaceInv(MainStage st){

        gameStage = new Stage();
        returnMain = st;

        //Read settings from file.cfg and set all variables
        try(Scanner in = new Scanner(Paths.get("settings/SpaceInv.cfg"))){
            while(in.hasNext()){
                String line = in.nextLine();
                int pos = line.indexOf('=');
                String temp1 = line.substring(0, pos);
                String temp2 = line.substring(pos + 1);
                switch(temp1){
                    case "difficulty":
                            switch(temp2){
                                case "EASY": difficulty = DIFFICULTY.EASY; break;
                                case "NORMAL": difficulty = DIFFICULTY.NORMAL; break;
                                case "HARD": difficulty = DIFFICULTY.HARD; break;
                            }
                        break;
                    case "height":
                        HEIGHT = Double.parseDouble(temp2);
                        break;
                    case "width":
                        WIDTH = Double.parseDouble(temp2);
                        break;
                    case "fullscreen":
                            switch(temp2){
                                case "true": fullScreen = true; break;
                                case "false": fullScreen = false; break;
                            }
                        break;
                }
            }
        } catch(Exception e){
            System.out.println(e);
            System.out.println("Line: "+ 103+" File: SpaveInv.java");
            difficulty = DIFFICULTY.NORMAL;
            HEIGHT = 1000;
            WIDTH = 1000;
            fullScreen = false;
        } finally {
            //gra wymaga kwadratowego okna
            korekta=(WIDTH-HEIGHT)/2;
        }

        moveEnemy =((WIDTH-2*korekta)/10.0)*0.02;
        increaseSpeed=((WIDTH-2*korekta)/10.0)*0.02;
        sizeE=40;
        life=3;
        pause=false;
        scoreI=0;

        SPEED = 30;
        gamePane = new Pane();
        gracz=new Ship();
        gamePane.getChildren().add(gracz);

        bullets = new ArrayList<>();
        bulletsEnemy = new ArrayList<>();
        enemies = new ArrayList<>();
        lives=new ArrayList<>();
        score=new ArrayList<>();
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

        createInf();
    }

    public void createInf(){
        //dodaje sciany (tylko uwidaczniaja istniejace ograniczenia)
        Rectangle wallL=new Rectangle(5,HEIGHT,Color.GREY);
        Rectangle wallR=new Rectangle(5,HEIGHT,Color.GREY);
        wallL.setTranslateX(korekta-5);
        wallR.setTranslateX(WIDTH-korekta);
        gamePane.getChildren().add(wallL);
        gamePane.getChildren().add(wallR);

        //dodaje informacje o zyciach
        if(korekta>((WIDTH - 2 * korekta) / 32.0)) {
            for (int i = 0; i < life; i++) {
                lives.add(new Ship());
                lives.get(i).setTranslateX(korekta / 2 - ((WIDTH - 2 * korekta) / 32.0));
                lives.get(i).setTranslateY(((HEIGHT / 10.0) * 0.2) * (i + 1) + i * ((HEIGHT / 10.0) * 0.4));
                gamePane.getChildren().add(lives.get(i));
            }
        }
        else{
            for (int i = 0; i < life; i++) {
                lives.add(new Ship());
                lives.get(i).setTranslateX(-100);
                lives.get(i).setTranslateY(-100);
                gamePane.getChildren().add(lives.get(i));
            }
        }

        //score
        for(int i=0;i<3;i++){
            //trzeba jeszcze dobrze dobrac przesuniecia
            score.add(new Rectangle((korekta-korekta/2)/3,(5*(korekta/3))/6));
            score.get(i).setTranslateX(korekta/10+i*(korekta/3));
            //
            score.get(i).setTranslateY((((HEIGHT / 10.0) * 0.2) * (life + 1) + life * ((HEIGHT / 10.0) * 0.4))*2);
            score.get(i).setFill(zero);
            gamePane.getChildren().add(score.get(i));
        }
    }

    public void scoreAkt(){
        if((scoreI%10)==1){
            score.get(2).setFill(jeden);
        } else if((scoreI%10)==2){
            score.get(2).setFill(dwa);
        } else if((scoreI%10)==3){
            score.get(2).setFill(trzy);
        } else if((scoreI%10)==4){
            score.get(2).setFill(cztery);
        } else if((scoreI%10)==5){
            score.get(2).setFill(piec);
        } else if((scoreI%10)==6){
            score.get(2).setFill(szesc);
        } else if((scoreI%10)==7){
            score.get(2).setFill(siedem);
        } else if((scoreI%10)==8){
            score.get(2).setFill(osiem);
        } else if((scoreI%10)==9){
            score.get(2).setFill(dziewiec);
        } else if((scoreI%10)==0){
            score.get(2).setFill(zero);
        }

        int tmp=scoreI/10;
        if((tmp%10)==1){
            score.get(1).setFill(jeden);
        } else if((tmp%10)==2){
            score.get(1).setFill(dwa);
        } else if((tmp%10)==3){
            score.get(1).setFill(trzy);
        } else if((tmp%10)==4){
            score.get(1).setFill(cztery);
        } else if((tmp%10)==5){
            score.get(1).setFill(piec);
        } else if((tmp%10)==6){
            score.get(1).setFill(szesc);
        } else if((tmp%10)==7){
            score.get(1).setFill(siedem);
        } else if((tmp%10)==8){
            score.get(1).setFill(osiem);
        } else if((tmp%10)==9){
            score.get(1).setFill(dziewiec);
        } else if((tmp%10)==0){
            score.get(1).setFill(zero);
        }

        tmp=tmp/10;
        if((tmp%10)==1){
            score.get(0).setFill(jeden);
        } else if((tmp%10)==2){
            score.get(0).setFill(dwa);
        } else if((tmp%10)==3){
            score.get(0).setFill(trzy);
        } else if((tmp%10)==4){
            score.get(0).setFill(cztery);
        } else if((tmp%10)==5){
            score.get(0).setFill(piec);
        } else if((tmp%10)==6){
            score.get(0).setFill(szesc);
        } else if((tmp%10)==7){
            score.get(0).setFill(siedem);
        } else if((tmp%10)==8){
            score.get(0).setFill(osiem);
        } else if((tmp%10)==9){
            score.get(0).setFill(dziewiec);
        } else if((tmp%10)==0){
            score.get(0).setFill(zero);
        }
    }

    public void startGame() throws Exception{
        //pierwszy EH obsluguje wcisniete klawisze, a drugi puszczone
        gameStage.addEventHandler(KeyEvent.KEY_PRESSED,stworzEH());
        gameStage.addEventHandler(KeyEvent.KEY_RELEASED,stworzEH2());

        gameStage.setTitle("SpaceInvaders");
        gameStage.setScene(scene);
        if(fullScreen){
            gameStage.setFullScreen(true);
        }
        gameStage.centerOnScreen();
        gameStage.setResizable(true);
        gameStage.show();
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

        moveEnemy =((WIDTH-2*korekta)/10.0)*0.02;
        increaseSpeed=((WIDTH-2*korekta)/10.0)*0.02;
        sizeE=40;
        life=3;
        pause=false;
        scoreI=0;
        scoreAkt();

        for(int i=0;i<life;i++){
            lives.get(i).setVisible(true);
        }
    }

    private EventHandler<KeyEvent> stworzEH(){
        eventHandler = new EventHandler<>() {
            @Override
            public void handle(KeyEvent wcisnieto) {
                if(wcisnieto.getCode()== KeyCode.ESCAPE){System.exit(0);}
                if(wcisnieto.getCode()== KeyCode.P){gamePause();}
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
        eventHandler2 = new EventHandler<>() {
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
        Ship y;
        long t;


        public Explosion(Enemy x, long t){this.x=x; this.t=t;}
        public Explosion(Ship y){this.y=y;}
        @Override
        public void run() {
            if(y==null) {
                switch (x.type) {
                    case 1:
                        x.setFill(explosionG);
                        break;
                    case 2:
                        x.setFill(explosionG);
                        break;
                    case 3:
                        x.setFill(explosionB);
                        break;
                    case 4:
                        x.setFill(explosionB);
                        break;
                    case 5:
                        x.setFill(explosionF);
                        break;
                    default:
                        x.setFill(explosion);
                        break;
                }
                try {
                    sleep(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    x.setVisible(false);
                }
            }
            else{
                try {
                    sleep(170);
                    y.setVisible(true);
                    sleep(150);
                    y.setVisible(false);
                    sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    y.setVisible(true);
                }
            }
        }
    }

    private void ruch(){
        //wykonanie ruchu statku
        if(left){gracz.moveA();}
        else if(right){gracz.moveD();}
        if(space&&canShot){gracz.shot(); canShot=false;}

        //jezeli wcisnieto wczesniej enter oraz nie ma juz pociskow i przeciwnikow na planszy to tworzeni sa nowi
        //if(nextWave){ if(bullets.isEmpty()){createEnemies(); nextWave=false;} }
        if(enemies.isEmpty()) {
            gameWin();
        }

        ArrayList<Bullet> removeB=new ArrayList<>();
        ArrayList<Bullet> removeBE=new ArrayList<>();
        ArrayList<Enemy> removeE=new ArrayList<>();
        //przemieszcza pociski statku
        for(Bullet i : bullets){
            if(i.moveUp()) {
                for (Enemy j : enemies) {
                    //jezeli jakis pocisk trafil przeciwnika
                    if (i.getBoundsInParent().intersects(j.getBoundsInParent())) {
                        sound1.play();
                        scoreI+=j.points;
                        scoreAkt();
                        //ukrywam pocisk i przeciwnika
                        new Explosion(j,(long) ((j.szerokoscE/moveEnemy)*5)).start();
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

        boolean tmp;
        for(Bullet i : bulletsEnemy){
            if(i.moveDown()) {
                tmp=true;
                for(Bullet j : bullets){
                    //pocisk trafil pocisk gracza
                    if (i.getBoundsInParent().intersects(j.getBoundsInParent())) {
                        i.setVisible(false);
                        j.setVisible(false);
                        removeBE.add(i);
                        removeB.add(j);
                        tmp=false;
                        break;
                    }
                }
                //pocisk nie trafil innego pocisku, wiec moze trafic gracza
                if(tmp) {
                    //jakis pocisk przeciwnika trafil statek
                    if (i.getBoundsInParent().intersects(gracz.getBoundsInParent())) {
                        gracz.setVisible(false);
                        i.setVisible(false);
                        //dodaje do listy elementow do usuniecia
                        removeBE.add(i);
                        life--;
                        lives.get(life).setVisible(false);
                        if(life==0){gameOver();}
                        else{
                            new Explosion(gracz).start();
                        }
                    }
                }
            }
            //pocisk przeciwnika wyszedl poza plansze
            else {
                i.setVisible(false);
                removeBE.add(i);
            }
        }
        bulletsEnemy.removeAll(removeBE);
        bullets.removeAll(removeB);

        //losowo wybierani sa przeciwnicy ktorzy w tym ruchu strzelaja
        for(Enemy i : enemies){
            if(random.nextInt(250)<1) {i.shot();}
            //sprawdzenie czy mozliwe jest dalsze poruszanie sie przeciwnikow w prawo
            if(moveR) {
                if (i.currentX + moveEnemy + ((WIDTH - 2 * korekta) / 10.0) * 0.6 > WIDTH - korekta) {
                    moveR = false;
                    moveS=true;
                }
            }
            //sprawdzenie czy mozliwe jest dalsze poruszanie sie przeciwnikow w lewo
            else{
                if(i.currentX-moveEnemy<korekta){
                    moveR =true;
                    moveS=true;
                }
            }
        }
        //co 10 trafionych wrogow, zwieksza sie szybkosc ich poruszania
        if(enemies.size()<=sizeE){
            sizeE-=10;
            moveEnemy+=increaseSpeed;
        }

        //przemieszczenie wszystkich wrogow w lewo lub w prawo
        moveEnemies();
    }

    private void moveEnemies(){
        if(moveR) {
            if(moveS) {
                for (Enemy i : enemies) {
                    i.moveD();
                    i.moveS();
                    if(i.currentY+(HEIGHT/10.0)*0.4>=HEIGHT-(HEIGHT/10.0)*0.8){gameOver();}
                }
            }
            else{
                for (Enemy i : enemies) {
                    i.moveD();
                }
            }
        }
        else {
            if(moveS) {
                for (Enemy i : enemies) {
                    i.moveA();
                    i.moveS();
                    if(i.currentY+(HEIGHT/10.0)*0.4>=HEIGHT-(HEIGHT/10.0)*0.8){gameOver();}
                }
            }
            else{
                for (Enemy i : enemies) {
                    i.moveA();
                }
            }
        }
        if(moveS){moveS=false;}
    }

    private void createEnemies(){
        //liczba rzedow przeciwnikow
        int k=5;
        //odstep w pionie
        int gap=(int) ((HEIGHT/10)*0.8);
        double start=(((WIDTH-2*korekta)/10)*0.2);
        double end=(((WIDTH-2*korekta)/10)*0.9);
        for(double j=gap;j<=k*gap;j=j+gap) {
            for (double i = start+korekta+end; i < WIDTH-korekta-end; i = i + end) {
                gamePane.getChildren().add(new Enemy(i, j, (int) j/gap));
            }
        }
    }

    private class Enemy extends Rectangle {
        double currentX;
        double currentY;
        int type;
        int points;
        double szerokoscE;
        double moveDown=((WIDTH-2*korekta)/10.0)*0.2;

        Enemy(double x, double y, int type){
            super(((WIDTH-2*korekta)/10.0)*0.6,(HEIGHT/10.0)*0.4);
            szerokoscE=((WIDTH-2*korekta)/10.0)*0.6;
            switch (type){
                case 1: pattern = enemy2g;
                        points=3;
                        break;
                case 2: pattern = enemy1g;
                        points=2;
                        break;
                case 3: pattern = enemy1b;
                        points=2;
                        break;
                case 4: pattern = enemy3b;
                        points=1;
                        break;
                case 5: pattern = enemy3f;
                        points=1;
                        break;
                default: pattern = enemy1b;
                        points=2;
                        break;
            }
            this.type=type;
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
            double correction=((WIDTH-2*korekta)/10.0)*0.28;
            bulletsEnemy.add(new Bullet(currentX+correction,currentY));
        }

        void moveD() {
            currentX += moveEnemy;
            setTranslateX(currentX);
        }
        void moveA(){
            currentX -= moveEnemy;
            setTranslateX(currentX);
        }
        void moveS(){
            currentY += moveDown;
            setTranslateY(currentY);
        }

    }

    private class Ship extends Rectangle{
        //obecna pozycja statku
        double currentX;
        //jednostaka o jaka sie przemieszcza
        double move;
        double szerokosc;

        Ship(){
            super(((WIDTH-2*korekta)/16.0),(HEIGHT/10.0)*0.4);
            szerokosc=((WIDTH-2*korekta)/16.0);
            move=szerokosc/8;
            pattern = ship3b;
            this.setFill(pattern);

            //ustala poczatkowa pozycje
            setPosition();
        }

        void moveD() {
            //(WIDTH/10.0)*0.6 to wartosc szerokosci prostokata podana w konstruktorze super
            if (currentX+ move+szerokosc<=WIDTH-korekta) {
                currentX += move;
                setTranslateX(currentX);
            }
        }
        void moveA(){
            if (currentX-move>=korekta) {
                currentX -= move;
                setTranslateX(currentX);
            }
        }
        void shot(){
            sound2.play();
            //x jest zwiekszony o correction aby strzaly wychodzily ze srodka wrogow
            double correction=((WIDTH-2*korekta)/10.0)*0.28;
            bullets.add(new Bullet(currentX+correction));
        }

        void setPosition(){
            setTranslateX(WIDTH/2-szerokosc/2);
            setTranslateY(HEIGHT-(HEIGHT/10.0)*0.8);
            currentX=WIDTH/2-szerokosc/2;
        }
    }

    private class Bullet extends Rectangle{
        //obecna wysokosc pocisku
        double currentY;
        //jednostaka o jaka sie przemieszcza
        double moveU=(HEIGHT/10.0)*0.3;
        double moveD=(HEIGHT/10.0)*0.1;

        Bullet(double x){
            super(((WIDTH-2*korekta)/10.0)*0.06,(HEIGHT/10.0)*0.2,Color.CHARTREUSE);
            //poczatkowa pozycja x jest taka jak statku ktory wystrzeliwuje
            setTranslateX(x);
            //poczatkowa wysokosc pocisku
            setTranslateY(HEIGHT-(HEIGHT/10.0)*0.2);
            currentY=0.9*HEIGHT;
            gamePane.getChildren().add(this);
        }
        Bullet(double x,double y){
            super(((WIDTH-2*korekta)/10.0)*0.06,(HEIGHT/10.0)*0.2,Color.WHITE);
            //poczatkowa pozycja x jest taka jak statku ktory wystrzeliwuje
            setTranslateX(x);
            setTranslateY(y);
            currentY=y;
            gamePane.getChildren().add(this);
        }
        //jak statek strzela do gory
        boolean moveUp(){
            if(currentY-moveU+(HEIGHT/10.0)*0.8>=0) {
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

    public void gameOver(){
        sound3.play();
        timeline.stop();
        returnMain.overStage.show();
    }

    public void gameWin(){
        soundWin.play();
        timeline.stop();
        returnMain.winStage.show();
    }

    @Override
    public void gamePause() {
        if(!pause){
            pause=true;
            timeline.stop();
        }
        else{
            pause=false;
            timeline.play();
        }
    }

    //@Override protected void finalize() throws Throwable{ System.out.println("Deleted SpaceInv"); }
}
