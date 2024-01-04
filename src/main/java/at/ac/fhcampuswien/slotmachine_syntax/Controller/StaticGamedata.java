package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;

import java.util.Arrays;
import java.util.List;

public class StaticGamedata {


    public static final Symbol U1 = new Symbol(null, SymbolType.U1, 0.0, 0.15,0.5, 2.5, false, false);
    public static final Symbol U6 = new Symbol(null, SymbolType.U6, 0.0, 0.15,0.5, 2.5, false, false);
    public static final Symbol REDBULL = new Symbol(null, SymbolType.RedBull, 0.0, 0.5,2.5, 7.5, false, false);
    public static final Symbol MARLBORO = new Symbol(null, SymbolType.Marlboro, 0.0, 0.5,3.5, 10.0, false, false);
    public static final Symbol GIS = new Symbol(null, SymbolType.GIS, 0.0, 1.5,4.0, 15.3, false, false);
    public static final Symbol LUGNER = new Symbol(null, SymbolType.Lugner, 0.0, 2.5,10.0, 50.0, false, false);

    //to be finished in a later version - not mission critical for project
    public static final Symbol FEATURESYMBOL = new Symbol(null, SymbolType.FeatureSymbol, 0.0, 0,0, 0, false, true);

    //has to be added to the first version of the game already!
    public static final Symbol WILD = new Symbol(null, SymbolType.WILD, 0.0, 0,0, 0, true, false);



    public static List<Symbol> getAllSymbols() {

        return Arrays.asList(U1, U6, REDBULL, MARLBORO, GIS, LUGNER, FEATURESYMBOL, WILD);
    }

}
