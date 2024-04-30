enum SUITS {CLUBS, DIAMOND, HEARTS, SPADES, NO_TRUMP};
enum DOUBLE_STATUS {UNDOUBLED, DOUBLED, REDOUBLED};
public class Scoring {
    private final int BOOK = 6;
    private final int MINOR_SUIT_MULTIPLIER = 20;
    private final int MAJOR_SUIT_MULTIPLIER = 30;
    private final int DOUBLE_MULTIPLIER = 2;
    private final int REDOUBLE_MULTIPLIER = 4;
    private int calculateOvertrick(int trick_wins, Contract contract) {
        if (trick_wins <= contract.contract_level) return 0;
        if (contract.doubleStatus.equals(DOUBLE_STATUS.UNDOUBLED)) {
            if (contract.contract_suit.equals(SUITS.CLUBS) || contract.contract_suit.equals(SUITS.DIAMOND))
                return (trick_wins - BOOK - contract.contract_level) * MINOR_SUIT_MULTIPLIER;
            else if (contract.contract_suit.equals(SUITS.HEARTS) || contract.contract_suit.equals(SUITS.SPADES) ||
                    contract.contract_suit.equals(SUITS.NO_TRUMP))
                return (trick_wins - BOOK - contract.contract_level) * MAJOR_SUIT_MULTIPLIER;
        }
        int multiplier = (contract.vulnerable) ? 200 : 100;
        if (contract.doubleStatus.equals(DOUBLE_STATUS.REDOUBLED))  multiplier *= 2;
        return (trick_wins - BOOK - contract.contract_level) * multiplier;
    }
    private int calculateUndertrick(int trick_wins, Contract contract) {
        if (trick_wins >= contract.contract_level) return 0;
        int undertrick =  BOOK + contract.contract_level - trick_wins;
        if (contract.doubleStatus.equals(DOUBLE_STATUS.UNDOUBLED))
            return (contract.vulnerable) ? 100 * undertrick : 50 * undertrick;
        int result = 0;
        if (undertrick >= 1) result += (contract.vulnerable) ? 200 : 100;
        if (undertrick >= 2) result += (contract.vulnerable) ? 300 : 200;
        if (undertrick >= 3) result += (contract.vulnerable) ? 300 : 200;
        for (int i = 0; i < undertrick-3; i++) result += 300;
        if (contract.doubleStatus.equals(DOUBLE_STATUS.REDOUBLED)) result *= 2;
        return result;
    }
    private int calculateSlam(int trick_wins, Contract contract) {
        if (contract.contract_level < BOOK || contract.contract_level > 7) return 0;
        if (trick_wins - (contract.contract_level + BOOK) < 0) return 0;
        if (contract.contract_level == 6) return (contract.vulnerable) ? 750 : 500;
        else return (contract.vulnerable) ? 1500 : 1000;
    }
    private int calculateGameBonus(Contract contract) {
        int contractRequired = 0;
        if (contract.contract_suit == SUITS.NO_TRUMP) contractRequired += 10;
        if (contract.contract_suit.equals(SUITS.CLUBS) || contract.contract_suit.equals(SUITS.DIAMOND))
            contractRequired += MINOR_SUIT_MULTIPLIER;
        else if (contract.contract_suit.equals(SUITS.HEARTS) || contract.contract_suit.equals(SUITS.SPADES) ||
                contract.contract_suit.equals(SUITS.NO_TRUMP))
            contractRequired += MAJOR_SUIT_MULTIPLIER;
        if (contract.doubleStatus.equals(DOUBLE_STATUS.DOUBLED)) contractRequired *= DOUBLE_MULTIPLIER;
        else if (contract.doubleStatus.equals(DOUBLE_STATUS.REDOUBLED)) contractRequired *= REDOUBLE_MULTIPLIER;
        int gameBonus = 0;
        if (contractRequired < 100) gameBonus = 50;
        else gameBonus = (contract.vulnerable) ? 500 : 300;
        return gameBonus + contractRequired;
    }
    private int calculateDoubleContract(Contract contract) {
        if (contract.doubleStatus.equals(DOUBLE_STATUS.DOUBLED)) return 50;
        if (contract.doubleStatus.equals(DOUBLE_STATUS.REDOUBLED)) return 100;
        else return 0;
    }
    public int calculateScore(int trick_wins, Contract contract) {
        if (trick_wins >= BOOK + contract.contract_level) {
            return calculateOvertrick(trick_wins, contract) + calculateSlam(trick_wins, contract) +
                    calculateGameBonus(contract) + calculateDoubleContract(contract);
        }
        return calculateUndertrick(trick_wins, contract);
    }
}