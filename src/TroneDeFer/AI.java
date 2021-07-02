package TroneDeFer;

public class AI {
    Board board;
    long start;
    long finish ;
    long timeElapsed ;
    final long MAXTIME = 459999999000l;
    int depth = 7;

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
        int[][] moves = this.board.moves(board.deepCopyBoard(),x,y,1);
        int[] bestMove = {moves[0][0],moves[0][1]};
        for(int[] move : moves ){
            Pocket tempPocket = new Pocket(pocket.hand ,pocket.availableCard);
            if (move[0] == -1){
                break;
            }
            int [][] board = this.board.moveCard(x,y,move[0],move[1],this.board.deepCopyBoard(),tempPocket,false);
            double score = minMax(board,pocket.hand,pocket.availableCard,depth,-10000,10000,false,move[0],move[1]);
            if (score > maxScore){
                maxScore = score;
                bestMove[0] = move[0];
                bestMove[1] = move[1];
            }
            if(timeElapsed > MAXTIME){
                System.out.println( depth + "<--- depth reached");
                System.out.println( "Move : {"+ bestMove[0]+","+bestMove[1]+"}");
                return bestMove;
            }
        }
        System.out.println( "Move : {"+ bestMove[0]+","+bestMove[1]+"}");
        return bestMove;
    }

    public double minMax(int[][] board,int[][] pocket,int[] availableCard, int depth , double alpha, double beta, boolean isMax, int x, int y) {
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
            int[][] moves = this.board.moves(board,x,y,1);
            return this.evaluation(board, pocket, availableCard, moves);
        }
        if (isMax) {
            int[][] moves = this.board.moves(board,x,y,1);
            Pocket pkt = new Pocket(pocket,availableCard);
            for (int[] move : moves) {
                int[][] boardCopy = this.board.deepCopyBoard(board);
                if (move[0] == -1){
                    break;
                }
                int[][] tempBoard = this.board.moveCard(x,y,move[0],move[1],boardCopy,pkt,false);
                double eval = minMax(tempBoard, pkt.hand, pkt.availableCard, depth - 1, alpha, beta, false,move[0],move[1]);
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
                return this.evaluation(board, pocket, availableCard, moves);
            }
            return maxEval;
        } else {

            int[][] moves = this.board.moves(board,x,y,1);
            Pocket pkt = new Pocket(pocket,availableCard);
            for (int[] move : moves) {
                int[][] boardCopy = this.board.deepCopyBoard(board);
                if (move[0] == -1){
                    break;
                }
                int[][] tempBoard = this.board.moveCard(x,y,move[0],move[1],boardCopy,pkt,true);
                double eval = minMax(tempBoard, pkt.hand, pkt.availableCard, depth - 1, alpha, beta, true,move[0],move[1]);
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
                return this.evaluation(board, pocket, availableCard, moves);
            }
            return minEval;
        }
    }

    /**
     * Evaluate a board worth compared to an opponent.
     *
     * @param board the board that need a evaluation
     * @param pocket all the house member held by the AI
     * @return A score between 10000 and -10000
     */
    public double evaluation(int[][] board, int[][] pocket, int[] availableCard, int[][] moves){
        final double K = (481d/280d);
        final int MAXSCORE = 1000;

        double score = 0;
        int numberShield = 0;
        int house = 2;
        for(int[] houseHand : pocket){
            int cardAvailability = availableCard[house - 2] == -1 ? 0 : availableCard[house - 2];
            if((float)(cardAvailability + houseHand[house + 1 ])/house > 0.50f || houseHand[house + 2] == 1 ){
                if((houseHand[house + 1 ] / house) > 0.50){
                    score += ((MAXSCORE * 0.75)/8 );
                }
                else{
                    for( int i = 1; i <= houseHand[house + 1 ]; i++ ){
                        score += (((MAXSCORE/4) * 0.75)/(8 * house));
                        //score += ((MAXSCORE * 0.75)/(house * K))/house;
                    }
                }
            }
            else{
                score -= ((MAXSCORE * 0.75)/8 );
            }
            if(availableCard[house-2] == -1){
                //score += ((MAXSCORE * 0.25)/8);
                //score += ((MAXSCORE * 0.75)/(house * K))/(house*8);
            }
            if(houseHand[house + 2] == 1 ){
                numberShield++;
            }
            house++;

        }
        if(moves[11][0] == 0){
            if (numberShield >= 4){
                score = 10000;
            }
            else{
                score = -10000;
            }
        }

        //score  += (MAXSCORE * 0.15625)/((10 - numberShield) * K);

        return score;

    }
}
