package at.ac.fhcampuswien.slotmachine_syntax.Util;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonDataLoader {
    public static List<Symbol> getAllSymbolsFromJSON() {
        InputStream is = JsonDataLoader.class.getResourceAsStream("/symbols.json");
        if (is == null) {
            return Collections.emptyList();
        }

        List<Symbol> result = new ArrayList<>();

        JSONTokener tokenizer = new JSONTokener(is);
        JSONObject root = new JSONObject(tokenizer);
        root.getJSONArray("symbols").forEach(symbol ->
            result.add(mapJSONtoSymbol((JSONObject) symbol))
        );

        return result;
    }

    private static Symbol mapJSONtoSymbol(JSONObject jsonSymbol) {
        return new Symbol(jsonSymbol.getString("imagePath"),
                SymbolType.valueOf(jsonSymbol.getString("symbolType").toUpperCase()),
                jsonSymbol.getDouble("appearFactor"),
                jsonSymbol.getDouble("multiplierX3"),
                jsonSymbol.getDouble("multiplierX4"),
                jsonSymbol.getDouble("multiplierX5"),
                jsonSymbol.getBoolean("isWild"),
                jsonSymbol.getBoolean("isFreeSpin"));
    }
}
