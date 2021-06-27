package TroneDeFer;

public class Pocket {
    public int[][] hand = new int[7][];
    // number on member of every houses
    public final int[] HOUSESET = new int[] {2,3,4,5,6,7,8};
    // 3 extras spaces for a shield, number of member of a house
    // and number of member of each house in possession.
    private final int EXTRASETSPACE = 3;
    // number of card available per house
    public int[] availableCard = new int[] {2,3,4,5,6,7,8};

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
}
