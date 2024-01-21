package at.ac.fhcampuswien.slotmachine_syntax;

import at.ac.fhcampuswien.slotmachine_syntax.controller.GameManager;
import at.ac.fhcampuswien.slotmachine_syntax.model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.model.SymbolType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
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
        double moneyFromWins = 0.0;
        int winningSpins = 0;

        GameResult lastGameResult = new GameResult(0.0, 0.0, null);
        for (int i = 0; i < SIMULATE_SPINS_AMOUNT; i++) {
            List<Symbol> spinResult = manager.createSpinResult();
            GameResult gameResult = manager.calculateWinnings(spinResult);
            moneyFromWins = moneyFromWins + gameResult.getProfit();

            if (gameResult.getProfit() > 0.0) {
                winningSpins++;
            }
            lastGameResult = gameResult;
        }

        int totalBetSum = TEST_BET_AMOUNT * SIMULATE_SPINS_AMOUNT;
        System.out.println("Runden gespielt: " + SIMULATE_SPINS_AMOUNT);
        System.out.println("Runden gewonnen: " + winningSpins);
        System.out.println("Gesamter Einsatz: " + totalBetSum);
        System.out.println("Kontostand: " + lastGameResult.getNewBalance());

        //der Kontostand nach 10000 (bzw SIMULATE_SPINS_AMOUNT) Spielen darf nie höher sein als davor -> House Edge
        boolean loser = lastGameResult.getNewBalance() < totalBetSum;
        assertTrue(loser);
    }

    @Test
    public void testRandomDistribution() {
        List<Symbol> symbols = new ArrayList<>();
        GameManager manager = new GameManager(500);

        //randomly pick symbols up to RANDOM_DISTRIBUTION_QUANTITY
        for (int i = 0; i < RANDOM_DISTRIBUTION_QUANTITY; i++) {
            symbols.add(manager.pickRandomSymbol(Collections.emptyList()));
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
                () -> assertTrue(countU1 / factor >= manager.getSymbol(SymbolType.U1).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countU1 / factor <= manager.getSymbol(SymbolType.U1).getAppearFactor() * 100 + maxDeviation)
        );

        //countU6 must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countU6 / factor >= manager.getSymbol(SymbolType.U6).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countU6 / factor <= manager.getSymbol(SymbolType.U6).getAppearFactor() * 100 + maxDeviation)
        );

        //countREDBULL must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countREDBULL / factor >= manager.getSymbol(SymbolType.RED_BULL).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countREDBULL / factor <= manager.getSymbol(SymbolType.RED_BULL).getAppearFactor() * 100 + maxDeviation)
        );

        //countMARLBORO must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countMARLBORO / factor >= manager.getSymbol(SymbolType.MARLBORO).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countMARLBORO / factor <= manager.getSymbol(SymbolType.MARLBORO).getAppearFactor() * 100 + maxDeviation)
        );

        //countGIS must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countGIS / factor >= manager.getSymbol(SymbolType.GIS).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countGIS / factor <= manager.getSymbol(SymbolType.GIS).getAppearFactor() * 100 + maxDeviation)
        );

        //countLUGNER must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countLUGNER / factor >= manager.getSymbol(SymbolType.LUGNER).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countLUGNER / factor <= manager.getSymbol(SymbolType.LUGNER).getAppearFactor() * 100 + maxDeviation)
        );

        //countWILD must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countWILD / factor >= manager.getSymbol(SymbolType.WILD).getAppearFactor() * 100 - maxDeviation),
                () -> assertTrue(countWILD / factor <= manager.getSymbol(SymbolType.WILD).getAppearFactor() * 100 + maxDeviation)
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
