package model;

import java.util.Arrays;
import java.util.HashMap;

public class Board {
    // board of SIZE x SIZE
    public static final int SIZE = 6;
    private static final int EMPTY_SPACE = 0;
    private int[][] board;
    //exclude the moving card ( identified by -1 )
    private final static  int[] MAP_CARD_HOUSE = {2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,7,7,7,7,7,7,7,8,8,8,8,8,8,8,8};



    public Board(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() { return board; }

    /**
     * Deep copy the entire  board array
     * @return  a copy of the board array
     **/
    public int[][] deepCopyBoard(){
       return deepCopyBoard(board);
    }

    /**
     * Deep copy a board.
     *
     * @param board the board that will be deep copied
     * @return the new board
     */
    public static int[][] deepCopyBoard(int[][] board){
        int[][] boardCopy =  new int[SIZE][SIZE];
        int i = 0;
        for(int[] row : board){
            int [] rowCopy =  row.clone();
            boardCopy[i++] = rowCopy;
        }
        return boardCopy;
    }

    /**
     * Move the deplacement card (-1) from (init_x,init_y) to the position
     * (x,y).
     *
     * @param init_x initial point on x axe of movement card
     * @param init_y initial point on y axe of movement card
     * @param x point on x axe
     * @param y point on y axe
     * @param board the board that the action will be execute on
     * @param pocket the pocket that will be modify
     * @param updateHand if true will move card without updating the pocket
     * @return the board array with de deplacement made
     **/
    public static int[][] moveCard(int init_x, int init_y, int x, int y, int[][] board,
                            Pocket pocket, boolean updateHand)
    {
        board[init_y][init_x] = EMPTY_SPACE;
        int [] grabbedCard = grabCard(init_x,init_y,x,y,board);
        pocket.updateHand(grabbedCard, updateHand);
        board[y][x] = -1;
        return board;
    }
    /**
     * Grab all subsequent card of the same house in the same direction (x,y)
     * of the movement.
     *
     * @param init_x initial point on x axe of movement card
     * @param init_y initial point on y axe of movement card
     * @param x point on x axe
     * @param y point on y axe
     * @param board the board that the action will be executed on
     * @return int[] of grabbed cards
    **/
    public static int[] grabCard(int init_x, int init_y, int x, int y, int[][] board){
        int pos_x = init_x;
        int pos_y = init_y;
        int signe =  ((x - init_x) + (y - init_y))/Math.abs((x - init_x) + (y - init_y));
        int vect_x = init_x != x ? signe : 0;
        int vect_y = init_y != y ? signe : 0;
        int targetCard = board[y][x];
        int targetHouse = findHouse(targetCard);
        int[] grabbedCard = new int[SIZE+1];
        Arrays.fill(grabbedCard,-1);
        int grabCardCount = 0;
        while( pos_x != x || pos_y != y){
            pos_x += vect_x;
            pos_y += vect_y;
            int card = board[pos_y][pos_x];
            int cardHouse =  findHouse(card);
            if(targetHouse == cardHouse){
                board[pos_y][pos_x] = EMPTY_SPACE;
                grabbedCard[grabCardCount++] = card;
            }
        }
        return grabbedCard;
    }

    /**
     * Find the house of a card depending on it's value
     *
     * @param card the card number between 1 and 35
     * @return the house of the card
     */
    public static int findHouse(int card){
        if(card <= 0){
            // when card is not between 1 and 35 inclusively
            return -1;
        }
        // card identifier start at 1
        int card_pos = card-1;
        return MAP_CARD_HOUSE[card_pos];
    }
    /**
     * Find availaible move on axe x or y
     *
     * @param board the board that required the evaluation
     * @param x position on x axe of the moving card
     * @param y position on y axe of the moving card
     * @param axe 0 for x axe and 1 for y axe
     * @return a 2D array of all possible position on x axe
     */
    public static int[][] moves_axe(int[][] board, int x, int y,int axe){
        int[] foundHouses = {2,3,4,5,6,7,8};
        // last index is of array at [5][0] is the number of moves found
        int[][] moves = new int[SIZE][2];
        int moveFound = 0;
        int axeX = 0;
        int axeVal = axe ==  axeX ? x : y;
        for(int i = 0; i <= SIZE-1 - axeVal; i++){
            int card = axe == axeX ? board[y][(SIZE-1)-i] : board[(SIZE-1)-i][x];
            //skip places in board set to 0 and anything under
            if( card < 1){ continue; }
            int houseCard = findHouse(card);
            if( foundHouses[houseCard - 2] == houseCard){
                foundHouses[houseCard - 2] = 0;
                moves[moveFound][axe] = (SIZE-1)-i;
                moves[moveFound++][1 - axe] = axe ==  axeX ? y : x;
            }
        }
        foundHouses = new int[]{2, 3, 4, 5, 6, 7, 8};
        for(int i = 0; i < axeVal ; i++){
            int card = axe == axeX ? board[y][i] : board[i][x];
            int houseCard = findHouse(card);
            if( houseCard != -1 && foundHouses[houseCard - 2] == houseCard){
                foundHouses[houseCard - 2] = 0;
                moves[moveFound][axe] = i;
                moves[moveFound++][1 - axe] = axe ==  axeX ? y : x;
            }
        }
        moves[5][0] = moveFound;
        return moves;
    }


    /**
     * Find all the possible move by using moves_axe on 2D axes at a position
     * (x,y)
     * NOTE :
     *  moves[10][1] is 1 = shield and 0 = no shield
     *  moves[11][1] nb of moves in the array
     *
     * @param board the board that required the evaluation
     * @param x position on x axe of the moving card
     * @param y position on y axe of the moving card
     * @return  a 2D array of all possible position
     */
    public static int[][] moves(int[][] board, int x, int y){
        int axeX = 0;
        int axeY = 1;
        // the last index in move_x and move_y
        int lastMovePos = SIZE - 1;
        int[][] moves_x = moves_axe(board,x,y,axeX);
        int[][] moves_y = moves_axe(board,x,y,axeY);
        // 2 extra array space :
        // moves[10][1] is a separation space between moves and  nb of moves in array
        // moves[11][1] nb of moves in the array
        int[][] moves = new int[SIZE*2][2];
        System.arraycopy(moves_x,0,moves,0,moves_x[lastMovePos][0]);
        System.arraycopy(moves_y,0,moves,moves_x[lastMovePos][0],moves_y[lastMovePos][0]);
        int numberMoves = moves_x[lastMovePos][0] + moves_y[lastMovePos][0];
        //last position in the array is the number of moves
        moves[SIZE * 2 - 1][0] = numberMoves;
        for (int i = numberMoves; i <= (SIZE * 2 - 2); i++ ){
            // -1 at x position for empty moves space in the array
            moves[i][0] = -1;
        }
        return moves;
    }
    public int[] shuffleBoardCards(){
        return shuffleBoardCards(this.board);
    }
    public static int[] shuffleBoardCards(int[][] board){

        int[] moveCard = new int[2];
        for(int i = 0; i < 6; i++ ){
            for(int j = 0; j < 6; j++ ){
                double randDouble =  Math.random() * 5;
                int randIntX = (int) randDouble;
                randDouble =  Math.random() * 5;
                int randIntY = (int) randDouble;
                int temps = board[i][j];
                board[i][j] = board[randIntY][randIntX];
                board[randIntY][randIntX] = temps;
            }
        }
        for(int i = 0; i < 6; i++ ){
            for(int j = 0; j < 6; j++ ){
                if( board[i][j] == -1 ){
                    moveCard[0] = j;
                    moveCard[1] = i;
                    break;
                }

            }
        }
        return moveCard;
    }
    /**
     * Deduct the opposite player pocket hand base on the board and the know pocket hand*
     *
     * @param initiatingPocket the pocket with which the deduction will base on
     * @param board the board in the same state has the initiating Pocket
     * @return  the opposite pocket deduct from parameters
     */
    public static Pocket deductOppositePocket(Pocket initiatingPocket, Board board){
        int EMPTY = 0;
        HashMap cardAvailability = new HashMap<Integer,Boolean>();
        for (int[] row : board.getBoard()){
            for (int card : row) {
                if (card > EMPTY)
                    cardAvailability.put(card,true);
            }
        }
        Pocket oppositePocket = new Pocket();
        int[][] initiatingPocketHand = initiatingPocket.getHand();
        int visitedCard = 0;
        for (int i = 0 ; i < initiatingPocketHand.length; i++){
            int[] houseHand =  initiatingPocketHand[i];
            final int HOUSE_INDEX = 0;
            int HOUSE_SHIELD_INDEX = houseHand[HOUSE_INDEX] + 2;
            int NB_HOUSE_CARD_INDEX = houseHand[HOUSE_INDEX] + 1;
            int cardLimit =  houseHand[HOUSE_INDEX] + 1;
            for (int t = 1 ; t < cardLimit; t++) {
                ++visitedCard;
                int card = houseHand[t] ;
                boolean isCardAvailable = cardAvailability.get(visitedCard) != null && (boolean) cardAvailability.get(visitedCard);
                if (card == EMPTY && !isCardAvailable){
                    card = visitedCard;
                    int nbHouseCard = oppositePocket.getHand()[i][NB_HOUSE_CARD_INDEX];
                    oppositePocket.getHand()[i][t] = card;
                    oppositePocket.getHand()[i][NB_HOUSE_CARD_INDEX] = ++nbHouseCard;
                }
            }
            int[] oppositeHand = oppositePocket.getHand()[i];
            boolean initiativeHasMoreCard = houseHand[NB_HOUSE_CARD_INDEX] > oppositeHand[NB_HOUSE_CARD_INDEX];
            boolean initiativeHasShield = houseHand[HOUSE_SHIELD_INDEX] == 1;
            boolean initiativeAndOppositeHasZeroCard = houseHand[NB_HOUSE_CARD_INDEX] == 0  &&
                    (oppositeHand[NB_HOUSE_CARD_INDEX] == houseHand[NB_HOUSE_CARD_INDEX]);
            oppositeHand[HOUSE_SHIELD_INDEX] = initiativeHasMoreCard || initiativeHasShield
                    || initiativeAndOppositeHasZeroCard ? 0 : 1;
        }
        return oppositePocket;

    }

    public String toString(Pocket pocket){
        StringBuilder strBoard = new StringBuilder();
        Pocket oppositePocket = deductOppositePocket(pocket, this);
        buildHeader(strBoard);
        buildFirstLine(strBoard,pocket,oppositePocket);
        buildLines(strBoard,pocket,oppositePocket);
        return strBoard.toString();
    }
    private StringBuilder buildHeader(StringBuilder strBuilder){
        strBuilder.append("\n");
        strBuilder.append("+-------------------------------------------------------------------------------------------------------------------------------------+\n");
        strBuilder.append("|      Board                         Pocket Hand(C)               Shield(C)                    Pocket Hand(H)             Shield(H)   |\n");
        strBuilder.append("+-------------------------------------------------------------------------------------------------------------------------------------+\n");
        return strBuilder;
    }
    private StringBuilder buildFirstLine(StringBuilder strBuilder, Pocket pocket, Pocket oppositePocket){
        String space = "        ";
        String indentation = "                  ";
        final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
        final String ANSI_RESET = "\u001B[0m";
        strBuilder.append("   0  1  2  3  4  5  ").append(space).append(ANSI_WHITE_BACKGROUND).append("\033[1;90m").append(" 8 ").append(ANSI_RESET);
        buildFirstLinePocketShield(strBuilder, pocket);
        strBuilder.append(indentation);
        buildFirstLinePocketShield(strBuilder,oppositePocket);
        strBuilder.append("\n");
        return strBuilder;
    }
    private StringBuilder buildFirstLinePocketShield(StringBuilder strBuilder, Pocket pocket){
        String space = "         ";
        int unavailableCard = 0;
        int [][] hand =  pocket.getHand();
        strBuilder.append(" | ");
        for(int i = 1; i <= 8; i++){
            int card = hand[6][i];
            if(card > unavailableCard) {
                strBuilder.append(String.format("%-3.3s", hand[6][i]));
            }
            else{
                strBuilder.append(String.format("%-3.3s", "_"));
            }
        }
        strBuilder.append("|").append(space);
        if(hand[6][10] == 1)
            strBuilder.append("✓");
        else
            strBuilder.append("×");
        return strBuilder;
    }

    private StringBuilder buildLines(StringBuilder strBuilder, Pocket pocket, Pocket oppositePocket){
        final String ANSI_RESET = "\u001B[0m";
        String[] colorHousePktOrderBG = {"\033[0;100m","\033[41m","\033[40m","\033[43m","\033[42m","\033[41m"};
        String[] colorHousePktOrderF = {"\033[1;93m","\033[1;93m","\033[1;91m","\033[1;90m","\033[1;93m","\033[1;96m"};
        String space = "        ";
        String indentation = "                  ";
        for(int i = 0; i < 6; i++){
            buildBoardLine(strBuilder, i);
            strBuilder.append(" ").append(space).append(colorHousePktOrderBG[i]).append(colorHousePktOrderF[i]).append(" ").append(7 - i).append(" ").append(ANSI_RESET);
            buildLinePocketShield(strBuilder,pocket, i);
            strBuilder.append(indentation);
            buildLinePocketShield(strBuilder,oppositePocket, i);
            strBuilder.append("\n");
        }
        return strBuilder;
    }

    private StringBuilder buildBoardLine(StringBuilder strBuilder, int lineNumber){
        final String ANSI_RESET = "\u001B[0m";
        final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        String[] colorHouseBoardOrderUn = {"\033[41m","\033[42m","\033[43m","\033[40m","\033[41m","\033[1;100m","\033[47m"};
        String[] colorHouseBoardOrderF = {"\033[1;96m","\033[1;93m","\033[1;90m","\033[1;91m","\033[1;93m","\033[1;93m","\033[1;90m"};
        strBuilder.append(lineNumber+" ");
        for(int j = 0; j < 6; j++){
            int card = this.board[lineNumber][j];
            if(card == -1 || card == 0){
                if(card == -1) {
                    strBuilder.append(PURPLE_BACKGROUND_BRIGHT + BLACK_BOLD_BRIGHT).append(String.format("%-3.3s", this.board[lineNumber][j])).append(ANSI_RESET);
                }
                else{
                    strBuilder.append(String.format("%-3.3s", " "));
                }
            }
            else{
                int house =findHouse(card);
                strBuilder.append(colorHouseBoardOrderUn[house - 2]).append(colorHouseBoardOrderF[house - 2]).append(String.format("%-3.3s", this.board[lineNumber][j])).append(ANSI_RESET);
            }
        }
        return strBuilder;
    }
    private StringBuilder buildLinePocketShield(StringBuilder strBuilder, Pocket pocket, int lineNumber){
        int [][] hand =  pocket.getHand();
        strBuilder.append(" | ");
        for(int j = 1; j <= (7-lineNumber); j++){
            if(hand[5 - lineNumber][j] > 0){
                strBuilder.append(String.format("%-3.3s", hand[5 - lineNumber][j]));
            }
            else{
                strBuilder.append(String.format("%-3.3s", "_"));
            }
        }
        strBuilder.append("|");
        int c_space = 13+(lineNumber*3);
        if(hand[5-lineNumber][9-lineNumber] == 1)
            strBuilder.append(strBuilder.append(String.format("%" + c_space + ".3s", "✓")));
        else
            strBuilder.append(String.format("%" + c_space + ".3s", "×"));
        return strBuilder;
    }


}
