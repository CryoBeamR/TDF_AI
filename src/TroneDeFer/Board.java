package TroneDeFer;

public class Board {
    // board of n x n size
    public final int SIZE = 6;
    private int[][] board = new int[SIZE][SIZE];
    private int companions = 6;


    public int[][] deepCopyBoard(){
        int[][] boardCopy =  new int[SIZE][SIZE];
        int i = 0;
        for(int[] row : board){
            int [] rowCopy =  row.clone();
            boardCopy[i++] = rowCopy;
        }
        return boardCopy;
    }

    public void setBoard(int[][] board){
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
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
     **/
    public int[][] moveCard(int init_x, int init_y, int x, int y, int[][] board,
                            Pocket pocket, boolean oponentTurn)
    {
        board[init_y][init_x] = 0;
        grabCard(init_x,init_y,x,y,board,pocket,oponentTurn);
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
     * @param board the board that the action will be execute on
    **/
    public int[][] grabCard(int init_x, int init_y, int x, int y, int[][] board,
                            Pocket pocket, boolean oponentTurn){
        int pos_x = init_x;
        int pos_y = init_y;
        int signe =  ((x - init_x) + (y - init_y))/Math.abs((x - init_x) + (y - init_y));
        int vect_x = init_x != x ? signe : 0;
        int vect_y = init_y != y ? signe : 0;
        final int MAXHOUSE = 8;
        final int MAXCARD = 35;
        int targetCard = board[y][x];
        int targetHouse = findHouse(targetCard,MAXHOUSE,MAXCARD);
        final int OFFSETS = 2;
        while( pos_x != x || pos_y != y){
            pos_x += vect_x;
            pos_y += vect_y;
            //to remove !! was trying to catch a bug
            if(pos_x == -1 || pos_y == -1){
                break;
            }
            int card = board[pos_y][pos_x];
            int house =  findHouse(card,MAXHOUSE,MAXCARD);
            if(targetHouse == house && !oponentTurn){
                // add to pocket if it's the AI turn
                int cardPocketIndex =  findCardPosPocket(card,MAXHOUSE,MAXCARD);
                //to remove !! was trying to catch a bug
                if(house-OFFSETS == -1 || cardPocketIndex+1 == -1){
                    break;
                }
                pocket.hand[house-OFFSETS][cardPocketIndex+1] = card;
                pocket.hand[house-OFFSETS][house+1] +=1;
                board[pos_y][pos_x]= 0;
                pocket.availableCard[house-OFFSETS] -= 1;
                if (pocket.availableCard[house-OFFSETS] == 0 && companions != 0){
                    pocket.availableCard[house-OFFSETS] = -1;
                    companions -= 1;
                }
                int cardAvailability = pocket.availableCard[house-OFFSETS] == -1 ? 0 : pocket.availableCard[house-OFFSETS];
                if(pocket.hand[house-OFFSETS][house+1] >= (house - cardAvailability - pocket.hand[house-OFFSETS][house+1]) ) {
                    pocket.hand[house-OFFSETS][house+2] = 1;
                }
            }
            else if(targetHouse == house){
                board[pos_y][pos_x]= 0;
                pocket.availableCard[house-OFFSETS] -= 1;
                int cardAvailability = pocket.availableCard[house-OFFSETS] == -1 ? 0 : pocket.availableCard[house-OFFSETS];
                if(pocket.hand[house-OFFSETS][house+1] <= (house - cardAvailability - pocket.hand[house-OFFSETS][house+1])) {
                    pocket.hand[house-OFFSETS][house+2] = 0;
                }
            }
        }
        return board;
    }

    /**
     * Recursively find the house of a card depending of it's value
     *
     * @param card the card number between 1 and 35
     * @param houseSize the number of the biggest house
     * @param cardSet the biggest card value
     * @return the house of the card
     */
    public int findHouse(int card, int houseSize, int cardSet){
        int house = 1;
        if(card <= 0) {
            return house;
        }
        if( card <= (cardSet - houseSize)){
            int newCardSet = cardSet - houseSize;
            house = findHouse(card,houseSize-1,newCardSet);
        }
        else{
            house = houseSize;
        }
        return house;
    }

    /**
     * Recursively find the position of a card in the pocket depending on
     * the value of the card
     *
     * @param card the card number between 1 and 35
     * @param houseSize the number of the biggest house
     * @param cardSet the biggest card value
     * @return the position of the card in the pocket
     */
    public int findCardPosPocket(int card, int houseSize, int cardSet){
        int position = 0;
        int newCardSet = 0;
        if( card <= (cardSet - houseSize)){
            newCardSet = cardSet - houseSize;
            position = findCardPosPocket(card,houseSize-1,newCardSet);
        }
        else{
            newCardSet = cardSet - houseSize;
            position = card - newCardSet - 1 ;
        }
        return position;
    }

    /**
     * Find availaible move on axe x
     *
     * @param board the board that requied the evaluation
     * @param x position on x axe of the moving card
     * @param y position on y axe of the moving card
     * @param direction the direction the function will go throught the bord
     * (+1) and (-1)
     * @return a 2D array of all possible position on x axe
     */
    public int[][] moves_x(int[][] board, int x, int y, int direction){
        int[] foundHouses_x = {2,3,4,5,6,7,8};
        // last index is of array at [5][0] is the number of moves found
        int[][] moves = new int[6][2];
        int moveFound = 0;
        int start = direction > 0 ? 0 : 5;
        int next = start;
        while(x != next && next >= 0 && next <= 5 ){
            int card_x = board[y][next];
            int house_x = findHouse(card_x,8,35);
            if ( house_x != 1 && foundHouses_x[house_x - 2] == house_x) {
                foundHouses_x[house_x - 2] = 0;
                moves[moveFound][0] = next;
                moves[moveFound++][1] = y;
                moves[5][0] = moveFound;
            }
            next =  next + (direction);
        }
        if((x == start || (x == next && x > 0 && x < 5)) && direction != -1) {
            int[][] restMove = moves_x(board,x,y,direction * (-1));
            System.arraycopy(restMove,0,moves,moveFound,restMove[5][0]);
            moves[5][0] += restMove[5][0];
            return moves;
        }
        return moves;
    }

    /**
     * Find availaible move on axe y
     *
     * @param board the board that requied the evaluation
     * @param x position on x axe of the moving card
     * @param y position on y axe of the moving card
     * @param direction the direction the function will go throught the bord
     * (+1) and (-1)
     * @return a 2D array of all possible position on y axe
     */
    public int[][] moves_y(int[][] board, int x, int y, int direction){
        int[] foundHouses_y = {2,3,4,5,6,7,8};
        // last index is of array at [5][0] is the number of moves found
        int[][] moves = new int[6][2];
        int moveFound = 0;
        int start = direction > 0 ? 0 : 5;
        int next = start;
        while(y != next && next >= 0 && next <= 5){
            int card_y = board[next][x];
            int house_y = findHouse(card_y,8,35);
            if (house_y != 1 && foundHouses_y[house_y - 2] == house_y) {
                foundHouses_y[house_y - 2] = 0;
                moves[moveFound][0] = x;
                moves[moveFound++][1] = next;
                moves[5][0] = moveFound;
            }
            next =  next + (direction);
        }
        if(y == start || (y == next && y > 0 && y < 5) && direction != -1) {
            int[][] restMove = moves_y(board,x,y,direction * (-1));
            System.arraycopy(restMove,0,moves,moveFound,restMove[5][0]);
            moves[5][0] += restMove[5][0];
            return moves;
        }

        return moves;

    }

    /**
     * Find all the possible move by using moves_x and moves_y at a position
     * (x,y)
     *
     * @param board the board that requied the evaluation
     * @param x position on x axe of the moving card
     * @param y position on y axe of the moving card
     * @return  a 2D array of all possible position
     */
    public int[][] moves(int[][] board, int x, int y,int direction){
        int[][] moves_x = moves_x(board,x,y,direction);
        int[][] moves_y = moves_y(board,x,y,direction);
        // moves[10][1] is 1 = shield and 0 = no shield
        // moves[11][1] nb of moves in the array
        int[][] moves = new int[12][2];
        System.arraycopy(moves_x,0,moves,0,moves_x[5][0]);
        System.arraycopy(moves_y,0,moves,moves_x[5][0],moves_y[5][0]);
        int numberMoves = moves_x[5][0] + moves_y[5][0];
        moves[11][0] = numberMoves;
        for (int i = numberMoves; i < 10; i++ ){
            // -1 at x position for empty moves space in the array
            moves[i][0] = -1;
        }
        return moves;
    }

    /**
     * Deep copy a board.
     *
     * @param board the board that will be deep copied
     * @return the new board
     */
    public int[][] deepCopyBoard(int[][] board){
        int[][] boardCopy =  new int[SIZE][SIZE];
        int i = 0;
        for(int[] row : board){
            int [] rowCopy =  row.clone();
            boardCopy[i++] = rowCopy;
        }
        return boardCopy;
    }
    public String toString(Pocket pocket){
        int[][] hand = pocket.hand;
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
        final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
        final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
        String[] colorHousePktOrderBG = {"\033[0;100m","\033[41m","\033[40m","\033[43m","\033[42m","\033[41m"};
        String[] colorHousePktOrderF = {"\033[1;93m","\033[1;93m","\033[1;91m","\033[1;90m","\033[1;93m","\033[1;96m"};
        String[] colorHouseBoardOrderUn = {"\033[41m","\033[42m","\033[43m","\033[40m","\033[41m","\033[1;100m","\033[47m"};
        String[] colorHouseBoardOrderF = {"\033[1;96m","\033[1;93m","\033[1;90m","\033[1;91m","\033[1;93m","\033[1;93m","\033[1;90m"};
        String space = "        ";
        StringBuilder board = new StringBuilder("+---------------------------------------------------------------------------+\n");
        board.append("|      Board                         Pocket Hand                  Shield    |\n");
        board.append("+---------------------------------------------------------------------------+\n");
        board.append("   0  1  2  3  4  5  ").append(space).append(ANSI_WHITE_BACKGROUND).append("\033[1;90m").append(" 8 ").append(ANSI_RESET).append(" | ");

        for(int i = 1; i <= 8; i++){
            if(hand[6][i] > 0){
                board.append(String.format("%-3.3s", hand[6][i]));
            }
            else{
                board.append(String.format("%-3.3s", "_"));
            }
        }
        board.append("|").append(space);
        if(hand[6][10] == 1)
            board.append("✓\n");
        else
            board.append("×\n");
        for(int i = 0; i < 6; i++){
            board.append(i+" ");
            for(int j = 0; j < 6; j++){
                int card = this.board[i][j];
                if(card == -1 || card == 0){
                    if(card == -1) {
                        board.append(PURPLE_BACKGROUND_BRIGHT + BLACK_BOLD_BRIGHT).append(String.format("%-3.3s", this.board[i][j])).append(ANSI_RESET);
                    }
                    else{
                        board.append(String.format("%-3.3s", " "));
                    }
                }
                else{
                    int house =findHouse(card,8,35);
                    board.append(colorHouseBoardOrderUn[house - 2]).append(colorHouseBoardOrderF[house - 2]).append(String.format("%-3.3s", this.board[i][j])).append(ANSI_RESET);
                }
            }
            board.append(" ").append(space).append(colorHousePktOrderBG[i]).append(colorHousePktOrderF[i]).append(" ").append(7 - i).append(" ").append(ANSI_RESET).append(" | ");
            for(int j = 1; j <= (7-i); j++){
                if(hand[5 - i][j] > 0){
                    board.append(String.format("%-3.3s", hand[5 - i][j]));
                }
                else{
                    board.append(String.format("%-3.3s", "_"));
                }
            }
            board.append("|");
            int c_space = 13+(i*3);
            if(hand[5-i][9-i] == 1)
                board.append(board.append(String.format("%" + c_space + ".3s", "✓\n")));
            else
                board.append(String.format("%" + c_space + ".3s", "×\n"));;
        }



        return board.toString();
    }

}
