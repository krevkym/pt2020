
import java.io.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Trida pro praci se souborem
 * @author Marek Křevký a Robin Kříž
 */
public class FileIO {
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
     * Vrati pocet druhu zbozi
     * @return int hodnota
     */
    public int getGoods() {
        return goods;
    }

    /**
     * Vrati ceny prevozu zbozi
     * @return 2D int pole
     */
    public int[][] getCost() {
        return cost;
    }

    /**
     * Vrati tovarny
     * @return pole intu
     */
    public Factory[] getFactories() {
        return factories;
    }

    /**
     * Vrati supermarkety
     * @return pole intu
     */
    public Supermarket[] getSupermarkets() {
        return supermarkets;
    }

    /**
     * Vrati pocet dnu simulace
     * @return int hodnota
     */
    public int getDays() {
        return days;
    }

    /**
     * Nacte soubor, vytvori datove struktury a naplni je
     * @param file soubor z ktereho se nacita
     * @throws NumberFormatException Pokud se nepovedou nacist data
     */
    public void loadFromFile(File file) throws NumberFormatException {
        try {
            Scanner sc = new Scanner(file);
            String line = skipLines(sc);

            String[] data = line.split(" ");
            try {
                factories = new Factory[Integer.parseInt(data[0])];
                supermarkets = new Supermarket[Integer.parseInt(data[1])];
                goods = Integer.parseInt(data[2]);
                days = Integer.parseInt(data[3]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
            for(int i = 0; i < factories.length; i++) {
                factories[i] = new Factory();
            }
            for(int i = 0; i < supermarkets.length; i++) {
                supermarkets[i] = new Supermarket();
            }
            int[] blockHeights = new int[]{factories.length, goods, goods*days, goods *days};
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
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Preskoci nezadouci radky pri cteni souboru
     * @param sc Scanner na cteni
     * @return zadouci radka
     */
    private String skipLines(Scanner sc) {
        String line = "";
        while(sc.hasNextLine()) {
            if (!(line = sc.nextLine()).equals("") && (line.charAt(0) != '#')) {
                break;
            }
        }
        return line;
    }

    /**
     * Zpracuje pole do pozadovaneho formatu
     * @param array zpracovavane pole
     * @param a offset
     * @param b offset
     * @param i index tovarny nebo supermarketu
     * @return 2D pole intu
     */
    private int[][] extractArray(int[][] array, int a, int b, int i) {
        int counter = 0;
        int[][] data = new int[a][b];
        for(int o = 0; o < a; o++) {
            for(int p = o; p < b*a; p+=a) {
                data[o][counter] = array[p][i];
                counter++;
            }
            counter=0;
        }
        return data;
    }

    /**
     * Nacte block ze souboru
     * @param sc Scanner pro nacteni
     * @param line aktualni radka
     * @param a vyska bloku
     * @param b sirka bloku
     * @return 2D pole nactenych intu
     */
    private int[][] loadArray(Scanner sc, String line, int a, int b) {
        int[][] array = new int[a][b];
        for(int i = 0; i < array.length; i++) {
            if(line.equals("") || line.charAt(0) == '#') {
                i--;
                line = sc.nextLine();
                continue;
            }
            //v real_small_sink.txt jsou cisla v poslednim sloupci v poptavce oddeleny dvemi mezerami.. takze celkem neprijemny split
            String[] lineSplit = line.split(" {2}| ");
            for(int o = 0; o < lineSplit.length; o++) {
                array[i][o] = Integer.parseInt(lineSplit[o]);
            }
            if(sc.hasNextLine()) {
                line = sc.nextLine();
            }
        }
        return array;
    }

    /**
     * Vygeneruje pseudonahodne cislo s normalnim rozdelenim
     * @param min minimalni hodnota vygenerovaneho cisla
     * @param max maximalni hodnota vygenerovaneho cisla
     * @return pseudonahodny int
     */
    public int generateNumber(int min, int max) {
        Random rd = new Random();
        int number;
        do{
            number = (int)(Math.round(rd.nextGaussian()*(max-min)/5)+((max+min)/2.0));
        } while(number > max || number < min);
        return number;
    }

    /**
     * Vygeneruje data
     * @param file soubor do ktereho se data ulozi
     * @param data data pro nastaveni generace
     */
    public void generateData(File file, int[] data) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            String countData = data[0]+" "+data[1]+" "+data[2]+" "+data[3];
            bw.write(countData);
            bw.newLine();
            int[] blockHeights = new int[]{data[0], data[2], data[2]*data[3], data[2]*data[3]};
            int[] blockWidths = new int[]{data[1], data[1], data[0], data[1]};
            for(int i = 4; i < 12; i+=2) {
                writeBlock(bw, data[i], data[i+1], blockHeights[((i-2)/2)-1], blockWidths[((i-2)/2)-1]);
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zapise do souboru blok dat
     * @param bw stream pro zapisovani do souboru
     * @param min minimalni hodnota pro blok
     * @param max maximalni hodnota pro blok
     * @param blockHeight vyska bloku
     * @param blockWidth sirka bloku
     * @throws IOException pri chybe se souborem
     */
    private void writeBlock(BufferedWriter bw, int min, int max, int blockHeight, int blockWidth) throws IOException {
        for(int i = 0; i < blockHeight; i++) {
            for(int o = 0; o < blockWidth; o++) {
                bw.write(generateNumber(min, max)+" ");
            }
            bw.newLine();
        }
    }
}
