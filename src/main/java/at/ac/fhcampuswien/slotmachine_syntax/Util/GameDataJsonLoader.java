package at.ac.fhcampuswien.slotmachine_syntax.Util;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;

public class GameDataJsonLoader {

    private final List<Symbol> symbols;

    public GameDataJsonLoader() {
        symbols = new ArrayList<>();
        initializeJson();
    }

    private void initializeJson() {
        InputStream is = getClass().getResourceAsStream("/symbols.json");
        if(is == null){
            return;
        }

        JSONTokener tokenizer = new JSONTokener(is);
        JSONObject root = new JSONObject(tokenizer);
        loadSymbols(root.getJSONArray("symbols"));
    }

    private void loadSymbols(JSONArray symbolsArray) {
        List<Symbol> loadedSymbols = new ArrayList<>();
        IntStream.range(0, symbolsArray.length()).forEach(i -> {
            try {
                JSONObject jsonSymbol = symbolsArray.getJSONObject(i);
                Symbol symbol = new Symbol(jsonSymbol.getString("imagePath"),
                        SymbolType.valueOf(jsonSymbol.getString("symbolType").toUpperCase()),
                        jsonSymbol.getDouble("appearFactor"),
                        jsonSymbol.getDouble("multiplierX3"),
                        jsonSymbol.getDouble("multiplierX4"),
                        jsonSymbol.getDouble("multiplierX5"),
                        jsonSymbol.getBoolean("isWild"),
                        jsonSymbol.getBoolean("isFreeSpin")
                );
                loadedSymbols.add(symbol);
            } catch (JSONException e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
            }
        });
        symbols.addAll(loadedSymbols);
    }

    public Symbol getSymbol(SymbolType symbolType) {
        return symbols.stream()
                .filter(symbol -> symbol.getSymbolType() == symbolType)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("This symbol is not existing: "+symbolType.toString()));
    }

    public String getImageByName(SymbolType symbolType) {
        return symbols.stream()
                .filter(symbol -> symbol.getSymbolType() == symbolType)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("This image is not existing: "+ symbolType.toString())).getImagePath();
    }

    public List<Symbol> getAllSymbols() {
        return symbols.stream().toList();
    }

}
