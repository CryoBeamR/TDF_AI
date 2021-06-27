

import TroneDeFer.Pocket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PocketTest {

    @Test
    void initiatePocketSpace() {
        int[][] expectedPocket = {
                {2,0,0,0,0},
                {3,0,0,0,0,0},
                {4,0,0,0,0,0,0},
                {5,0,0,0,0,0,0,0},
                {6,0,0,0,0,0,0,0,0},
                {7,0,0,0,0,0,0,0,0,0},
                {8,0,0,0,0,0,0,0,0,0,0}
        };
        Pocket pocket = new Pocket();
        pocket.initiatePocketSpace();
        assertArrayEquals(pocket.hand,expectedPocket);
    }
}