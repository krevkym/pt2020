package semestralka;

public class Simulation {
    private Factory[] factories;
    private Supermarket[] supermarkets;
    private int[][] cost;
    private int days;
    private int goods;

    public static void main(String[] args) {
        String[] filenames = {"real_large.txt", "real_medium.txt", "real_small.txt", "real_small_sink.txt", "test_optim.txt", "test_optim_sink.txt", "test_price.txt", "test_small.txt"};
        FileIO f = new FileIO();
        f.loadFromFile(filenames[2]);
        Simulation s = new Simulation(f);
        System.out.println("Celkova cena: " + s.simulate());
    }

    public Simulation(FileIO f) {
        supermarkets = f.getSupermarkets();
        factories = f.getFactories();
        cost = f.getCost();
        days = f.getDays();
        goods = f.getGoods();
    }

    private int simulate() {
        int suma = 0;
        for(int i = 0; i < days; i++) {
            int sumPerDay = simulateOneDay(i);
            suma += sumPerDay;
            for(int j = 0; j < supermarkets.length; j++) {
                supermarkets[j].updateStock(i);
            }
            System.out.println(i+1 + ". den cena - " + sumPerDay);
            System.out.println("---------------------");

        }
        return suma;
    }

    private int simulateOneDay(int i) {
        int sum = 0;
        for(int j = 0; j < goods; j++) {
            for(int l = 0; l < supermarkets.length; l++) {
                int demandInDay = supermarkets[l].getDemandInDay(i)[j] - supermarkets[l].getGoodsOnStock()[j];
                while(demandInDay > 0) {
                    int factory = getLowestCostFactory(l, i, j);
                    if(factory == -1) {
                        System.out.println("Zbozi " + j + " doslo");
                        break;
                    }
                    int production = factories[factory].getProductionInOneDay(i)[j];
                    int number;
                    if(demandInDay >= production) {
                        number = production;
                    } else {
                        number = demandInDay;
                    }
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
                    if(supermarkets[supermarket].getTotalDemand(i)[j] >= production) {
                            number = production;
                    } else {
                            number = supermarkets[supermarket].getTotalDemand(i)[j];
                    }
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

}
