package semestralka;

public class Factory {
    private int[][] production;

    public void setProduction(int[][] production) {
        this.production = production;
    }

    public int[] getProductionInOneDay(int day) {
        return production[day];
    }

    public void changeProduction(int day, int good, int howMuch) {
        production[day][good] -= howMuch;
    }
}
