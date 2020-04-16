
public class Checkers extends Game{

    //konstruktor, tutaj tworzymy grę: jej stage(każdy obiekt gry ma swój), scene, i jakieś zmienne potrzebne
    Checkers(MainStage mS){
        returnMain = mS;

    }

    //odpalamy grę, pokazujemy stage i włączamy czas
    @Override
    public void startGame() throws Exception {

    }

    //tutaj resetujemy grę tak aby można było grać od nowa bez tworzenia gry na nowo
    @Override
    public void resetGame() {

    }

    //gdy przegramy wywołujemy tą metodę
    //w przypadku warcabów można ustalić, że będzie wywoływana zawsze jedna metoda
    // albo gameOver albo gameWin i w niej będzie pokazany wynik rozgrywki
    @Override
    public void gameOver() {

    }

    //wywołujemy gdy wygramy grę
    @Override
    public void gameWin() {

    }

    //wywołujemy gdy chcemy zapauzować grę
    @Override
    public void gamePause() {

    }
}
