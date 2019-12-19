import java.io.IOException;
import java.util.Scanner;

public class Interface {
    public static void main(String[] args) throws IOException {
        //Create variables
        String move;
        String perft;
        String s;
        Scanner scan = new Scanner(System.in);

        int[][] board = new int[6][7];      //create board
        for (int z = 0; z < 6; z++) {       //z = height
            for (int i = 0; i < 7; i++) {   //i = row
                board[z][i] = 0;
            }
        }
                                                                //while the program is running
        while (true) {
            s = scan.nextLine();                                //read in next line
            if (s.contains("name")) {
                System.out.println("ShortCircuit-c3303493");    //print Name
            } else if (s.contains("isready")) {
                System.out.println("readyok");                  //Print Ready
            } else if (s.contains("position startpos")) {
                move = s.substring(s.length() - 1);
                if (!move.contains("s") && !move.contains(" ")){
                    updateBoard(move, board, 2);    //Take in opponent next move and update our board
                    }
                    if((checkWin(board,0) == 1) && (checkWin(board,1) == 1)){
                	System.out.println("SOMEONE HAS WON!");
                	}
                    printBoard(board);                              //Print the board
                    minimaxStart(board, 7);                  //<------------------DEPTH HERE Calculate and print next move
                	if((checkWin(board,0) == 1) && (checkWin(board,1) == 1)){
                	System.out.println("SOMEONE HAS WON!");
                	}
                	printBoard(board);                              //Print the board
            } else if (s.contains("go")) {
                minimaxStart(board, 7);                  //<------------------DEPTH HERE Calculate and print next move
                printBoard(board);                              //Print the board
            } else if (s.contains("perft")) {
                perft = s.substring(s.length() -1);             //reply with number of nodes in board
                int depth = Integer.parseInt(perft);
                perft(board,depth);
            } else if (s.contains("quit")) {
                System.out.println("quitting");                 //quit the system
                System.exit(0);
            } else if (s.contains("print")) {
                printBoard(board);                              //Print the board
            }
        }
    }

    static void perft(int[][] board, int depth){                //Calculates how many possible nodes are in the current board calculated to given depth
        int value = 0;
        int[][] tempBoard = cloneArray(board);                  //Create new board and send it to the miniMax Tally
        value = miniMaxTally(tempBoard, depth);                 //get node count of given board
        if (value != 1){
            value++;
        }
        System.out.println("perft " + depth + " " + value);     //Print the depth and returned node count
    }

   static void minimaxStart(int[][] board, int depth){          //Creates every possible move in board and returns minimax value for all moves
       int[][] tempBoard;
       int bestIndex = 0;
       int bestScore;
       bestScore = Integer.MIN_VALUE;

       for (int row = 0; row < 7; row++) {
           tempBoard = cloneArray(board);                       //Create new copy of the board
           if (board[0][row] == 2 || board[0][row] == 1){       //If the current coloumn is full, skip making move in this coloumn
               continue;
           }
           updateBoard(Integer.toString(row), tempBoard, 1);                //make new move in the temp board
           int currentScore = miniMax(tempBoard, depth, false);        //get score of the new board

           if (currentScore > bestScore) {                                              //if this board score is greater than the last, update score
               bestScore = currentScore;
               bestIndex = row;
           }
       }
        updateBoard(Integer.toString(bestIndex), board, 1);                                         //update actual board
        System.out.println("bestmove " + Integer.toString(bestIndex) + " " + Integer.toString(bestScore));      //print results
        if((checkWin(board,0) == 1) && (checkWin(board,1) == 1)){
                	System.out.println("SOMEONE HAS WON!");
                	}
    }

    static int miniMax(int[][] board,int depth,boolean maximisingPlayer){                       //returns the value of every board possible to the given depth
        int[][] tempBoard;
        if (depth == 0){
            return(evaluateFunction(board));                                                    //calculate the score of the current board and return score
        }
        if (maximisingPlayer){                                                                  //if player is maximising, create new board with every move possible for me and find score of the board with the next depth
            int value = Integer.MIN_VALUE;
            for (int i = 0; i < 7; i++){
                tempBoard = cloneArray(board);
                updateBoard(Integer.toString(i), tempBoard, 1);
                if((checkWin(tempBoard,0) == 1) && (checkWin(tempBoard,1) == 1)){
                	continue;
                }
                value = Math.max(value, miniMax(tempBoard, depth - 1, false));
            }
            return value;
        }
        else {
            int value = Integer.MAX_VALUE;                                                      //if player is minimising, create new board with every move possible for them and find score of the board with the next depth
            for (int i = 0; i < 7; i++){
                tempBoard = cloneArray(board);
                updateBoard(Integer.toString(i), tempBoard, 2);
                if((checkWin(tempBoard,0) == 1) && (checkWin(tempBoard,1) == 1)){
                	continue;
                }
                value = Math.min(value,miniMax(tempBoard,depth -1,true));
            }
            return value;
        }
    }

    static int evaluateFunction(int[][] board){
        //boards current score
        int currentScore = 1;
        //universal scores for connecting and blocking
        int connectTwo = 10;
        int connectThree = 50;
        int connectFour = 1000;
        int middleMove = 90;
        int blockThree = 50;
        int blockFour = 500;

        //for each position in the given board
        for (int z = 0; z < 6; z++) {
            for (int i = 0; i < 7; i++) {
                    //if position belongs to me look add to my overall score for each connection and block and middle move
                    if (board[z][i] == 1) {

                        //If token is in middle row +middleMove
                        if (i == 3) {
                            currentScore += middleMove;
                        }

                        //If table contains a connectTwo,three or four upwards forwards diagonally /
                        if (z != 0 && i != 6) {
                            if (board[z - 1][i + 1] == 1) {
                                //System.out.println("Two Hit");
                                currentScore += connectTwo;
                                if (z != 1 && i != 5) {
                                    if (board[z - 2][i + 2] == 1) {
                                        currentScore += connectThree;
                                        if (z != 2 && i != 4) {
                                            if (board[z - 3][i + 3] == 1) {
                                                currentScore += connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains a connectTwo,three or four upwards backwards diagonally \
                        if (z != 0 && i != 0) {
                            if (board[z - 1][i - 1] == 1) {
                                currentScore += connectTwo;
                                if (z != 1 && i != 1) {
                                    if (board[z - 2][i - 1] == 1) {
                                        currentScore += connectThree;
                                        if (z != 2 && i != 2) {
                                            if (board[z - 3][i - 1] == 1) {
                                                currentScore += connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains a connectTwo,three or four downwards vertically |
                        if (z != 5){
                            if(board[z+1][i] == 1){
                                currentScore += connectTwo;
                                if (z != 4){
                                    if(board[z+2][i] == 1){
                                        currentScore += connectThree;
                                        if (z != 3){
                                            if(board[z+2][i] == 1){
                                                currentScore += connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains a connectTwo,three or four horizontally to the right -->
                        if(i != 6){
                            if(board[z][i+1] == 1){
                                currentScore += connectTwo;
                                if(i != 5){
                                    if(board[z][i+2] == 1){
                                        currentScore += connectThree;
                                        if(i != 4){
                                            if(board[z][i+3] == 1){
                                                currentScore += connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains a connectTwo,three or four horizontally to the left <--
                        if(i != 0){
                            if(board[z][i-1] == 1){
                                currentScore += connectTwo;
                                if(i != 1){
                                    if(board[z][i-2] == 1){
                                        currentScore += connectThree;
                                        if(i != 2){
                                            if(board[z][i-3] == 1){
                                                currentScore += connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //if table can block a connect three or four horizontally to the right -->
                        if (i != 6 && i != 5) {
                            if (board[z][i+1] == 2 && board[z][i+2] == 2){
                                currentScore += blockThree;
                                if (i != 4){
                                    if (board[z][i+3] == 2){
                                        currentScore += blockFour;
                                    }
                                }
                            }
                        }

                        //if table can block a connect three or four horizontally to the left <--x
                        if (i != 0 && i != 1) {
                            if (board[z][i-1] == 2 && board[z][i-2] == 2){
                                currentScore += blockThree;
                                if (i != 2){
                                    if (board[z][i-3] == 2){
                                        currentScore += blockFour;
                                    }
                                }
                            }
                        }

                        //if table can block a connect three or four vertically down |
                        if (z != 5 && z != 4){
                            if (board[z+1][i] == 2 && board[z+2][i] == 2){
                                currentScore += blockThree;
                                if (z != 3){
                                    if (board[z+3][i] == 2){
                                        currentScore += blockFour;
                                    }
                                }
                            }
                        }

                        //if table can block a connect three or four diagonally up forwards /
                        if(z != 0 && z != 1 && i != 6 && i != 5){
                            if (board[z-1][i+1] == 2 && board[z-2][i+2] == 2) {
                                currentScore += blockThree;
                                if (z != 2 && i != 4) {
                                    if (board[z - 3][i + 3] == 2) {
                                        currentScore += blockFour;
                                    }
                                }
                            }
                        }

                        //if table can block a connect three or four diagonally up backwards \
                        if(z != 0 && z != 1 && i != 0 && i != 1){
                            if (board[z-1][i-1] == 2 && board[z-2][i-2] == 2) {
                                currentScore += blockThree;
                                if (z != 2 && i != 2) {
                                    if (board[z - 3][i - 3] == 2) {
                                        currentScore += blockFour;
                                    }
                                }
                            }
                        }
                        //if position belongs to them subtract to my overall score for each connection and block and middle move
                    } else if (board[z][i] == 2){
                        //If middle row contains an opponent token -middleMove
                        if (i == 3) {
                            currentScore -= middleMove;
                        }

                        //If table contains an Opp connectTwo,three or four upwards forwards diagonally /
                        if (z != 0 && i != 6) {
                            if (board[z - 1][i + 1] == 2) {
                                currentScore -= connectTwo;
                                if (z != 1 && i != 5) {
                                    if (board[z - 2][i + 2] == 2) {
                                        currentScore -= connectThree;
                                        if (z != 2 && i != 4) {
                                            if (board[z - 3][i + 3] == 2) {
                                                currentScore -= connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains an Opp connectTwo,three or four upwards backwards diagonally \
                        if (z != 0 && i != 0) {
                            if (board[z - 1][i - 1] == 2) {
                                currentScore -= connectTwo;
                                if (z != 1 && i != 1) {
                                    if (board[z - 2][i - 1] == 2) {
                                        currentScore -= connectThree;
                                        if (z != 2 && i != 2) {
                                            if (board[z - 3][i - 1] == 2) {
                                                currentScore -= connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains an Opp a connectTwo,three or four downwards vertically |
                        if (z != 5){
                            if(board[z+1][i] == 2){
                                currentScore -= connectTwo;
                                if (z != 4){
                                    if(board[z+2][i] == 2){
                                        currentScore -= connectThree;
                                        if (z != 3){
                                            if(board[z+2][i] == 2){
                                                currentScore -= connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains an Opp connectTwo,three or four horizontally to the right -->
                        if(i != 6){
                            if(board[z][i+1] == 2){
                                currentScore -= connectTwo;
                                if(i != 5){
                                    if(board[z][i+2] == 2){
                                        currentScore -= connectThree;
                                        if(i != 4){
                                            if(board[z][i+3] == 2){
                                                currentScore -= connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //If table contains an Opp connectTwo,three or four horizontally to the left <--
                        if(i != 0){
                            if(board[z][i-1] == 2){
                                currentScore -= connectTwo;
                                if(i != 1){
                                    if(board[z][i-2] == 2){
                                        currentScore -= connectThree;
                                        if(i != 2){
                                            if(board[z][i-3] == 2){
                                                currentScore -= connectFour;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //if opponent blocks connect three or four horizontally to the right -->
                        if (i != 6 && i != 5) {
                            if (board[z][i+1] == 1 && board[z][i+2] == 1){
                                currentScore -= blockThree;
                                if (i != 4){
                                    if (board[z][i+3] == 1){
                                        currentScore -= blockFour;
                                    }
                                }
                            }
                        }

                        //if opponent blocks connect three or four horizontally to the left <--x
                        if (i != 0 && i != 1) {
                            if (board[z][i-1] == 1 && board[z][i-2] == 1){
                                currentScore -= blockThree;
                                if (i != 2){
                                    if (board[z][i-3] == 1){
                                        currentScore -= blockFour;
                                    }
                                }
                            }
                        }

                        //if opponent blocks connect three or four vertically down |
                        if (z != 5 && z != 4){
                            if (board[z+1][i] == 1 && board[z+2][i] == 1){
                                currentScore -= blockThree;
                                if (z != 3){
                                    if (board[z+3][i] == 1){
                                        currentScore -= blockFour;
                                    }
                                }
                            }
                        }

                        //if opponent blocks connect three or four diagonally up forwards /
                        if(z != 0 && z != 1 && i != 6 && i != 5){
                            if (board[z-1][i+1] == 1 && board[z-2][i+2] == 1) {
                                currentScore -= blockThree;
                                if (z != 2 && i != 4) {
                                    if (board[z - 3][i + 3] == 1) {
                                        currentScore -= blockFour;
                                    }
                                }
                            }
                        }

                            //if opponent blocks connect three or four diagonally up backwards \
                            if(z != 0 && z != 1 && i != 0 && i != 1){
                                if (board[z-1][i-1] == 1 && board[z-2][i-2] == 1) {
                                    currentScore -= blockThree;
                                    if (z != 2 && i != 2) {
                                        if (board[z - 3][i - 3] == 1) {
                                            currentScore -= blockFour;
                                        }
                                    }
                                }
                            }

                    }
                }
            }

        return(currentScore);
    }

    static void printBoard(int board[][]) {                     //print board out with formatting, 0 = no ones move, 1 = me and 2 = opponent
        for (int i = 0; i < board.length; ++i) {
            System.out.print("| ");
            for (int j = 0; j < board[i].length - 1; ++j) {
                if (board[i][j] == 1) {
                    System.out.print("X | ");
                } else if (board[i][j] == 2) {
                    System.out.print("O | ");
                } else {
                    System.out.print("  | ");
                }
            }

            if (board[i][board[i].length - 1] == 1) {
                System.out.println("X | ");
            } else if (board[i][board[i].length - 1] == 2) {
                System.out.println("O | ");
            } else {
                System.out.println("  | ");
            }
        }
    }

    static void updateBoard(String move, int[][] board, int playersTurn) {              //updates the given board, with the new move, for the given player
        int moveInt = Integer.parseInt(move);                                           //get column of the new move
        for (int i = board.length - 1; i >= 0; --i) {                                   //start at bottom slot, for each slot check to see if slot is empty, once found a slot break.
            if (board[i][moveInt] == 0) {
                board[i][moveInt] = playersTurn;
                break;
            }
        }
    }

    public static int[][] cloneArray(int[][] src) {                                      //takes an array and gives back a copy of the given array
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }

    static int miniMaxTally(int[][] board,int depth){
        int[][] tempBoard;
        int count = 0;
        if (depth == 0){                                                    //if depth = 0, return 1
            return(0);
        } 

        for (int i = 0; i < 7; i++){                                        //for each board, whilst its not at depth 0, get more children
            tempBoard = cloneArray(board);
            if (board[0][i] == 2 || board[0][i] == 1){                      //if slot is full do not put a coin in this slot
                continue;
                }
            updateBoard(Integer.toString(i), tempBoard, 1);
            if(!(checkWin(tempBoard,0) == 1) && !(checkWin(tempBoard,1) == 1)){        //if board contains a win or loss do not continue looking
                count += miniMaxTally(tempBoard, depth - 1);
                count++;

                }
            }
        return count;
    }


    static int checkWin(int[][] board, int playersTurn) {                       //check for win in every direction from each position, if a win is found return 1, if no win found 0
        int i;
        int j;
        for(i = 0; i < board.length; ++i) {
            for(j = 0; j < board[i].length - 3; ++j) {
                if (board[i][j] == playersTurn + 1 && board[i][j + 1] == playersTurn + 1 && board[i][j + 2] == playersTurn + 1 && board[i][j + 3] == playersTurn + 1) {
                    return 1;
                }
            }
        }

        for(i = 0; i < board.length - 3; ++i) {
            for(j = 0; j < board[i].length; ++j) {
                if (board[i][j] == playersTurn + 1 && board[i + 1][j] == playersTurn + 1 && board[i + 2][j] == playersTurn + 1 && board[i + 3][j] == playersTurn + 1) {
                    return 1;
                }
            }
        }

        for(i = 3; i < board.length; ++i) {
            for(j = 0; j < board[i].length - 3; ++j) {
                if (board[i][j] == playersTurn + 1 && board[i - 1][j + 1] == playersTurn + 1 && board[i - 2][j + 2] == playersTurn + 1 && board[i - 3][j + 3] == playersTurn + 1) {
                    return 1;
                }
            }
        }

        for(i = 3; i < board.length; ++i) {
            for(j = 3; j < board[i].length; ++j) {
                if (board[i][j] == playersTurn + 1 && board[i - 1][j - 1] == playersTurn + 1 && board[i - 2][j - 2] == playersTurn + 1 && board[i - 3][j - 3] == playersTurn + 1) {
                    return 1;
                }
            }
        }

        return 0;
    }
}
