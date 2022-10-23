

import model.Board;
import model.Pocket;
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
        assertArrayEquals(pocket.hand,expectedPocket);
    }

    @Test
    void findCardPosPocket() {
        Pocket pocket = new Pocket();
        // number of card
        final int  MAXCARD = 36;
        int[] expectedHousePos = {0,1,0,1,2,0,1,2,3,0,1,2,3,4,0,1,2,3,4,5,
                0,1,2,3,4,5,6,0,1,2,3,4,5,6,7};
        for(int i = 1; i < MAXCARD;i++){
            int house = pocket.findCardPosPocket(i);
            assertEquals(expectedHousePos[i-1],house);
        }
    }
}