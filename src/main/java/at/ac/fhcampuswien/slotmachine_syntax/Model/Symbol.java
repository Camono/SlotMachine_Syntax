package at.ac.fhcampuswien.slotmachine_syntax.Model;

import javafx.scene.image.Image;

import java.util.Objects;

/**
 * This class is used for the displayed symbols of the slot machine.
 * each variable represents a logic for the spinning wheels including appearChances,
 * multipliers and images.
 * Lower worth symbol types have higher chances to be rolled, where as better ones like Lugner
 * are harder to get.
 */

public class Symbol {
    private Image image;
    private SymbolType symbolType;
    private double appearFactor;
    private double multiplierX3;
    private double multiplierX4;
    private double multiplierX5;
    private boolean isWild;
    private boolean isFreeSpin;

    public Symbol(Image image, SymbolType symbolType, double appearChance, double multiplierX3, double multiplierX4, double multiplierX5, boolean isWild, boolean isFreeSpin) {
        this.image = image;
        this.symbolType = symbolType;
        this.appearFactor = appearChance;
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

    public double getAppearFactor() {
        return appearFactor;
    }

    public double getMultiplierX3() {
        return multiplierX3;
    }

    public double getMultiplierX4() {
        return multiplierX4;
    }

    public double getMultiplierX5() {
        return multiplierX5;
    }

    public boolean isWild() {
        return isWild;
    }

    public boolean isFreeSpin() {
        return isFreeSpin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Symbol)) {
            return false;
        }
        Symbol symbolToCompare = (Symbol) obj;
        return Objects.equals(symbolToCompare, this.symbolType);
    }

    @Override
    public String toString() {
        return symbolType.name();
    }
}
