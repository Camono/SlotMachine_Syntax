package at.ac.fhcampuswien.slotmachine_syntax.controller;

import at.ac.fhcampuswien.slotmachine_syntax.model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.util.JsonDataLoader;

import java.util.*;

public class GameManager {

    //variables
    private int currentBetIndex;
    private double balance;
    private final List<Integer> betRange;
    private final List<Symbol> allSymbols;


    //Wahrscheinlichkeit, dass das zuerst gewählte Symbol noch 2x erscheint
    private static final double DEFAULT_CHANCE_OF_X3 = 0.33;
    //Wahrscheinlichkeit, dass das zuerst gewählte Symbol noch 3x erscheint
    private static final double DEFAULT_CHANCE_OF_X4 = 0.17;


    //constructors
    public GameManager(double balance) {
        this.balance = balance;
        //betting possibilities are predefined
        this.betRange = Arrays.asList(1, 2, 5, 10, 20, 50, 100);
        //10 Credits pro Spin
        this.currentBetIndex = 3;
        allSymbols = JsonDataLoader.getAllSymbolsFromJSON();
    }

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
        //erstes Symbol darf nicht wild sein
        Symbol firstSymbol = pickRandomSymbol(List.of(getSymbol(SymbolType.WILD)));
        result.add(firstSymbol);

        //Chancen auf große Gewinne sind abhängig von ihrem Auszahlungsmultiplikator
        //Die Chance auf Fullscreen ist abhängig vom möglichen Auszahlungsmultiplikator * 0.8
        //zB wenn eine Kombination einen Auszahlungsfaktor von x100 hat, darf die Chance, dass diese Kombination eintritt nicht 1/100 = 1% sein, sondern muss geringer sein -> house edge
        double chanceOfX5 = getChanceOfX5(firstSymbol);

        double randomValue = getRandomValue();

        if (chanceOfX5 > randomValue) {
            //add 4 more same symbols to the spin result
            result.addAll(Arrays.asList(firstSymbol, firstSymbol, firstSymbol, firstSymbol));
            return result;
        }

        if (DEFAULT_CHANCE_OF_X4 > randomValue) {
            //add 3 more same symbols and 1 random symbol
            //an der letzten Stelle darf kein Gewinnsymbol oder Wildsymbol mehr generiert werden
            //sonst wäre möglich dass eine Gewinnkombination angezeigt wird die nicht mit dem Multiplikator übereinstimmt
            result.addAll(Arrays.asList(firstSymbol, firstSymbol, firstSymbol, pickRandomSymbol(List.of(firstSymbol, getSymbol(SymbolType.WILD)))));
            return result;
        }

        if (DEFAULT_CHANCE_OF_X3 > randomValue) {
            //add 2 more same symbols and 2 random symbols
            //an der letzten und vorletzten Stelle darf kein Gewinnsymbol oder Wildsymbol mehr generiert werden
            //sonst wäre möglich dass eine Gewinnkombination angezeigt wird die nicht mit dem Multiplikator übereinstimmt
            result.addAll(Arrays.asList(firstSymbol, firstSymbol, pickRandomSymbol(List.of(firstSymbol, getSymbol(SymbolType.WILD))), pickRandomSymbol(List.of(firstSymbol, getSymbol(SymbolType.WILD)))));
        } else {
            //4 random symbols = no win
            //das zweite zufällig generierte symbol darf weder wild noch das erste vorkommende Symbol sein
            //sonst wäre möglich dass eine Gewinnkombination angezeigt wird die nicht mit dem Multiplikator übereinstimmt
            result.addAll(Arrays.asList(pickRandomSymbol(List.of(firstSymbol, getSymbol(SymbolType.WILD))), pickRandomSymbol(Collections.emptyList()), pickRandomSymbol(Collections.emptyList()), pickRandomSymbol(Collections.emptyList())));
        }

        return result;
    }


    public Symbol pickRandomSymbol(List<Symbol> symbolsToExclude) {
        List<Symbol> elements = new ArrayList<>(allSymbols);
        //Wenn wir ein oder mehr Symbol ausschließen wollen aus der Zufallswahl
        if (!symbolsToExclude.isEmpty()) {
            elements.removeAll(symbolsToExclude);
        }

        //Liste muss zufällig angeordnet werden
        Collections.shuffle(elements);

        //zufälliger wert von 0.0 bis 1.0 wird generiert
        double randomValue = getRandomValue();

        //die kumulative Wahrscheinlichkeit sorgt dafür das die appearChance von allen Symbolen berücksichtigt wird
        double cumulativeProbability = 0.0;
        for (Symbol symbol : elements) {
            //die appearChance jedes Symbols erhöht die kumulative Wahrscheinlichkeit bis sie einen Wert erreicht der höher ist als die Random Value
            //jene appearChance die den Ausschlag gibt ist dem Symbol zugeordnet das gewählt wird
            cumulativeProbability = cumulativeProbability + symbol.getAppearFactor();
            if (randomValue < cumulativeProbability) {
                return symbol;
            }
        }

        // Shouldn't reach here, but return the last element just in case
        return elements.get(elements.size() - 1);
    }

    private double getRandomValue() {
        Random random = new Random();
        return random.nextDouble();
    }

    public GameResult calculateWinnings(List<Symbol> spinResult) {
        //das erste Symbol ist ausschlaggebend für das Endergebnis
        Symbol firstSymbol = spinResult.get(0);

        //kein gewinn
        if (!spinResult.get(1).equals(firstSymbol) && !spinResult.get(1).getSymbolType().equals(SymbolType.WILD)) {
            balance = balance - betRange.get(currentBetIndex);
            return new GameResult(0.0, balance, spinResult);
        }

        int counter = 0;

        //zählt wie oft ein Symbol von Links nach Rechts vorkommt = Gewinnbedingung
        for (int i = 1; i < spinResult.size(); i++) {
            if (spinResult.get(i).equals(firstSymbol) || spinResult.get(i).isWild()) {
                counter++;
            }
        }

        //wenn der counter 2 oder größer ist, gibt es mindestens 3 gleiche symbole von links nach rechts
        if (counter > 1) {
            double winSum = getWinSum(counter, firstSymbol);
            balance = balance - betRange.get(currentBetIndex) + winSum;
            return new GameResult(winSum, balance, spinResult);
        } else {
            // wenn der counter 1 oder niedriger ist, ist das Symbol maximal 2 mal erschienen von Links nach rechts
            //somit gibt es keinen gewinn
            balance = balance - betRange.get(currentBetIndex);
            return new GameResult(0.0, balance, spinResult);
        }
    }

    private double getWinSum(int counter, Symbol firstSymbol) {
        //map the number of appearances of the symbol from left to right to the correct payout multiplicator
        double multiplicator = switch (counter) {
            case 2 -> firstSymbol.getMultiplierX3();
            case 3 -> firstSymbol.getMultiplierX4();
            case 4 -> firstSymbol.getMultiplierX5();
            default -> 0.0;
        };

        //bet from the betRange List multiplied with the multiplicator is the win sum
        return betRange.get(currentBetIndex) * multiplicator;
    }

    private double getChanceOfX5(Symbol firstSymbol) {
        double chanceOfX5 = 1 / firstSymbol.getMultiplierX5() * 0.8;
        //Chance of fullscreen is max 10%
        if (chanceOfX5 > 0.1) {
            chanceOfX5 = 0.1;
        }
        return chanceOfX5;
    }

    public Symbol getSymbol(SymbolType symbolType) {
        return allSymbols.stream()
                .filter(symbol -> symbol.getSymbolType() == symbolType)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("This symbol is not existing: " + symbolType.toString()));
    }

    public int getBet() {
        return betRange.get(currentBetIndex);
    }

    public double getBalance() { return balance; }
}
