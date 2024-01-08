package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;

import java.util.*;

public class GameManager {

    //variables
    private int bet;
    private double balance;
    private List<Integer> betRange;



    //constructors


    public GameManager(double balance) {
        this.balance = balance;
        this.betRange = Arrays.asList(1,2,5,10,20,50,100); //not part of constructor as it's hardcoded during runtime init.
    }


    public void initialize() {

    }

    /**
     * the below methods are used to in/decrease your current bet to the next higher/lower one.
     * e.g. from 10 to 5 credits per spin or vice versa.
     */
    public int decreaseBet() {

        return 0;
    }

    public int increaseBet() {

        return 0;
    }

    public List<Symbol> createSpinResult() {
        List<Symbol> result = new ArrayList<>();
        Symbol firstSymbol = pickRandomSymbol(null);
        result.add(firstSymbol);

        //braucht extra logik für wild
        if (!firstSymbol.isWild()) {
            double chanceOfX3 = 1/firstSymbol.getMultiplierX3()*0.95;
            if (firstSymbol.getMultiplierX3() < 1.6) {
                chanceOfX3 = 0.38;
            }
            double chanceOfX4 = 1/firstSymbol.getMultiplierX4()*0.7;
            if (firstSymbol.getMultiplierX4() < 1) {
                chanceOfX4 = 0.2;
            }
            double chanceOfX5 = 1/firstSymbol.getMultiplierX5()*0.8;

            Random random = new Random();
            double randomValue = random.nextDouble();

            if (chanceOfX5 > randomValue) {
                //add 4 more same symbols to the spin result
                result.addAll(Arrays.asList(firstSymbol, firstSymbol, firstSymbol, firstSymbol));
                return result;
            }

            if (chanceOfX4 > randomValue) {
                //add 3 more same symbols and 1 random symbol
                result.addAll(Arrays.asList(firstSymbol, firstSymbol, firstSymbol, pickRandomSymbol(firstSymbol)));
                return result;
            }

            if (chanceOfX3 > randomValue) {
                //add 2 more same symbols and 2 random symbols
                result.addAll(Arrays.asList(firstSymbol, firstSymbol, pickRandomSymbol(firstSymbol), pickRandomSymbol(firstSymbol)));
                return result;
            } else {
                //4 random symbols = no win
                result.addAll(Arrays.asList(pickRandomSymbol(firstSymbol), pickRandomSymbol(firstSymbol), pickRandomSymbol(firstSymbol), pickRandomSymbol(firstSymbol)));
                return result;
            }

        } else {
            return null;
        }
    }

    public Symbol pickRandomSymbol(Symbol symbolToExclude) {
        List<Symbol> elements = new ArrayList<>(StaticGamedata.getAllSymbols());
        if (symbolToExclude != null) {
            elements.remove(symbolToExclude);
        }

        Collections.shuffle(elements);

        Random random = new Random();
        double randomValue = random.nextDouble();

        double cumulativeProbability = 0.0;
        for (Symbol symbol : StaticGamedata.getAllSymbols()) {
            cumulativeProbability += symbol.getAppearFactor();
            if (randomValue < cumulativeProbability) {
                return symbol;
            }
        }

        // Shouldn't reach here, but return the last element just in case
        return elements.get(elements.size() - 1);
    }
}
