public class Score {
    int move;
    int score;

    public Score(){
        move = 0;
        score = 0;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getMove() {
        return move;
    }
}
