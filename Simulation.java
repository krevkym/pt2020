package semestralka;

public class Simulation {
    private Factory[] factories;
    private Supermarket[] supermarkets;
    private int[][] cost;
    private int days;

    public static void main(String[] args) {
        String[] filenames = {"real_large.txt", "real_medium.txt", "real_small.txt", "real_small_sink.txt", "test_optim.txt", "test_optim_sink.txt", "test_price.txt", "test_small.txt"};
        FileIO f = new FileIO();
        f.loadFromFile(filenames[3]);
        Simulation s = new Simulation(f);
        System.out.println("Celkova cena: " + s.simulate());
    }

    public Simulation(FileIO f) {
        supermarkets = f.getSupermarkets();
        factories = f.getFactories();
        cost = f.getCost();
        days = f.getDays();
    }

    public int simulate() {
        int suma = 0;
        for(int i = 0; i < days; i++) {

            int sumPerDay = simulateOneDay(i);
            suma += sumPerDay;
            System.out.println(i+1 + ". den cena - " + sumPerDay);
            System.out.println("---------------------");
            for(int j = 0; j < supermarkets.length;j++) {
                supermarkets[j].resetStock();
            }
        }
        return suma;
    }

    //ještě nějak přepíšu (nejspíš)
    private int simulateOneDay(int i) {
        int sum = 0;
        for(int j = 0; j < supermarkets.length; j++) {
            //System.out.println(j + ". supermarket:");
            int[] supermarketDemandPerDay = supermarkets[j].getDemand()[i];
            for(int k = 0; k < supermarketDemandPerDay.length; k++) {
                int demandAfterOneStuff = supermarketDemandPerDay[k]-supermarkets[j].getGoodsOnStock()[k];
                while(demandAfterOneStuff > 0) {
                    int oneCost = Integer.MAX_VALUE;
                    int factory = factories.length+1;
                    int available = -1;
                    for(int l = 0; l < factories.length; l++) {
                        int [] productionInDay = factories[l].getProductionInOneDay(i);
                        if(productionInDay[k] > 0) {
                            if(cost[l][j] < oneCost) {
                                oneCost = cost[l][j];
                                factory = l;
                                available = productionInDay[k];
                            }
                        }
                    }
                    if(factory == factories.length+1) {
                        System.out.println(k+1+ ". zbozi doslo - doobjednat!!!");
                        demandAfterOneStuff = 0;
                    } else {
                        int howMuch;
                        if(demandAfterOneStuff >= available) {
                            howMuch = available;

                        } else {
                            howMuch = demandAfterOneStuff;
                        }
                        factories[factory].changeProduction(i, k, howMuch);
                        //System.out.println(Arrays.toString(factories[factory].getProductionInOneDay(i)));
                        demandAfterOneStuff -= howMuch;
                        sum += (howMuch*oneCost);
                    }
                }
            }
            //System.out.println("----------------------------------");
        }
        return sum;
    }
}
