package at.ac.fhcampuswien.slotmachine_syntax.Model;

import javafx.scene.image.Image;

public class Symbol {

    private Image image;
    private SymbolType symbolType;
    private double appearChance;
    private int multiplierX3;
    private int multiplierX4;
    private int multiplierX5;
    private boolean isWild;
    private boolean isFreeSpin;

    public Symbol(Image image, SymbolType symbolType, double appearChance, int multiplierX3, int multiplierX4, int multiplierX5, boolean isWild, boolean isFreeSpin) {
        this.image = image;
        this.symbolType = symbolType;
        this.appearChance = appearChance;
        this.multiplierX3 = multiplierX3;
        this.multiplierX4 = multiplierX4;
        this.multiplierX5 = multiplierX5;
        this.isWild = isWild;
        this.isFreeSpin = isFreeSpin;
    }

    public Image getImage() {
        return image;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public double getAppearChance() {
        return appearChance;
    }

    public int getMultiplierX3() {
        return multiplierX3;
    }

    public int getMultiplierX4() {
        return multiplierX4;
    }

    public int getMultiplierX5() {
        return multiplierX5;
    }

    public boolean isWild() {
        return isWild;
    }

    public boolean isFreeSpin() {
        return isFreeSpin;
    }
}
