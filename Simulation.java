package semestralka;

public class Simulation{
    private Factory[] factories;
    private Supermarket[] supermarkets;
    private int[][] cost;
    private int days;
    private int goods;
    private Controller c;
    private boolean isPaused;
    public boolean stop;

    public Simulation(FileIO f, Controller c) {
        supermarkets = f.getSupermarkets();
        factories = f.getFactories();
        cost = f.getCost();
        days = f.getDays();
        goods = f.getGoods();
        this.c = c;
        isPaused = false;
    }


    public int simulate() {
        int sum = 0;
        for(int i = 0; i < days; i++) {
            int sumPerDay = simulateOneDay(i);
            if(sumPerDay == -1) {
                c.getOutcome().appendText("Došlo zboží - ukončuji simulaci.\n");
                break;
            }
            sum += sumPerDay;
            c.getWhatDay().setText((i+1)+"");
            c.getDaily().setText(sumPerDay+"");
            c.getSumm().setText(sum+"");
            c.getOutcome().appendText(String.format("Den %d., cena - %,d\n", (i+1), sumPerDay));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(Supermarket supermarket : supermarkets) {
                supermarket.updateStock(i);
            }

            System.out.println(i + 1 + ". den cena - " + sumPerDay);
            System.out.println("---------------------");
            while(isPaused) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(stop) {
                stop = false;
                break;
            }
        }
        c.getOutcome().appendText("Celkova cena za simulaci - " + sum);
        return sum;
    }


    private int simulateOneDay(int i) {
        int sum = 0;
        for(int j = 0; j < goods; j++) {
            for(int l = 0; l < supermarkets.length; l++) {
                int demandInDay = supermarkets[l].getDemandInDay(i)[j] - supermarkets[l].getGoodsOnStock()[j];
                while(demandInDay > 0) {
                    int factory = getLowestCostFactory(l, i, j);
                    if(factory == -1) {
                        c.getOutcome().appendText("Zboží " + j + " došlo\n");
                        return -1;
                    }
                    int production = factories[factory].getProductionInOneDay(i)[j];
                    int number = Math.min(demandInDay, production);
                    sum += number*cost[factory][l];
                    demandInDay -= number;
                    supermarkets[l].addGoods(number ,j);
                    factories[factory].changeProduction(i, j, number);
                }
            }
            for(int l = 0; l < factories.length; l++) {
                int production = factories[l].getProductionInOneDay(i)[j];
                if(production > 0) {
                    int supermarket = getMarket(l, j, i, production);
                    int number;
                    if(supermarket == -1) {
                        break;
                    }
                    number = Math.min(supermarkets[supermarket].getTotalDemand(i)[j], production);
                    sum += number*cost[l][supermarket];
                    supermarkets[supermarket].addGoods(number, j);
                }
            }
        }
        return sum;

    }

    private int getMarket(int factory, int goods, int day, int production) {
        int lowestCost = Integer.MAX_VALUE;
        int market = -1;
        for(int j = 0; j < cost.length; j++) {
            if(cost[j][factory] < lowestCost) {
                if(supermarkets[j].getTotalDemand(day)[goods] > production) {
                    lowestCost = cost[factory][j];
                    market = j;
                }
            }

        }
        return market;
    }

    private int getLowestCostFactory(int supermarket, int day, int goods) {
        int lowestCost = Integer.MAX_VALUE;
        int factory = -1;
        for(int j = 0; j < cost.length; j++) {
            if(factories[j].getProductionInOneDay(day)[goods] > 0) {
                if(cost[j][supermarket] < lowestCost) {
                    lowestCost = cost[j][supermarket];
                    factory = j;
                }
            }
        }
        return factory;
    }

    public void setPaused(boolean state) {
        isPaused = state;
    }


}