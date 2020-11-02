package semestralka;

public class Supermarket {
    private int[] goodsOnStock;
    private int[][] demand;


    public int[] getGoodsOnStock() {
        return goodsOnStock;
    }

    public void setGoodsOnStock(int[] goodsOnStock) {
        this.goodsOnStock = goodsOnStock;
    }

    public int[][] getDemand() {
        return demand;
    }

    public void setDemand(int[][] demand) {
        this.demand = demand;
    }

    public void resetStock() {
        for(int i = 0; i < goodsOnStock.length; i++) {
            goodsOnStock[i] = 0;
        }
    }
}
