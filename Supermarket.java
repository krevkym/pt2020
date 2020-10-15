package semestralka;

public class Supermarket {
    private int[] initialGoods;
    private int[][] demand;

    public int[] getInitialGoods() {
        return initialGoods;
    }

    public void setInitialGoods(int[] initialGoods) {
        this.initialGoods = initialGoods;
    }

    public int[][] getDemand() {
        return demand;
    }

    public void setDemand(int[][] demand) {
        this.demand = demand;
    }
}
