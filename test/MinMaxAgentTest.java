

import model.Pocket;
import org.junit.jupiter.api.Test;

class MinMaxAgentTest {

    @Test
    void bestMove() {
        Pocket pocket = new Pocket();
        int[][] boardArray3 = {
                {-1,5,3,4,16,6},
                {12,11,9,1,8,7},
                {10,14,15,2,17,18},
                {35,23,32,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,34,22}
        };
        MinMaxAgent minMaxAgent = new MinMaxAgent(boardArray3);
        int[] move = minMaxAgent.bestMove(0,0, pocket);
    }

    @Test
    void minMax() {
    }

    @Test
    void evaluation() {
    }
}