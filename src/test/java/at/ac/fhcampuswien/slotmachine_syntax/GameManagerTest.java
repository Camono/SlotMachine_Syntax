package at.ac.fhcampuswien.slotmachine_syntax;

import at.ac.fhcampuswien.slotmachine_syntax.Controller.GameManager;
import at.ac.fhcampuswien.slotmachine_syntax.Model.GameResult;
import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.Util.JsonDataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    private final Integer SIMULATE_SPINS_AMOUNT = 10000;
    private final Integer RANDOM_DISTRIBUTION_QUANTITY = 500000;
    private static final Integer START_CREDITS = 1000;

    private static GameManager manager;


    @BeforeAll
    public static void setUp() {
        manager = new GameManager(START_CREDITS);
    }


    @Test
    void testSingleSpin() {
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
    void testHouseEdge() {
        //simulating 1000 x 10000 (with 1 unit bet amount) spins
        List<GameResult> allGameResults = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            GameManager gameManager = new GameManager(START_CREDITS);

            GameResult lastGameResult = new GameResult(0.0, 0.0, null);
            for (int j = 0; j < SIMULATE_SPINS_AMOUNT; j++) {
                List<Symbol> spinResult = gameManager.createSpinResult();

                lastGameResult = gameManager.calculateWinnings(spinResult);
            }
            //save latest game result as it contains the balance after 10000 games
            allGameResults.add(lastGameResult);
        }

        List<Double> balances = allGameResults
                .stream()
                .map(GameResult::getNewBalance)
                .sorted()
                .toList();

        double sumOfBalances = 0.0;
        int winners = 0;

        for (double value : balances) {
            //start credits relevant to determine winner
            if (value > 1000) {
                winners++;
            }
            sumOfBalances += value;
        }

        System.out.println("Gesamt Balance: " + sumOfBalances + "  |   Abzüglich Einsatz: " + (sumOfBalances - 1000 * START_CREDITS));
        System.out.println("Winner count: " + winners);
        System.out.println("Average balance after 10000 games starting with 1000 credits: " + sumOfBalances / 1000);
        System.out.println("Min balance after 10000 games starting with 1000 credits:" + balances.get(0));
        System.out.println("Max balance after 10000 games starting with 1000 credits: " + balances.get(999));
        System.out.println("Median balance after 10000 games starting with 1000 credits: " + balances.get(500));

        //haus vorteil ist gegeben wenn der durchschnittliche Kontostand aller Sitzungen geringer als das Startguthaben ist
        boolean houseEdge = START_CREDITS > sumOfBalances/1000;
        assertTrue(houseEdge);
    }

    @Test
    void testRandomDistribution() {
        List<Symbol> symbols = new ArrayList<>();
        manager = new GameManager(500);

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
        double factor = RANDOM_DISTRIBUTION_QUANTITY;

        //Toleranz der Abweichung
        double maxDeviation = 0.25;

        //countU1 must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countU1 / factor >= manager.getSymbol(SymbolType.U1).getAppearFactor() - maxDeviation),
                () -> assertTrue(countU1 / factor <= manager.getSymbol(SymbolType.U1).getAppearFactor() + maxDeviation)
        );

        //countU6 must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countU6 / factor >= manager.getSymbol(SymbolType.U6).getAppearFactor() - maxDeviation),
                () -> assertTrue(countU6 / factor <= manager.getSymbol(SymbolType.U6).getAppearFactor() + maxDeviation)
        );

        //countREDBULL must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countREDBULL / factor >= manager.getSymbol(SymbolType.RED_BULL).getAppearFactor() - maxDeviation),
                () -> assertTrue(countREDBULL / factor <= manager.getSymbol(SymbolType.RED_BULL).getAppearFactor() + maxDeviation)
        );

        //countMARLBORO must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countMARLBORO / factor >= manager.getSymbol(SymbolType.MARLBORO).getAppearFactor() - maxDeviation),
                () -> assertTrue(countMARLBORO / factor <= manager.getSymbol(SymbolType.MARLBORO).getAppearFactor() + maxDeviation)
        );

        //countGIS must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countGIS / factor >= manager.getSymbol(SymbolType.GIS).getAppearFactor() - maxDeviation),
                () -> assertTrue(countGIS / factor <= manager.getSymbol(SymbolType.GIS).getAppearFactor() + maxDeviation)
        );

        //countLUGNER must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countLUGNER / factor >= manager.getSymbol(SymbolType.LUGNER).getAppearFactor() - maxDeviation),
                () -> assertTrue(countLUGNER / factor <= manager.getSymbol(SymbolType.LUGNER).getAppearFactor() + maxDeviation)
        );

        //countWILD must appear within their appearChance +/- 0.25%
        assertAll(
                () -> assertTrue(countWILD / factor >= manager.getSymbol(SymbolType.WILD).getAppearFactor() - maxDeviation),
                () -> assertTrue(countWILD / factor <= manager.getSymbol(SymbolType.WILD).getAppearFactor() + maxDeviation)
        );

        System.out.println("U1: " + (double) countU1 / factor * 100 + "%");
        System.out.println("U6: " + (double) countU6 / factor * 100 + "%");
        System.out.println("REDBULL: " + (double) countREDBULL / factor * 100 + "%");
        System.out.println("MARLBORO: " + (double) countMARLBORO / factor * 100 + "%");
        System.out.println("GIS: " + (double) countGIS / factor * 100 + "%");
        System.out.println("LUGNER: " + (double) countLUGNER / factor * 100 + "%");
        System.out.println("WILD: " + (double) countWILD / factor * 100 + "%");

    }

    @Test
    void testCreateWinningsAfterNormalLose() {
        //arrange
        List<Symbol> spinResult = JsonDataLoader.getAllSymbolsFromJSON(JsonDataLoader.class.getResourceAsStream("/test_normal_lose_symbols.json"));

        //act
        GameResult gameResult = manager.calculateWinnings(spinResult);

        //assert
        assertNotSame(manager.getBalance(), gameResult.getNewBalance());
    }

    @Test
    void testCreateWinningsAfterLose() {
        //arrange
        List<Symbol> spinResult = JsonDataLoader.getAllSymbolsFromJSON(JsonDataLoader.class.getResourceAsStream("/test_lose_symbols.json"));

        //act
        GameResult gameResult = manager.calculateWinnings(spinResult);

        //assert
        assertNotSame(manager.getBalance(), gameResult.getNewBalance());
    }

    @Test
    void testCreateWinningsNotWild() {
        //arrange
        List<Symbol> spinResult = JsonDataLoader.getAllSymbolsFromJSON(JsonDataLoader.class.getResourceAsStream("/test_win_symbols.json"));
        double oldBalance = manager.getBalance();

        //act
        GameResult gameResult = manager.calculateWinnings(spinResult);

        //assert
        assertEquals(gameResult.getNewBalance() - oldBalance + manager.getBet(), gameResult.getProfit());
    }

    @Test
    void testIncreaseBet() {
        int initialBet = manager.getBet();
        manager.increaseBet();
        assertTrue(manager.getBet() > initialBet);
    }

    @Test
    void testDecreaseBet() {
        int initialBet = manager.getBet();
        manager.decreaseBet();
        assertTrue(manager.getBet() < initialBet);
    }

}
