import agent.MinMaxAgent;
import model.Board;
import model.Pocket;

import java.util.Scanner;

public class Main {

    public static void main(String[] arg){
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
        MinMaxAgent minMaxAgent = new MinMaxAgent();
        Pocket pocket = new Pocket();

        double randDouble =  Math.random()*2 ;
        int randomInt = (int) randDouble;
        if(randomInt == 1){
            System.out.println("Computer will start.");
            System.out.println(board.toString(pocket));
        }
        int[][] moves = Board.moves(board.getBoard(),moveCard[0],moveCard[1]);
        // false if it's human turn and true if it's computer turn
        boolean turn = randomInt != 1;
        while(moves[11][0] != 0){
            if(turn) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("----- Human turn ;) ----- ");
                Scanner scn = new Scanner(System.in);  // Create a Scanner object
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
            turn = !turn;
            moves = Board.moves(board.getBoard(),moveCard[0],moveCard[1]);
        }
        int shieldWon = 0;
        for(int[] house : pocket.hand){
            int shieldPos = house.length -1;

            if(house[shieldPos] == 1){
                shieldWon++;
            }

        }
        if(shieldWon >= 4){
            String sBoard = board.toString(pocket);
            System.out.print(sBoard);
            System.out.println(" Computer Won ... try harder next time HA HA HA ;)");
        }
        else{
            String sBoard = board.toString(pocket);
            System.out.print(sBoard);
            System.out.println(" YEaaaaH Human Won !!!!");
        }


    }


}
