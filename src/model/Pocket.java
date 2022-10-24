package model;

public class Pocket {
    public int[][] hand = new int[7][];
    // number on member of every houses
    public static final int[] HOUSESET = new int[] {2,3,4,5,6,7,8};
    // 3 extras spaces for a shield, number of member of a house
    // and number of member of each house in possession.
    // ex: hand = [[],[],[],[],[],[],[],[],<-- houses info| rupture | general pocket info --->[],[],[],]
    private static final int EXTRASETSPACE = 3;
    // number of card available per house
    public int[] nbAvailableCard = new int[] {2,3,4,5,6,7,8};
    private int[] CARD_HAND_POS = {0,1,0,1,2,0,1,2,3,0,1,2,3,4,0,1,2,3,4,5,
            0,1,2,3,4,5,6,0,1,2,3,4,5,6,7};
    // this offset is use to find the position of house in the a array
    private static final int OFFSETS = 2;

    public Pocket(int[][] hand, int[] nbAvailableCard) {
        int[][] pocketCopy =  new int[7][];
        int i = 0;
        for(int[] row : hand){
            int [] rowCopy =  row.clone();
            pocketCopy[i++] = rowCopy;
        }
        this.hand = pocketCopy;
        System.arraycopy(nbAvailableCard,0,this.nbAvailableCard,0,7);

    }

    public Pocket() {
        initiatePocketSpace();
    }


    public void initiatePocketSpace(){
        int i = 0;
        for(int numberCardHouseSet : HOUSESET){
            hand[i] = new int[numberCardHouseSet + EXTRASETSPACE];
            hand[i][0]= numberCardHouseSet;
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
        return CARD_HAND_POS[card_pos];
    }

    public void updateHand(int[] cards, boolean addCardsToHand){
        int i = 0;
        int cardHouse = Board.findHouse(cards[i]);
        int cardAvailability;
        int nbOpponentHouseCard;
        int nbPlayerHouseCard;
        int NUMBER_OF_CARD = cardHouse+1;
        int HAS_SHIELD = cardHouse+2;
        int cardHouseIndex = cardHouse-OFFSETS;
        do{
            nbAvailableCard[cardHouseIndex] -= 1;
            // add to pocket if it's the AI turn
            if (addCardsToHand){
                int cardPocketIndex =  findCardPosPocket(cards[i]) + 1;
                hand[cardHouseIndex][cardPocketIndex] = cards[i];
                hand[cardHouseIndex][NUMBER_OF_CARD] +=1;
            }
        }while(cards[++i] != -1);
        cardAvailability = nbAvailableCard[cardHouseIndex] == -1 ? 0 : nbAvailableCard[cardHouseIndex];
        nbOpponentHouseCard = cardHouse - cardAvailability - hand[cardHouseIndex][cardHouse+1];
        nbPlayerHouseCard = hand[cardHouseIndex][NUMBER_OF_CARD];
        if(nbPlayerHouseCard > nbOpponentHouseCard ) {
            hand[cardHouseIndex][HAS_SHIELD] = 1;
        }else if (nbPlayerHouseCard < nbOpponentHouseCard ){
            hand[cardHouseIndex][HAS_SHIELD] = 0;
        }else {
            hand[cardHouseIndex][HAS_SHIELD] = addCardsToHand ? 1 : 0;
        }
    }

    public void setHand(int[][] hand) {
        this.hand = hand;
    }

    public void setNbAvailableCard(int[] nbAvailableCard) {
        this.nbAvailableCard = nbAvailableCard;
    }
}
