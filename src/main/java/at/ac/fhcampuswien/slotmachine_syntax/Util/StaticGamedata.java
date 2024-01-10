package at.ac.fhcampuswien.slotmachine_syntax.Util;

import at.ac.fhcampuswien.slotmachine_syntax.Model.Symbol;
import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;

public class StaticGamedata {


    public static final Symbol U1 = new Symbol(new Image("/symbols/Banana.png"), SymbolType.U1, 0.25, 0.15, 0.5, 2.5, false, false);
    public static final Symbol U6 = new Symbol(new Image("/symbols/Bar.png"), SymbolType.U6, 0.24, 0.15, 0.5, 2.5, false, false);
    public static final Symbol REDBULL = new Symbol(new Image("/symbols/Cheeries.png"), SymbolType.RED_BULL, 0.17, 0.5, 2.5, 5.5, false, false);
    public static final Symbol MARLBORO = new Symbol(new Image("/symbols/Diamond.png"), SymbolType.MARLBORO, 0.14, 0.5, 3.5, 7.0, false, false);
    public static final Symbol GIS = new Symbol(new Image("/symbols/GoldenShoe.png"), SymbolType.GIS, 0.07, 1.5, 6.0, 15.3, false, false);
    public static final Symbol LUGNER = new Symbol(new Image("/symbols/Grapes.png"), SymbolType.LUGNER, 0.03, 5, 20.0, 50.0, false, false);

    //to be finished in a later version - not mission critical for project
    public static final Symbol FEATURESYMBOL = new Symbol(new Image("/symbols/Seven.png"), SymbolType.FEATURE_SYMBOL, 0.0, 0, 0, 0, false, true);

    //has to be added to the first version of the game already!
    public static final Symbol WILD = new Symbol(new Image("/symbols/Orange.png"), SymbolType.WILD, 0.1, 0, 0, 0, true, false);


    public static List<Symbol> getAllSymbols() {

        return Arrays.asList(U1, U6, REDBULL, MARLBORO, GIS, LUGNER, WILD);
    }

}
