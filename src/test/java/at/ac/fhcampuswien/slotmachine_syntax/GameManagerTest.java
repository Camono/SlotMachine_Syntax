package at.ac.fhcampuswien.slotmachine_syntax;

import at.ac.fhcampuswien.slotmachine_syntax.Controller.GameManager;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.util.Tuple;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GameManagerTest {

    private final Integer TEST_BET_AMOUNT = 1;
    private final Integer SIMULATE_SPINS_AMOUNT = 10000;

    @Test
    public void testMoneyReturn() {
        GameManager manager = new GameManager(500);
        List<Tuple<Symbol, Long>> spinRecordList = new ArrayList<>();

        for (int i = 0; i < SIMULATE_SPINS_AMOUNT; i++) {
            List<Symbol> spinResult = manager.createSpinResult();
            //only adds the winning spins to the record list
            addToList(spinResult, spinRecordList);
        }

        double moneyFromWins = 0.0;

        for (int i = 0; i < spinRecordList.size(); i++) {

            double multiplier = 0.0;

            if (spinRecordList.get(i).getSecond() == 3L) {
                multiplier = spinRecordList.get(i).getFirst().getMultiplierX3();
            }

            if (spinRecordList.get(i).getSecond() == 4L) {
                multiplier = spinRecordList.get(i).getFirst().getMultiplierX4();
            }

            if (spinRecordList.get(i).getSecond() == 5L) {
                multiplier = spinRecordList.get(i).getFirst().getMultiplierX4();
            }

            moneyFromWins = moneyFromWins + (TEST_BET_AMOUNT * multiplier);
        }

        int totalBetSum = TEST_BET_AMOUNT * SIMULATE_SPINS_AMOUNT;
        System.out.println("Runden gespielt: " + SIMULATE_SPINS_AMOUNT);
        System.out.println("Runden gewonnen: " + spinRecordList.size());
        System.out.println("Gesamter Einsatz: " + totalBetSum);
        System.out.println("Gesamtgewinn: " + moneyFromWins);
        System.out.println("Konotstand Änderung: " + (moneyFromWins - totalBetSum));
    }


    private void addToList(List<Symbol> spinResult, List<Tuple<Symbol, Long>> spinRecordList) {
        if (spinResult == null) {
            return;
        }

        Symbol firstSymbol = spinResult.get(0);

        if (!spinResult.get(1).equals(firstSymbol) && !spinResult.get(1).getSymbolType().equals(SymbolType.WILD)) {
            return;
        }

        int counter = 0;

        for (int i = 1; i < spinResult.size(); i++) {
            if (spinResult.get(i).equals(firstSymbol) || spinResult.get(i).isWild()) {
                counter++;
            }
        }

        if (counter > 1) {
            Tuple<Symbol, Long> result = new Tuple<>(firstSymbol, Long.valueOf(counter + 1));
            spinRecordList.add(result);
        }
    }

}
