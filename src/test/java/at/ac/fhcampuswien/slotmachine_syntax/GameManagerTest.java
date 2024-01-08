package at.ac.fhcampuswien.slotmachine_syntax;

import at.ac.fhcampuswien.slotmachine_syntax.Controller.GameManager;
import at.ac.fhcampuswien.slotmachine_syntax.Controller.StaticGamedata;
import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.util.Tuple;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameManagerTest {

    private final Integer TEST_BET_AMOUNT = 1;
    private final Integer SIMULATE_SPINS_AMOUNT = 10000;
    private final Integer RANDOM_DISTRIBUTION_QUANTITY = 500000;
    private final Integer START_CREDITS = 1000;

    @Test
    public void testSingleSpin() {
        GameManager manager = new GameManager(START_CREDITS);
        GameResult gameResult = manager.calculateWinnings(manager.createSpinResult());
        Symbol symbol1 = gameResult.getSymbols().get(0);
        Symbol symbol2 = gameResult.getSymbols().get(1);
        Symbol symbol3 = gameResult.getSymbols().get(2);
        Symbol symbol4 = gameResult.getSymbols().get(3);
        Symbol symbol5 = gameResult.getSymbols().get(4);
        System.out.println(symbol1.toString() + "  " + symbol2.toString() + "  " + symbol3.toString() + "  " + symbol4.toString() + "  " + symbol5.toString());
        System.out.println(gameResult.getProfit() + " Gewinn");
    }

    @Test
    public void testMoneyReturn() {
        GameManager manager = new GameManager(START_CREDITS);
        List<Tuple<Symbol, Long>> spinRecordList = new ArrayList<>();

        double moneyFromWins = 0.0;

        GameResult last = new GameResult(0.0, 0.0, null);
        for (int i = 0; i < SIMULATE_SPINS_AMOUNT; i++) {
            List<Symbol> spinResult = manager.createSpinResult();
            GameResult gameResult = manager.calculateWinnings(spinResult);
            moneyFromWins = moneyFromWins + gameResult.getProfit();
            //only adds the winning spins to the record list
            addToList(spinResult, spinRecordList);
            last = gameResult;
        }

        int totalBetSum = TEST_BET_AMOUNT * SIMULATE_SPINS_AMOUNT;
        System.out.println("Runden gespielt: " + SIMULATE_SPINS_AMOUNT);
        System.out.println("Runden gewonnen: " + spinRecordList.size());
        System.out.println("Gesamter Einsatz: " + totalBetSum);
        System.out.println("Kontostand: " + last.getNewBalance());
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




    @Test
    public void testRandomDistribution() {
        List<Symbol> symbols = new ArrayList<>();
        GameManager manager = new GameManager(500);

        //randomly pick symbols up to RANDOM_DISTRIBUTION_QUANTITY
        for (int i = 0; i < RANDOM_DISTRIBUTION_QUANTITY; i++) {
            symbols.add(manager.pickRandomSymbol(null));
        }

        //count every symbol with streams
        long countU1 = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.U1).count();
        long countU6 = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.U6).count();
        long countREDBULL = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.RED_BULL).count();
        long countMARLBORO = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.MARLBORO).count();
        long countGIS = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.GIS).count();
        long countLUGNER = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.LUGNER).count();
        long countWILD = symbols.stream().filter(symbol -> symbol.getSymbolType() == SymbolType.WILD).count();

        //faktor um Symbol Erscheinung in % zu ermitteln
        double factor = RANDOM_DISTRIBUTION_QUANTITY / 100.0;

        //Toleranz der Abweichung
        double maxDeviation = 0.25;

        //countU1 must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countU1 / factor >= StaticGamedata.U1.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countU1 / factor <= StaticGamedata.U1.getAppearFactor() * 100 + maxDeviation)
        );

        //countU6 must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countU6 / factor >= StaticGamedata.U6.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countU6 / factor <= StaticGamedata.U6.getAppearFactor() * 100 + maxDeviation)
        );

        //countREDBULL must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countREDBULL / factor >= StaticGamedata.REDBULL.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countREDBULL / factor <= StaticGamedata.REDBULL.getAppearFactor() * 100 + maxDeviation)
        );

        //countMARLBORO must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countMARLBORO / factor >= StaticGamedata.MARLBORO.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countMARLBORO / factor <= StaticGamedata.MARLBORO.getAppearFactor() * 100 + maxDeviation)
        );

        //countGIS must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countGIS / factor >= StaticGamedata.GIS.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countGIS / factor <= StaticGamedata.GIS.getAppearFactor() * 100 + maxDeviation)
        );

        //countLUGNER must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countLUGNER / factor >= StaticGamedata.LUGNER.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countLUGNER / factor <= StaticGamedata.LUGNER.getAppearFactor() * 100 + maxDeviation)
        );

        //countWILD must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countWILD / factor >= StaticGamedata.WILD.getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countWILD / factor <= StaticGamedata.WILD.getAppearFactor() * 100 + maxDeviation)
        );

        System.out.println("U1: " + (double) countU1 / factor + "%");
        System.out.println("U6: " + (double) countU6 / factor + "%");
        System.out.println("REDBULL: " + (double) countREDBULL / factor + "%");
        System.out.println("MARLBORO: " + (double) countMARLBORO / factor + "%");
        System.out.println("GIS: " + (double) countGIS / factor + "%");
        System.out.println("LUGNER: " + (double) countLUGNER / factor + "%");
        System.out.println("WILD: " + (double) countWILD / factor + "%");

    }
}
