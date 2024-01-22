package at.ac.fhcampuswien.slotmachine_syntax;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import at.ac.fhcampuswien.slotmachine_syntax.Util.JsonDataLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonDataLoaderTest {

    private static List<Symbol> symbols;

    @BeforeAll
    public static void setUp() {
        InputStream testInputStream = JsonDataLoaderTest.class.getResourceAsStream("/test_win_symbols.json");
        assertNotNull(testInputStream);
        symbols = JsonDataLoader.getAllSymbolsFromJSON(testInputStream);
    }

    @Test
    void testListIsEmpty() {
        symbols.clear();
        symbols = JsonDataLoader.getAllSymbolsFromJSON(null);
        assertTrue(symbols.isEmpty(), "List should not be empty");
    }

    @Test
    void testListNotNullOrEmpty() {
        assertNotNull(symbols, "List should not be null");
        assertFalse(symbols.isEmpty(), "List should not be empty");
        assertEquals(5, symbols.size(), "There should be 8 symbols");
    }

    @Test
    void testFirstSymbolProperties() {
        assertEquals(SymbolType.LUGNER, symbols.get(0).getSymbolType());
        assertEquals("/symbols/Lugner.png", symbols.get(0).getImagePath());
        assertEquals(0.03, symbols.get(0).getAppearFactor());
    }

}


