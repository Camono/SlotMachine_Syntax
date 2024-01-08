package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;

import java.util.Arrays;
import java.util.List;

public class StaticGamedata {


    public static final Symbol U1 = new Symbol(null, SymbolType.U1, 0.20, 0.5, 1.5, 2.5, false, false);
    public static final Symbol U6 = new Symbol(null, SymbolType.U6, 0.20, 0.5, 1.5, 2.5, false, false);
    public static final Symbol REDBULL = new Symbol(null, SymbolType.RED_BULL, 0.15, 1.0, 2.5, 7.5, false, false);
    public static final Symbol MARLBORO = new Symbol(null, SymbolType.MARLBORO, 0.14, 1.0, 3.5, 10.0, false, false);
    public static final Symbol GIS = new Symbol(null, SymbolType.GIS, 0.11, 1.5, 6.0, 15.3, false, false);
    public static final Symbol LUGNER = new Symbol(null, SymbolType.LUGNER, 0.1, 2.5, 10.0, 100.0, false, false);

    //to be finished in a later version - not mission critical for project
    public static final Symbol FEATURESYMBOL = new Symbol(null, SymbolType.FEATURE_SYMBOL, 0.0, 0, 0, 0, false, true);

    //has to be added to the first version of the game already!
    public static final Symbol WILD = new Symbol(null, SymbolType.WILD, 0.1, 0, 0, 0, true, false);


    public static List<Symbol> getAllSymbols() {

        return Arrays.asList(U1, U6, REDBULL, MARLBORO, GIS, LUGNER, FEATURESYMBOL, WILD);
    }

}
