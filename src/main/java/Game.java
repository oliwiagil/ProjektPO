import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class Game {

    enum DIFFICULTY{ EASY, NORMAL, HARD};

    public DIFFICULTY difficulty;
    public Stage gameStage;
    public double WIDTH;
    public double HEIGHT;
    protected int SPEED;
    protected Pane gamePane;
    protected Timeline timeline;
    protected MainStage returnMain;

    public abstract void startGame() throws Exception;
    public abstract void resetGame();
}
