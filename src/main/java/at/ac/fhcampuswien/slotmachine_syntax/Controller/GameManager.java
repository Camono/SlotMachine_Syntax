package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;

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
        //erstes Symbol darf nicht wild sein (vorerst)
        Symbol firstSymbol = pickRandomSymbol(List.of(StaticGamedata.WILD));
        result.add(firstSymbol);

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
            //an der letzten Stelle darf kein Gewinnsymbol oder Wildsymbol mehr generiert werden
            //sonst wäre möglich dass eine Gewinnkombination angezeigt wird die nicht mit dem Multiplikator übereinstimmt
            result.addAll(Arrays.asList(firstSymbol, firstSymbol, firstSymbol, pickRandomSymbol(List.of(firstSymbol, StaticGamedata.WILD))));
            return result;
        }

        if (chanceOfX3 > randomValue) {
            //add 2 more same symbols and 2 random symbols
            //an der letzten und vorletzten Stelle darf kein Gewinnsymbol oder Wildsymbol mehr generiert werden
            //sonst wäre möglich dass eine Gewinnkombination angezeigt wird die nicht mit dem Multiplikator übereinstimmt
            result.addAll(Arrays.asList(firstSymbol, firstSymbol, pickRandomSymbol(List.of(firstSymbol, StaticGamedata.WILD)), pickRandomSymbol(List.of(firstSymbol, StaticGamedata.WILD))));
            return result;
        } else {
            //4 random symbols = no win
            //das zweite zufällig generierte symbol darf weder wild noch das erste vorkommende Symbol sein
            //sonst wäre möglich dass eine Gewinnkombination angezeigt wird die nicht mit dem Multiplikator übereinstimmt
            result.addAll(Arrays.asList(pickRandomSymbol(List.of(firstSymbol, StaticGamedata.WILD)), pickRandomSymbol(Collections.emptyList()), pickRandomSymbol(Collections.emptyList()), pickRandomSymbol(Collections.emptyList())));
            return result;
        }

    }

    public Symbol pickRandomSymbol(List<Symbol> symbolsToExclude) {
        List<Symbol> elements = new ArrayList<>(StaticGamedata.getAllSymbols());
        //Wenn wir ein oder mehr Symbol ausschließen wollen aus der Zufallswahl
        if (symbolsToExclude != null && !symbolsToExclude.isEmpty()) {
            elements.removeAll(symbolsToExclude);
        }

        //Liste muss zufällig angeordnet werden
        Collections.shuffle(elements);

        //zufälliger wert von 0.0 bis 1.0 wird generiert
        Random random = new Random();
        double randomValue = random.nextDouble();

        //die kumulative Wahrscheinlichkeit sorgt dafür das die appearChance von allen Symbolen berücksichtigt wird
        double cumulativeProbability = 0.0;
        for (Symbol symbol : StaticGamedata.getAllSymbols()) {
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
            double multiplicator = 0.0;

            switch (counter) {
                case 2:
                    multiplicator = firstSymbol.getMultiplierX3();
                    break;
                case 3:
                    multiplicator = firstSymbol.getMultiplierX4();
                    break;
                case 4:
                    multiplicator = firstSymbol.getMultiplierX5();
                    break;
            }
            balance = balance + (betRange.get(currentBetIndex) * multiplicator);
            return new GameResult(multiplicator * betRange.get(currentBetIndex), balance, spinResult);
        } else {
            // wenn der counter 1 oder niedriger ist, ist das Symbol maximal 2 mal erschienen von Links nach rechts
            //somit gibt es keinen gewinn
            balance = balance - betRange.get(currentBetIndex);
            return new GameResult(0.0, balance, spinResult);
        }
    }

}
