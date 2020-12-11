
/**
 * Trida reprezentuje tovarnu
 * @author Marek Křevký a Robin Kříž
 */
public class Factory {
    /**
     * Produkce tovarny v jednotlive dny
     */
    private int[][] production;

    /**
     * Setter
     * @param production produkce tovarny v jednotlive dny
     */
    public void setProduction(int[][] production) {
        this.production = production;
    }

    /**
     * Vrati produkci v jeden konkretni den
     * @param day jaky den
     * @return produkce v konkretni den
     */
    public int[] getProductionInOneDay(int day) {
        return production[day];
    }

    /**
     * Zmeni produkci v konkretni den
     * @param day cislo dne
     * @param good cislo zbozi
     * @param howMuch o kolik se zmeni
     */
    public void changeProduction(int day, int good, int howMuch) {
        production[day][good] -= howMuch;
    }
}
