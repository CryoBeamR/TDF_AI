

import agent.MinMaxAgent;
import model.Board;
import model.Pocket;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinMaxAgentTest {

    @Test
    void bestMoveScenario0() {
        Pocket pocket = new Pocket();
        int[][] boardArray3 = {
                {-1,5,3,4,16,6},
                {12,11,9,1,8,7},
                {10,14,15,2,17,18},
                {35,23,32,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,34,22}
        };
        Board board = new Board(boardArray3);
        MinMaxAgent minMaxAgent = new MinMaxAgent();
        int[] actualMove = minMaxAgent.bestMove(0,0, pocket,board);
        int[] expectedMove = {3,0};
        assertArrayEquals(expectedMove,actualMove);
    }
    @Test
    void bestMoveScenario1() {
        Pocket pocket = new Pocket();
        int[][] boardArray3 = {
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,-1,24,0,8,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };
        Board board = new Board(boardArray3);
        int[][] hand = {
                {2,1,0,1,1},
                {3,0,0,0,0,0},
                {4,6,0,0,0,1,0},
                {5,10,11,0,13,14,4,1},
                {6,15,16,17,0,0,0,3,0},
                {7,0,0,0,0,0,26,0,1,0},
                {8,28,29,30,31,32,33,34,35,8,1}
        };
        int[] cardAvailable = {0,0,1,0,0,1,0};
        pocket.setHand(hand);
        pocket.setNbAvailableCard(cardAvailable);
        MinMaxAgent minMaxAgent = new MinMaxAgent();
        int[] actualMove = minMaxAgent.bestMove(1,2, pocket,board);
        int[] expectedMove = {4,2};
        assertArrayEquals(expectedMove,actualMove);
    }

    @Test
    void bestMoveScenario2() {
        Pocket pocket = new Pocket();
        int[][] boardArray3 = {
                {0,2,0,0,0,0},
                {0,0,0,0,0,0},
                {0,-1,0,0,31,0},
                {0,0,0,0,0,0},
                {0,0,27,0,19,0},
                {0,0,0,0,0,0}
        };
        Board board = new Board(boardArray3);
        int[][] hand = {
                {2,0,0,0,0},
                {3,3,4,0,4,1},
                {4,0,7,8,0,2,1},
                {5,0,11,12,0,0,2,0},
                {6,0,0,0,0,0,20,1,0},
                {7,21,22,23,24,25,26,0,6,1},
                {8,0,0,0,0,0,33,0,0,1,0}
        };
        int[] cardAvailable = {1,0,0,0,1,1,1};
        pocket.setHand(hand);
        pocket.setNbAvailableCard(cardAvailable);
        MinMaxAgent minMaxAgent = new MinMaxAgent();
        int[] actualMove = minMaxAgent.bestMove(1,2, pocket,board);
        int[] expectedMove = {1,0};
        assertArrayEquals(expectedMove,actualMove);
    }

}