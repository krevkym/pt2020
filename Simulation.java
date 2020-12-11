
/**
 * Trida na simulaci
 * @author Marek Křevký a Robin Kříž
 */
public class Simulation{
    /**
     * Tovarny
     */
    private Factory[] factories;
    /**
     * Supermarkety
     */
    private Supermarket[] supermarkets;
    /**
     * Cena prevozu jednoho druhu zbozi z tovarny do supermarketu
     */
    private int[][] cost;
    /**
     * Pocet dni simulace
     */
    private int days;
    /**
     * Pocet druhu zbozi
     */
    private int goods;
    /**
     * Kontroler pro GUI
     */
    private AppController c;
    /**
     * Flag pro pausnuti simulace
     */
    private boolean isPaused;

    /**
     * Flag pro stopnuti simulace
     */
    private boolean stop;

    /**
     * Konstruktor ktery naplni atributy
     * @param f FileIO pro ziskani atributu
     * @param c Kontoler pro vypisovani informaci
     */
    public Simulation(FileIO f, AppController c) {
        supermarkets = f.getSupermarkets();
        factories = f.getFactories();
        cost = f.getCost();
        days = f.getDays();
        goods = f.getGoods();
        this.c = c;
        isPaused = false;
    }

    /**
     * Metoda pro simulaci
     * @return celkova cena za simulaci
     */
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
            c.getSum().setText(sum+"");
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


    /**
     * Simulace jednoho dne
     * @param i den
     * @return cena za jeden den
     */
    private int simulateOneDay(int i) {
        int sum = 0;
        for(int j = 0; j < goods; j++) {
            for(int l = 0; l < supermarkets.length; l++) {
                int demandInDay = supermarkets[l].getDemandInDay(i)[j] - supermarkets[l].getGoodsOnStock()[j];
                while(demandInDay > 0) {
                    int factory = getLowestCostFactory(l, i, j);
                    if(factory == -1) {
                        c.getOutcome().appendText("Zboží " + j + " došlo\nVyžádáno od " + l + ". supermarketu\nChybi " + demandInDay + " zbozi.\n");
                        stop = true;
                        return sum;
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

    /**
     * Ziskani nejblizsiho supermarketu
     * @param factory tovarna odkud hledame
     * @param goods zbozi ktere hledame
     * @param day v den ktery hledame
     * @param production produkce tovarny
     * @return index nejblizsiho supermarketu
     */
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

    /**
     * Ziskani nejblizsi tovarny
     * @param supermarket supermarket odkud hledame
     * @param day v den ktery hledame
     * @param goods zbozi ktere hledame
     * @return index nejblizsi tovarny
     */
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

    /**
     * Nastavi flag
     * @param state novy stav
     */
    public void setPaused(boolean state) {
        isPaused = state;
    }

    /**
     * Nastavi flag
     * @param stop novy stav
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }
}