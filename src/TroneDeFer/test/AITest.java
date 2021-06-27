

import TroneDeFer.AI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void bestMove() {
        int[][] boardArray3 = {
                {-1,5,3,4,16,6},
                {12,11,9,1,8,7},
                {10,14,15,2,17,18},
                {35,23,32,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,34,22}
        };
        AI ai = new AI(boardArray3);
        int[] move = ai.bestMove(0,0);
    }

    @Test
    void minMax() {
    }

    @Test
    void evaluation() {
    }
}