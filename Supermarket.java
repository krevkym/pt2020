package semestralka;

public class Supermarket {
    private int[] goodsOnStock;
    private int[][] demand;


    public int[] getTotalDemand(int day) {
        int[] totalDemand = new int[demand[0].length];
        for(int i = day+1; i < demand.length; i++) {
            for(int j = 0; j < demand[0].length; j++) {
                totalDemand[j] += demand[i][j];
            }
        }
        return totalDemand;
    }

    public int[] getGoodsOnStock() {
        return goodsOnStock;
    }

    public void setGoodsOnStock(int[] goodsOnStock) {
        this.goodsOnStock = goodsOnStock;
    }

    public int[] getDemandInDay(int day) {
        return demand[day];
    }


    public void setDemand(int[][] demand) {
        this.demand = demand;
    }

    public void addGoods(int howMuch, int goods) {
        goodsOnStock[goods] += howMuch;
    }

    public void updateStock(int day) {
        for(int i = 0; i < goodsOnStock.length; i++) {
            goodsOnStock[i] -= demand[day][i];
        }
    }
}