package TroneDeFer;


import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] arg){
        int[][] randBoard = {
                {1,2,3,4,5,6},
                {12,11,10,9,8,7},
                {13,14,15,16,17,18},
                {24,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,35,34,33,32,31}
        };
        int[] moveCard = new int[2];
        for(int i = 0; i < 6; i++ ){
            for(int j = 0; j < 6; j++ ){
                double randDouble =  Math.random() * 5;
                int randIntX = (int) randDouble;
                randDouble =  Math.random() * 5;
                int randIntY = (int) randDouble;
                int temps = randBoard[i][j];
                randBoard[i][j] = randBoard[randIntY][randIntX];
                randBoard[randIntY][randIntX] = temps;

            }
        }
        for(int i = 0; i < 6; i++ ){
            for(int j = 0; j < 6; j++ ){
                if( randBoard[i][j] == -1 ){
                    moveCard[0] = j;
                    moveCard[1] = i;
                    break;
                }

            }
        }


        System.out.println(Arrays.deepToString(randBoard));
        System.out.println("Move : {"+ moveCard[0]+","+moveCard[1]+"}" );

        AI ai = new AI(randBoard);
        Pocket pocket = new Pocket();
        pocket.initiatePocketSpace();
        double randDouble =  Math.random()*2 ;
        int coinSide = (int) randDouble;
        if(coinSide == 1){
            System.out.println("Computer will start.");
        }
        int[][] moves = ai.board.moves(ai.board.getBoard(),moveCard[0],moveCard[1],1);
        // false if it's human turn and true if it's computer turn
        boolean turn = coinSide != 1;
        while(moves[11][0] != 0){
            if(turn) {
                System.out.println("----- Human turn ;) ----- ");
                Scanner scn = new Scanner(System.in);  // Create a Scanner object
                String SBoard = ai.board.toString(pocket);
                System.out.print(SBoard);
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
                ai.board.moveCard(moveCard[0], moveCard[1], moves[posMove][0], moves[posMove][1], ai.board.getBoard(), pocket, true);
                moveCard[0] = moves[posMove][0];
                moveCard[1] = moves[posMove][1];
                String board = ai.board.toString(pocket);
                System.out.print(board);
            }
            else{
                System.out.println("----- Computer turn :$ ----- ");
                int[] move = ai.bestMove(moveCard[0],moveCard[1],pocket);
                ai.board.moveCard(moveCard[0],moveCard[1],move[0],move[1],ai.board.getBoard(),pocket,false);
                moveCard[0] = move[0];
                moveCard[1] = move[1];
            }
            turn = !turn;
            moves = ai.board.moves(ai.board.getBoard(),moveCard[0],moveCard[1],1);
        }
        int shieldWon = 0;
        for(int[] house : pocket.hand){
            int shieldPos = house.length -1;

            if(house[shieldPos] == 1){
                shieldWon++;
            }

        }
        if(shieldWon >= 4){
            String board = ai.board.toString(pocket);
            System.out.print(board);
            System.out.println(" Computer Won ... try harder next time HA HA HA ;)");
        }
        else{
            String board = ai.board.toString(pocket);
            System.out.print(board);
            System.out.println(" YEaaaaH Human Won !!!!");
        }


    }


}
