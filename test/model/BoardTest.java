package model;

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

        Board board = new Board(boardArray);
        int[][] boardCopy = board.deepCopyBoard();
        // check entirety of the copy
        assertArrayEquals(boardCopy,boardArray);

        // check alterability of the copy
        boardCopy[4][3] = 0;
        assertFalse(Arrays.deepEquals(boardCopy, boardArray));




    }

    @Test
    void findHouse() {
        // number of card
        final int  MAX_CARD = 36;
        int[] expectedHouse = {2,2,3,3,3,4,4,4,4,5,5,5,5,5,6,6,6,6,6,6,
        7,7,7,7,7,7,7,8,8,8,8,8,8,8,8};
        for(int i = 1; i < MAX_CARD;i++){
            int house = Board.findHouse(i);
            assertEquals(expectedHouse[i-1],house);
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


        Board.grabCard(0,5,0,0, boardArray);
        assertArrayEquals(expectedBoardArray,boardArray);
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

        Pocket pocket = new Pocket();
        int[][] movedBoard =  Board.moveCard(0,5,5,5,
                boardArray,pocket, true);
        for(int i=0; i < 6; i++){
            System.out.println(Arrays.toString(movedBoard[i]));
        }
        assertArrayEquals(expectedBoardArray,movedBoard);
    }

    @Test
    void moves() {
        // model.Board #1

        int[][] boardArray = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {-1,24,13,33,1,31}
        };
        int[][] expectedMoves = {{5,5},{4,5},{2,5},{1,5},{0,0},{0,1},{0,4},{-1,0},{-1,0},{-1,0},{-1,0},{7,0}};


        int[][] moves=  Board.moves(boardArray,0,5);

        assertArrayEquals(expectedMoves,moves);

        // model.Board #2

        int[][] boardArray2 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,22,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,-1}
        };
        int[][] expectedMoves2 = {{0,5},{1,5},{2,5},{4,5},{5,0},{5,2},{5,4},{-1,0},{-1,0},{-1,0},{-1,0},{7,0}};

        moves=  Board.moves(boardArray2,5,5);
        assertArrayEquals(expectedMoves2,moves);

        // model.Board #3

        int[][] boardArray3 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,7},
                {10,14,15,16,17,18},
                {35,23,-1,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves3 = {{5,3},{3,3},{0,3},{1,3},{2,5},{2,4},{2,0},{2,1},{2,2},{-1,0},{-1,0},{9,0}};

        moves=  Board.moves(boardArray3,2,3);

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
        int[][] expectedMoves3 = {{5,3},{3,3},{0,3},{1,3},{0,0},{4,0}};

        int[][] moves=  Board.moves_axe(boardArray3,2,3,0);

        assertArrayEquals(expectedMoves3,moves);
        int[][] boardArray4 = {
                {32,2,3,4,5,6},
                {12,11,34,9,8,-1},
                {10,14,15,16,17,18},
                {35,23,7,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves4 = {{0,1},{2,1},{3,1},{0,0},{0,0},{3,0}};
        int[][] moves2 =  Board.moves_axe(boardArray4,5,1,0);

        assertArrayEquals(expectedMoves4,moves2);

        int[][] boardArray5 = {
                {32,2,3,4,5,6},
                {-1,11,34,9,8,12},
                {10,14,15,16,17,18},
                {35,23,7,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves5 = {{5,1},{4,1},{2,1},{0,0},{0,0},{3,0}};
        int[][] moves3 =  Board.moves_axe(boardArray5,0,1,0);

        assertArrayEquals(expectedMoves5,moves3);

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
        int[][] expectedMoves3 = {{2,5},{2,4},{2,0},{2,1},{2,2},{5,0}};
        int[][] moves=  Board.moves_axe(boardArray3,2,3,1);

        assertArrayEquals(expectedMoves3,moves);

        int[][] boardArray4 = {
                {32,2,-1,4,5,6},
                {12,11,34,9,8,3},
                {10,14,15,16,17,18},
                {35,23,7,21,20,19},
                {25,26,27,28,29,30},
                {31,24,13,33,1,22}
        };
        int[][] expectedMoves4 = {{2,5},{2,4},{2,3},{2,2},{2,1},{5,0}};
        int[][] moves2 =  Board.moves_axe(boardArray4,2,0,1);

        assertArrayEquals(expectedMoves4,moves2);

        int[][] boardArray5 = {
                {32,2,3,4,5,6},
                {13,11,34,9,8,12},
                {10,14,15,16,17,18},
                {35,23,7,21,20,19},
                {25,26,27,28,29,30},
                {31,24,-1,33,1,22}
        };
        int[][] expectedMoves5 = {{2,0},{2,1},{2,2},{2,3},{2,4},{5,0}};
        int[][] moves3 =  Board.moves_axe(boardArray5,2,5,1);

        assertArrayEquals(expectedMoves5,moves3);
    }

    @Test
    void deductOppositePocket() {
        Pocket pocket = new Pocket();
        int[][] boardArray = {
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,-1,24,0,8,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0},
                {0,0,0,0,0,0}
        };
        Board board = new Board(boardArray);
        int[][] hand = {
                {2,1,0,1,1},
                {3,0,0,0,0,0},
                {4,6,0,0,0,1,0},
                {5,10,11,0,13,14,4,1},
                {6,15,16,17,0,0,0,3,0},
                {7,0,0,0,0,0,26,0,1,0},
                {8,28,29,30,31,32,33,34,35,8,1}
        };
        int[][] expectedHand = {
                {2, 0, 2, 1, 0},
                {3, 3, 4, 5, 3, 1},
                {4, 0, 7, 0, 9, 2, 1},
                {5, 0, 0, 12, 0, 0, 1, 0},
                {6, 0, 0, 0, 18, 19, 20, 3, 1},
                {7, 21, 22, 23, 0, 25, 0, 27, 5, 1},
                {8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        int[] cardAvailable = {0,0,1,0,0,1,0};
        pocket.setHand(hand);
        pocket.setNbAvailableCard(cardAvailable);

        Pocket oppositePocket = Board.deductOppositePocket(pocket,board);

        assertArrayEquals(expectedHand,oppositePocket.getHand());

    }
}