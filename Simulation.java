package semestralka;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Simulation {
    private Factory[] factories;
    private Supermarket[] supermarkets;
    private int[][] cost;

    public static void main(String[] args) {
        String[] filenames = {"real_large.txt", "real_medium.txt", "real_small.txt", "real_small_sink.txt", "test_optim.txt", "test_optim_sink.txt", "test_price.txt", "test_small.txt"};
        Simulation s = new Simulation();
        s.loadFromFile(filenames[7]);
    }

     void loadFromFile(String fileName) {
         File file = new File("./src/semestralka/files/"+fileName);
         try {
             Scanner sc = new Scanner(file);
             String line;
             line = skipLines(sc);
             //System.out.println(line);
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
             int days = Integer.parseInt(data[3]);

             line = skipLines(sc);
             cost = loadArray(sc, line, factories.length, supermarkets.length);

             line = skipLines(sc);
             int[][] helpArray = loadArray(sc, line, goods, supermarkets.length);
             int[] goodsS;
             for(int i = 0; i < helpArray[0].length; i++) {
                 goodsS = new int[goods];
                 for(int o = 0; o < helpArray.length; o++) {
                     goodsS[o] = helpArray[o][i];
                 }
                 supermarkets[i].setInitialGoods(goodsS);
             }
             line = skipLines(sc);
             helpArray = loadArray(sc, line, goods*days, factories.length);
             for(int i = 0; i < factories.length; i++) {
                 int[][] productionPerDay = extractArray(helpArray, days, goods, i);
                 factories[i].setProduction(productionPerDay);
             }
             line = skipLines(sc);
             helpArray = loadArray(sc, line, goods*days, supermarkets.length);
             for(int i = 0; i < supermarkets.length; i++) {
                 int[][] demandPerDay = extractArray(helpArray, days, goods, i);
                 supermarkets[i].setDemand(demandPerDay);
             }
             System.out.println(line);

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
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

    String skipLines(Scanner sc) {
        String line;
        while((line = sc.nextLine()).equals("") || (line.charAt(0) == '#')) {}
        return line;
    }

    int[][] loadArray(Scanner sc, String line, int a, int b) {
        int[][] array = new int[a][b];
        for(int i = 0; i < array.length; i++) {
            if(line.equals("") || line.charAt(0) == '#') {
                i--;
                line = sc.nextLine();
                continue;
            }
            String[] lineSplit = line.split(" ");
            for(int o = 0; o < lineSplit.length; o++) {
                array[i][o] = Integer.parseInt(lineSplit[o]);
            }
            if(sc.hasNextLine())
                line = sc.nextLine();
        }
        return array;
    }
}
