
/**
 * Trida reprezentujici supermarket
 * @author Marek Křevký a Robin Kříž
 */
public class Supermarket {
    /**
     * zbozi na sklade
     */
    private int[] goodsOnStock;
    /**
     * poptavka supermarketu
     */
    private int[][] demand;

    /**
     * vrati celkovou poptavku supermarketu od konkretniho dne
     * @param day v ktery den
     * @return celkova poptavka po zbozi od konkretniho dne
     */
    public int[] getTotalDemand(int day) {
        int[] totalDemand = new int[demand[0].length];
        for(int i = day+1; i < demand.length; i++) {
            for(int j = 0; j < demand[0].length; j++) {
                totalDemand[j] += demand[i][j];
            }
        }
        return totalDemand;
    }

    /**
     * Vrati zbozi na sklade
     * @return pole intu
     */
    public int[] getGoodsOnStock() {
        return goodsOnStock;
    }

    /**
     * Nastavi zbozi na sklade
     * @param goodsOnStock pole intu
     */
    public void setGoodsOnStock(int[] goodsOnStock) {
        this.goodsOnStock = goodsOnStock;
    }

    /**
     * Vrati poptavku za jeden den
     * @param day v ktery den
     * @return pole intu
     */
    public int[] getDemandInDay(int day) {
        return demand[day];
    }

    /**
     * Nastavi poptavku supermarketu
     * @param demand 2D pole intu
     */
    public void setDemand(int[][] demand) {
        this.demand = demand;
    }

    /**
     * Prida zbozi na sklad
     * @param howMuch kolik se ma pridat
     * @param goods index zbozi ktere se ma pridat
     */
    public void addGoods(int howMuch, int goods) {
        goodsOnStock[goods] += howMuch;
    }

    /**
     * Odecte od zbozi na sklade poptavane zbozi za konkretni den
     * @param day v ktery den
     */
    public void updateStock(int day) {
        for(int i = 0; i < goodsOnStock.length; i++) {
            goodsOnStock[i] -= demand[day][i];
        }
    }
}