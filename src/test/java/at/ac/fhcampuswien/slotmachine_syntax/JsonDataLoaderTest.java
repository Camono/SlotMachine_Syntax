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
        InputStream testInputStream = JsonDataLoaderTest.class.getResourceAsStream("/symbols.json");
        assertNotNull(testInputStream);
        symbols = JsonDataLoader.getAllSymbolsFromJSON(testInputStream);
    }

    @Test
    void testListNotNullOrEmpty() {
        assertNotNull(symbols, "List should not be null");
        assertFalse(symbols.isEmpty(), "List should not be empty");
        assertEquals(7, symbols.size(), "There should be 8 symbols");
    }

    @Test
    void testFirstSymbolProperties() {
        assertEquals(SymbolType.U1, symbols.get(0).getSymbolType());
        assertEquals("/symbols/U1.png", symbols.get(0).getImagePath());
        assertEquals(0.25, symbols.get(0).getAppearFactor());
    }

}


