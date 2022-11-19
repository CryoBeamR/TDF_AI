import agent.MinMaxAgent;
import model.Board;
import model.Pocket;

import java.util.Scanner;

public class Game {
    static final int H_WON = 0;
    static final int C_WON = 1;

    public static void run(){
        int[][] genericBoard = {
                {1,2,3,4,5,6},
                {12,11,10,9,8,7},
                {13,14,15,16,17,18},
                {24,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,35,34,33,32,31}
        };
        Board board = new Board(genericBoard);
        int[] moveCard = board.shuffleBoardCards();
        Pocket pocket = new Pocket();
        startRounds(randStater(),board,moveCard,pocket);
        results(board,pocket);

    }

    public static void startRounds(boolean starter, Board board, int[] moveCard, Pocket pocket){
        MinMaxAgent minMaxAgent = new MinMaxAgent();
        int[][] moves = Board.moves(board.getBoard(),moveCard[0],moveCard[1]);
        if(!starter){
            System.out.println("Computer will start.");
            System.out.println(board.toString(pocket));
        }
        while(moves[11][0] != 0){
            if(starter) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("----- Human turn ;) ----- ");
                Scanner scn = new Scanner(System.in);
                String strBoard = board.toString(pocket);
                System.out.print(strBoard);
                System.out.println("\nChoose your move:");
                int decimalLetter = 65;
                int i = 0;
                for (int[] move : moves) {

                    if (move[0] == -1) {
                        break;
                    }
                    char letterMaj = (char) (decimalLetter + i++);
                    System.out.print(letterMaj + " : {" + move[0] + "," + move[1] + "}  ");
                }
                System.out.print("----> ");
                int chooseMove = scn.nextLine().trim().toUpperCase().charAt(0);
                int posMove = chooseMove - decimalLetter;
                Board.moveCard(moveCard[0], moveCard[1], moves[posMove][0], moves[posMove][1], board.getBoard(), pocket, false);
                moveCard[0] = moves[posMove][0];
                moveCard[1] = moves[posMove][1];
                String sBoard= board.toString(pocket);
                System.out.print(sBoard);
            }
            else{
                System.out.println("----- Computer turn :$ ----- ");
                int[] move = minMaxAgent.bestMove(moveCard[0],moveCard[1],pocket,board);
                Board.moveCard(moveCard[0],moveCard[1],move[0],move[1], board.getBoard(),pocket,true);
                moveCard[0] = move[0];
                moveCard[1] = move[1];
            }
            starter = !starter;
            moves = Board.moves(board.getBoard(),moveCard[0],moveCard[1]);
        }
    }

    public static boolean randStater(){
        double randDouble =  Math.random()*2 ;
        int randomInt = (int) randDouble;
        // false if it's human turn and true if it's computer turn
        return randomInt != 1;

    }
    public static int results(Board board, Pocket pocket){
        int shieldWon = 0;
        for(int[] house : pocket.getHand()){
            int shieldPos = house.length -1;

            if(house[shieldPos] == 1){
                shieldWon++;
            }
        }
        String sBoard = board.toString(pocket);
        System.out.print(sBoard);
        if(shieldWon >= 4){
            System.out.println(" Computer Won ... try harder next time HA HA HA ;)");
            return C_WON;
        }
        else{
            System.out.println(" YEaaaaH Human Won !!!!");
            return H_WON;
        }
    }
    public static void main(String[] arg){
        Game.run();
    }
}
