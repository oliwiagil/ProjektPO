import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class Game {

    protected Stage primaryStage;
    protected double WIDTH;
    protected double HEIGHT;
    protected int SPEED;
    protected Pane gamePane;
    protected Timeline timeline;
    protected MainStage returnMain;

    public abstract void startGame() throws Exception;
    public abstract void resetGame();
}
