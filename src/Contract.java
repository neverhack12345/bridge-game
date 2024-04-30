public class Contract {
    public int contract_level;
    public SUITS contract_suit;
    public DOUBLE_STATUS doubleStatus;
    public boolean vulnerable;

    public Contract(int contract_level, SUITS contract_suit,
                    DOUBLE_STATUS doubleStatus, boolean vulnerable) {
        this.contract_level = contract_level;
        this.contract_suit = contract_suit;
        this.doubleStatus = doubleStatus;
        this.vulnerable = vulnerable;
    }
}
