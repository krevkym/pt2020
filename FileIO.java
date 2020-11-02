package semestralka;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileIO {

    private Factory[] factories;
    private Supermarket[] supermarkets;
    private int[][] cost;
    private int days;


    public int[][] getCost() {
        return cost;
    }
    public Factory[] getFactories() {
        return factories;
    }
    public Supermarket[] getSupermarkets() {
        return supermarkets;
    }
    public int getDays() {
        return days;
    }

    //nevím jak to napsat jinak
    void loadFromFile(String fileName) {
        File file = new File("./src/semestralka/files/"+fileName);
        try {
            Scanner sc = new Scanner(file);
            String line = skipLines(sc);

            String[] data = line.split(" ");
            factories = new Factory[Integer.parseInt(data[0])];
            for(int i = 0; i < factories.length; i++) {
                factories[i] = new Factory();
            }
            supermarkets = new Supermarket[Integer.parseInt(data[1])];
            for(int i = 0; i < supermarkets.length; i++) {
                supermarkets[i] = new Supermarket();
            }
            int goods = Integer.parseInt(data[2]);
            days = Integer.parseInt(data[3]);
            int[] blockHeights = new int[]{factories.length, goods, goods *days, goods *days};
            int[] blockWidths = new int[]{supermarkets.length, supermarkets.length, factories.length, supermarkets.length};
            int blockCounter;
            int[][][] blocks = new int[4][][];
            for(blockCounter = 0; blockCounter < 4; blockCounter++) {
                line = sc.nextLine();
                blocks[blockCounter] = loadArray(sc, line, blockHeights[blockCounter], blockWidths[blockCounter]);
            }
            cost = blocks[0];
            int[] goodsS;
            for(int i = 0; i < supermarkets.length; i++) {
                goodsS = new int[goods];
                for(int o = 0; o < blocks[1].length; o++) {
                    goodsS[o] = blocks[1][o][i];
                }
                supermarkets[i].setGoodsOnStock(goodsS);
                int[][] demandPerDay = extractArray(blocks[3], days, goods, i);
                supermarkets[i].setDemand(demandPerDay);
            }
            for(int i = 0; i < factories.length; i++) {
                int[][] productionPerDay = extractArray(blocks[2], days, goods, i);
                factories[i].setProduction(productionPerDay);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String skipLines(Scanner sc) {
        String line = "";
        while(sc.hasNextLine()) {
            if (!(line = sc.nextLine()).equals("") && (line.charAt(0) != '#')) {
                break;
            }
        }
        return line;
    }

    int[][] extractArray(int[][] array, int a, int b, int i) {
        int counter = 0;
        int[][] productionPerDay = new int[a][b];
        for(int o = 0; o < a; o++) {
            for(int p = o; p < b*a; p+=a) {
                productionPerDay[o][counter] = array[p][i];
                counter++;
            }
            counter=0;
        }
        return productionPerDay;
    }

    int[][] loadArray(Scanner sc, String line, int a, int b) {
        int[][] array = new int[a][b];
        for(int i = 0; i < array.length; i++) {
            if(line.equals("") || line.charAt(0) == '#') {
                i--;
                line = sc.nextLine();
                continue;
            }
            //v real_small_sink.txt jsou cisla v poslednim sloupci v poptavce oddeleny dvemi mezerami.. takze celkem neprijemny split
            String[] lineSplit = line.split(" {2}| ");
            for(int o = 0; o < b; o++) {
                array[i][o] = Integer.parseInt(lineSplit[o]);
            }
            if(sc.hasNextLine()) {
                line = sc.nextLine();
            }
        }
        return array;
    }
}
