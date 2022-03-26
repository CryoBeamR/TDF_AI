public class AI {
    Board board;
    long start;
    long finish ;
    long timeElapsed ;
    final long MAXTIME = 459999999000l;
    int depth = 9;

    public AI(int[][] board) {
        this.board = new Board();
        this.board.setBoard(board);
    }

    /**
     * Find the best move depending of the heuristic of every board position
     * it encounter.
     *
     * @param x the initial position of the moving card on axe x
     * @param y the initial position of the moving card on axe y
     * @return coordinate {x,y} of the best move calculated
     */
    public int[] bestMove(int x, int y, Pocket pocket){
        double maxScore = -10000;
        start = System.nanoTime();
        int[][] moves = this.board.moves(board.deepCopyBoard(),x,y);
        int[] bestMove = {moves[0][0],moves[0][1]};
        for(int[] move : moves ){
            Pocket tempPocket = new Pocket(pocket.hand ,pocket.nbAvailableCard);
            if (move[0] == -1){
                break;
            }
            int [][] board = this.board.moveCard(x,y,move[0],move[1],this.board.deepCopyBoard(),tempPocket,false,false);
            double score = minMax(board,pocket.hand,pocket.nbAvailableCard,depth,-10000,10000,false,move[0],move[1], 1);
            if (score > maxScore){
                maxScore = score;
                bestMove[0] = move[0];
                bestMove[1] = move[1];
            }

            //debug
            System.out.print("Move:" + "["+move[0]+","+move[1]+"]"+" --> "+ score +", ");
            System.out.println();
            if(timeElapsed > MAXTIME){
                System.out.println( depth + "<--- depth reached");
                System.out.println( "Move : {"+ bestMove[0]+","+bestMove[1]+"}");
                return bestMove;
            }
        }
        System.out.println( "Best Move : {"+ bestMove[0]+","+bestMove[1]+"}");
        return bestMove;
    }

    public double minMax(int[][] board,int[][] pocket,int[] availableCard, int depth , double alpha, double beta, boolean isMax, int x, int y, int currentDepth) {
        double maxEval = -10000;
        double minEval = 10000;
        finish = System.nanoTime();
        timeElapsed = finish - start;
        if (timeElapsed > MAXTIME) {
            if (isMax) {
                return minEval;
            } else {
                return maxEval;
            }
        }
        if (depth == 0) {
            int[][] moves = this.board.moves(board,x,y);
            return this.evaluation(board, pocket, availableCard, moves, currentDepth);
        }
        if (isMax) {
                int[][] moves = this.board.moves(board,x,y);
                Pocket pkt = new Pocket(pocket,availableCard);
                for (int[] move : moves) {
                    int[][] boardCopy = this.board.deepCopyBoard(board);
                    if (move[0] == -1){
                        break;
                    }
                    int[][] tempBoard = this.board.moveCard(x,y,move[0],move[1],boardCopy,pkt,false,false);
                    double eval = minMax(tempBoard, pkt.hand, pkt.nbAvailableCard, depth - 1, alpha, beta, false,move[0],move[1], currentDepth + 1);
                    if (timeElapsed > MAXTIME) {
                        return eval;
                    }
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, maxEval);
                    if (beta <= alpha) {
                        break;
                    }
                }
                if(moves[11][0] == 0){
                    return this.evaluation(board, pocket, availableCard, moves, currentDepth);
                }
                return maxEval;
        } else {

            int[][] moves = this.board.moves(board,x,y);
            Pocket pkt = new Pocket(pocket,availableCard);
            for (int[] move : moves) {
                int[][] boardCopy = this.board.deepCopyBoard(board);
                if (move[0] == -1){
                    break;
                }
                int[][] tempBoard = this.board.moveCard(x,y,move[0],move[1],boardCopy,pkt,true,false);
                double eval = minMax(tempBoard, pkt.hand, pkt.nbAvailableCard, depth - 1, alpha, beta, true,move[0],move[1],currentDepth + 1);
                if (timeElapsed > MAXTIME) {
                    return eval;
                }
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, minEval);
                if (beta <= alpha) {
                    break;
                }
            }
            if(moves[11][0] == 0){
                return this.evaluation(board, pocket, availableCard, moves, currentDepth);
            }
            return minEval;
        }
    }

    /**
     * Evaluate a board worth compared to an opponent.
     *
     * @param board the board that need a evaluation
     * @param pocket all the house member held by the AI
     * @param availableCard  number of card available per house
     * @param moves list of all move possible
     * @param numMove  number of move played to this point
     * @return A score between 10000 and -10000
     */
    public double evaluation(int[][] board, int[][] pocket, int[] availableCard, int[][] moves, int numMove){
        final double K = (481d/280d);
        final int MAXSCORE = 10000;

        double score = 0;
        int numberShield = 0;
        int house = 2;

        for(int[] houseHand : pocket){
            int nbCardHousePocket = house + 1;
            int badge = house + 2;
            int housePos = house - 2;
            int cardAvailability = availableCard[housePos] == -1 ? 0 : availableCard[housePos];
            if(moves[11][0] == 0){
                if (numberShield >= 4){
                    score = MAXSCORE;
                    return score;
               /* score = MAXSCORE * 0.75;
                score += (MAXSCORE * 0.125)/numMove;*/
                }
                else{
                    /*score = -MAXSCORE * 0.75;
                    score -= (MAXSCORE * 0.125)/numMove;*/
                    return MAXSCORE * -1;
                }
            }
            if((float)(cardAvailability + houseHand[nbCardHousePocket])/house > 0.50f || houseHand[badge] == 1 ){
                if(houseHand[badge] == 1 ){
                    score += ((MAXSCORE * 0.75)/(8*2) );
                }
                if((houseHand[nbCardHousePocket] / house) > 0.50 ){
                    score += ((MAXSCORE * 0.75)/(8*2) );
                }
                else{
                    score += ((MAXSCORE * 0.20)/8 );
                }
                score += ((MAXSCORE * 0.05)*houseHand[nbCardHousePocket])/(8*house);
                /*else{
                    for( int i = 1; i <= houseHand[nbCardHousePocket]; i++ ){
                        score += (((MAXSCORE/4) * 0.75)/(8 * house));
                        //score += ((MAXSCORE * 0.75)/(house * K))/house;
                    }
                    score += houseHand[nbCardHousePocket] * ((MAXSCORE * 0.125)/(7 * house));
                }*/
            }
            else{
                score -= ((MAXSCORE * 0.75)/8 );
            }
            /*if(availableCard[house-2] == -1){
                //score += ((MAXSCORE * 0.25)/8);
                //score += ((MAXSCORE * 0.75)/(house * K))/(house*8);
            }*/
            /*if(houseHand[badge] == 1 ){
                numberShield++;
            }
            house++;*/

        }

        //score  += (MAXSCORE * 0.15625)/((10 - numberShield) * K);

        return score;

    }
}
