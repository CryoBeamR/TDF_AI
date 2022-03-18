public class Pocket {
    public int[][] hand = new int[7][];
    // number on member of every houses
    public final int[] HOUSESET = new int[] {2,3,4,5,6,7,8};
    // 3 extras spaces for a shield, number of member of a house
    // and number of member of each house in possession.
    // ex hand = [[],[],[],[],[],[],[],[],<-- houses info| rupture | general pocket info --->[],[],[],]
    private final int EXTRASETSPACE = 3;
    // number of card available per house
    public int[] availableCard = new int[] {2,3,4,5,6,7,8};
    private int[] CARDHANDPOS = {0,1,0,1,2,0,1,2,3,0,1,2,3,4,0,1,2,3,4,5,
            0,1,2,3,4,5,6,0,1,2,3,4,5,6,7};

    public Pocket(int[][] hand, int[] availableCard) {
        int[][] pocketCopy =  new int[7][];
        int i = 0;
        for(int[] row : hand){
            int [] rowCopy =  row.clone();
            pocketCopy[i++] = rowCopy;
        }
        this.hand = pocketCopy;
        System.arraycopy(availableCard,0,this.availableCard,0,7);
    }

    public Pocket() {

    }



    public void initiatePocketSpace(){
        int i = 0;
        for(int maxMember : HOUSESET){
            hand[i] = new int[maxMember + EXTRASETSPACE];
            hand[i][0]= maxMember;
            i++;
        }
    }

    /**
     * Find the position of a card in the pocket depending on
     * the value of the card
     *
     * @param card the card number between 1 and 35
     * @return the position of the card in the pocket
     */
    public int findCardPosPocket(int card){
        if(card <= 0){
            // when card is not between 1 and 35 inclusively
            return -1;
        }
        int card_pos = card - 1;
        return CARDHANDPOS[card_pos];
    }


}
