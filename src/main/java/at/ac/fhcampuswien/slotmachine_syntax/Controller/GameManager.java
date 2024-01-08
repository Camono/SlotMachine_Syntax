package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;

import java.util.*;

public class GameManager {

    //variables
    private int currentBetIndex;
    private double balance;

    private List<Integer> betRange;

    //Wahrscheinlichkeit dass das zuerst gewählte Symbol noch 2 mal erscheint
    private final double DEFAULT_CHANCE_OF_X3 = 0.2;
    //Wahrscheinlichkeit dass das zuerst gewählte Symbol noch 3 mal erscheint

    private final double DEFAULT_CHANCE_OF_X4 = 0.15;
    //Wahrscheinlichkeit dass das zuerst gewählte Symbol noch 4 mal erscheint

    //constructors
    public GameManager(double balance) {
        this.balance = balance;
        this.betRange = Arrays.asList(1, 2, 5, 10, 20, 50, 100); //not part of constructor as it's hardcoded during runtime init.
        this.currentBetIndex = 0;
    }


    public void initialize() {

    }

    /**
     * the below methods are used to in/decrease your current bet to the next higher/lower one.
     * e.g. from 10 to 5 credits per spin or vice versa.
     */

    public int decreaseBet() {
        if (currentBetIndex > 0) {
            currentBetIndex--;
        }

        return betRange.get(currentBetIndex);
    }

    public int increaseBet() {
        if (currentBetIndex < betRange.size() -1) {
            currentBetIndex++;
        }

        return betRange.get(currentBetIndex);
    }

    public List<Symbol> createSpinResult() {
        List<Symbol> result = new ArrayList<>();
        Symbol firstSymbol = pickRandomSymbol(null);
        result.add(firstSymbol);

        //braucht extra logik für wild
        if (!firstSymbol.isWild()) {
            //Chancen auf Gewinne sind abhängig von ihrem Auszahlungsmultiplikator
            //"Schlechte" Symbole hätten eine zu hohe Chance auf Gewinn wegen ihrem schlechten Multiplikator
            //Deshalb gibt es eigene Berechnungen für X3 und X4
            double chanceOfX3 = 1 / firstSymbol.getMultiplierX3() * 0.70;
            if (firstSymbol.getMultiplierX3() < 1.6) {
                chanceOfX3 = DEFAULT_CHANCE_OF_X3;
            }
            double chanceOfX4 = 1 / firstSymbol.getMultiplierX4() * 0.55;
            if (firstSymbol.getMultiplierX4() < 1) {
                chanceOfX4 = DEFAULT_CHANCE_OF_X4;
            }

            //Die Chance auf Fullscreen ist abhängig vom möglichen Auszahlungsmultiplikator * 0.8
            //zB wenn eine Kombination einen Auszahlungsfaktor von x100 hat darf die Chance dass diese Kombination eintritt nicht 1/100 = 1% sein sondern muss geringer sein -> house edge
            double chanceOfX5 = 1 / firstSymbol.getMultiplierX5() * 0.8;

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
        //Wenn wir ein Symbol nicht mehr zufällig generieren wollen für den Fall eines Lose
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
