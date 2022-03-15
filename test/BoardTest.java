
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
    void initiatePocketSpace() {

    }

    @Test
    void deepCopyBoard() {
        int[][] boardArray = {
                {1,2,3,4,5,6},
                {12,11,10,9,8,7},
                {13,14,15,16,17,18},
                {24,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,35,34,33,32,31}
        };

        Board board = new Board();
        board.setBoard(boardArray);
        int[][] boardCopy = board.deepCopyBoard();
        // check entirety of the copy
        assertArrayEquals(boardCopy,boardArray);

        // check alterability of the copy
        boardCopy[4][3] = 0;
        assertFalse(Arrays.equals(boardCopy, boardArray));




    }

    @Test
    void findHouse() {
        // number of card
        final int  MAXCARD = 36;
        int[] expectedHouse = {2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,
        7,7,7,7,7,7,7,8,8,8,8,8,8,8,8};
        Board board =  new Board();
        for(int i = 1; i < MAXCARD;i++){
            int house = board.findHouse(i,8,35);
            assertEquals(expectedHouse[i-1],house);
        }
    }

    @Test
    void findCardPosPocket() {
        // number of card
        final int  MAXCARD = 36;
        int[] expectedHousePos = {0,1,0,1,2,0,1,2,3,0,1,2,3,4,0,1,2,3,4,5,
                0,1,2,3,4,5,6,0,1,2,3,4,5,6,7};
        Board board =  new Board();
        for(int i = 1; i < MAXCARD;i++){
            int house = board.findCardPosPocket(i,8,35);
            assertEquals(expectedHousePos[i-1],house);
        }
    }

    @Test
    void grabCard() {
        int[][] boardArray = {
                {32,2,3,4,5,6},
                {12,11,10,9,8,7},
                {34,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,24,13,33,1,31}
        };
        int[][] expectedBoardArray = {
                {0,2,3,4,5,6},
                {12,11,10,9,8,7},
                {0,14,15,16,17,18},
                {0,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,24,13,33,1,31}
        };

        Board board = new Board();
        Pocket pocket = new Pocket();
        pocket.initiatePocketSpace();
        int[][] movedBoard =  board.grabCard(0,5,0,0,
                boardArray,pocket,true);
        for(int i=0; i < 6; i++){
            System.out.println(Arrays.toString(movedBoard[i]));
        }
        assertArrayEquals(expectedBoardArray,movedBoard);
    }

    @Test
    void moveCard() {
        int[][] boardArray = {
                {32,2,3,4,5,6},
                {12,11,10,9,8,7},
                {34,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,24,13,33,1,31}
        };
        int[][] expectedBoardArray = {
                {32,2,3,4,5,6},
                {12,11,10,9,8,7},
                {34,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {0,24,13,0,1,-1}
        };

        Board board = new Board();
        Pocket pocket = new Pocket();
        pocket.initiatePocketSpace();
        int[][] movedBoard =  board.moveCard(0,5,5,5,
                boardArray,pocket, true);
        for(int i=0; i < 6; i++){
            System.out.println(Arrays.toString(movedBoard[i]));
        }
        assertArrayEquals(expectedBoardArray,movedBoard);
    }

    @Test
    void moves() {
        // Board #1

        int[][] boardArray = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,24,13,33,1,31}
        };
        int[][] expectedMoves = {{5,5},{4,5},{2,5},{1,5},{0,0},{0,1},{0,4},{-1,0},{0,0},{0,0},{7,0}};

        Board board = new Board();
        int[][] moves=  board.moves(boardArray,0,5,1);

        assertArrayEquals(expectedMoves,moves);

        // Board #2

        int[][] boardArray2 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,-1}
        };
        int[][] expectedMoves2 = {{0,5},{1,5},{2,5},{4,5},{5,0},{5,2},{5,4},{-1,0},{0,0},{0,0},{7,0}};

        moves=  board.moves(boardArray2,5,5,1);
        assertArrayEquals(expectedMoves2,moves);

        // Board #3

        int[][] boardArray3 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,-1,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves3 = {{0,3},{1,3},{5,3},{3,3},{2,0},{2,1},{2,2},{2,5},{2,4},{-1,0},{9,0}};

        moves=  board.moves(boardArray3,2,3,1);

        assertArrayEquals(expectedMoves3,moves);

    }

    @Test
    void moves_x() {
        int[][] boardArray3 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,-1,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves3 = {{0,3},{1,3},{5,3},{3,3},{0,0},{4,0}};
        Board board = new Board();
        int[][] moves=  board.moves_x(boardArray3,2,3,1);

        assertArrayEquals(expectedMoves3,moves);
    }

    @Test
    void moves_y() {
        int[][] boardArray3 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,-1,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves3 = {{2,0},{2,1},{2,2},{2,5},{2,4},{5,0}};
        Board board = new Board();
        int[][] moves=  board.moves_y(boardArray3,2,3,1);

        assertArrayEquals(expectedMoves3,moves);
    }
}