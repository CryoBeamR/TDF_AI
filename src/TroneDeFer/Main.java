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
        int randIntX = 1;
        if(randIntX == 1){
            System.out.println("Computer will start.");
            int[] move = ai.bestMove(moveCard[0],moveCard[1]);
            ai.board.moveCard(moveCard[0],moveCard[1],move[0],move[1],ai.board.getBoard(),pocket,false);
        }
        String board = ai.board.toString(pocket);
        System.out.print(board);

    }


}
