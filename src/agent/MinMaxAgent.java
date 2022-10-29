package agent;

import model.Board;
import model.Pocket;


public class MinMaxAgent {
    private static final int  DEPT = 9;

    /**
     * Find the best move depending of the heuristic of every board position
     * it encounter.
     *
     * @param x the initial position of the moving card on axe x
     * @param y the initial position of the moving card on axe y
     * @return coordinate {x,y} of the best move calculated
     */
    public int[] bestMove(int x, int y, Pocket pocket, Board board){
        double maxScore = -10000;
        int[][] moves = Board.moves(board.deepCopyBoard(),x,y);
        int[] bestMove = {moves[0][0],moves[0][1]};
        for(int[] move : moves ){
            Pocket tempPocket = new Pocket(pocket.hand ,pocket.nbAvailableCard);
            if (move[0] == -1){
                break;
            }
            int [][] tempBoard = Board.moveCard(x,y,move[0],move[1],board.deepCopyBoard(),tempPocket,true);
            double score = minMax(tempBoard,tempPocket.hand,tempPocket.nbAvailableCard, DEPT,-10000,10000,false,move[0],move[1], 1);
            if (score > maxScore){
                maxScore = score;
                bestMove[0] = move[0];
                bestMove[1] = move[1];
            }
        }
        System.out.println("Best Move : {"+ bestMove[0]+","+bestMove[1]+"}");
        return bestMove;
    }

    public double minMax(int[][] board,int[][] pocket,int[] availableCard, int depth , double alpha, double beta, boolean isMax, int x, int y, int currentDepth) {
        double maxEval = -10000;
        double minEval = 10000;
        if (depth == 0) {
            int[][] moves = Board.moves(board,x,y);
            return this.evaluation(pocket, availableCard, moves);
        }
        int[][] moves = Board.moves(board,x,y);
        Pocket pkt = new Pocket(pocket,availableCard);
        if (isMax) {
            for (int[] move : moves) {
                int[][] boardCopy = Board.deepCopyBoard(board);
                if (move[0] == -1){
                    break;
                }
                int[][] tempBoard = Board.moveCard(x,y,move[0],move[1],boardCopy,pkt,true);
                double eval = minMax(tempBoard, pkt.hand, pkt.nbAvailableCard, depth - 1, alpha, beta, false,move[0],move[1], currentDepth + 1);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha) {
                    break;
                }
            }
            if(moves[11][0] == 0){
                return this.evaluation(pocket, availableCard, moves);
            }
            return maxEval;
        } else {
            for (int[] move : moves) {
                int[][] boardCopy = Board.deepCopyBoard(board);
                if (move[0] == -1){
                    break;
                }
                int[][] tempBoard = Board.moveCard(x,y,move[0],move[1],boardCopy,pkt,false);
                double eval = minMax(tempBoard, pkt.hand, pkt.nbAvailableCard, depth - 1, alpha, beta, true,move[0],move[1],currentDepth + 1);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, minEval);
                if (beta <= alpha) {
                    break;
                }
            }
            if(moves[11][0] == 0){
                return this.evaluation(pocket, availableCard, moves);
            }
            return minEval;
        }
    }

    /**
     * Evaluate a board worth compared to an opponent.
     *
     * @param pocket all the house member held by the AI
     * @param availableCard  number of card available per house
     * @param moves list of all move possible
     * @return A score between 10000 and -10000
     */
    public double evaluation(int[][] pocket, int[] availableCard, int[][] moves){
        final int MAXSCORE = 10000;
        final float OFFENSIVE_RATIO = 0.15f;
        final float DEFENSIVE_RATIO = 1 - OFFENSIVE_RATIO;
        double score = 0;
        int numberShield = 0;
        int currentHouse = 2;
        // Target number of card needed to have majority
        int[] targetCardsNumber = {2,2,3,3,4,4,5};

        for(int[] houseHand : pocket){
            int nbCardHousePocketIndex = currentHouse + 1;
            int badgeIndex = currentHouse + 2;
            int houseIndex = currentHouse - 2;
            int cardAvailability = availableCard[houseIndex] == -1 ? 0 : availableCard[houseIndex];
            boolean hasBadge = houseHand[badgeIndex] == 1;

            boolean isHouseBadgeReachable = ((float)(cardAvailability + houseHand[nbCardHousePocketIndex])/currentHouse) > 0.50f;
            boolean hasBadgeWhenNoCardAvailable = ((float)(houseHand[nbCardHousePocketIndex])/currentHouse) == 0.50f && hasBadge && cardAvailability == 0;
            boolean hasNoBadgeWhenCardAvailable = !hasBadge && (float) cardAvailability/currentHouse == 0.50f ;
            boolean isHouseBadgeAvailable = isHouseBadgeReachable || hasBadgeWhenNoCardAvailable || hasNoBadgeWhenCardAvailable;

            if( isHouseBadgeAvailable){
                boolean hasMajorityOfHouseCards = (float)(houseHand[nbCardHousePocketIndex]) / currentHouse > 0.50f;
                //Give points if has majority of house cards or was the last to equalize
                if( hasMajorityOfHouseCards || hasBadgeWhenNoCardAvailable){
                    score += ((MAXSCORE * DEFENSIVE_RATIO)/8 );
                }
                else{
                    float cardsByTarget= (float) houseHand[nbCardHousePocketIndex]/(targetCardsNumber[houseIndex]);
                    score += ((MAXSCORE * OFFENSIVE_RATIO)/8)*cardsByTarget ;
                }
            } else{
                score -= ((MAXSCORE * DEFENSIVE_RATIO)/8 );
            }
            if(houseHand[badgeIndex] == 1 ){
                numberShield++;
            }
            currentHouse++;

        }
        if(moves[11][0] == 0){
            if (numberShield >= 4){
                return MAXSCORE;
            }
            else{
                return MAXSCORE * -1;
            }
        }

        return score;

    }
}
