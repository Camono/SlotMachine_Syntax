package at.ac.fhcampuswien.slotmachine_syntax.Model;

import java.util.Objects;

/**
 * This class is used for the displayed symbols of the slot machine.
 * each variable represents a logic for the spinning wheels including appearChances,
 * multipliers and images.
 * Lower worth symbol types have higher chances to be rolled, whereas better ones like Lugner
 * are harder to get.
 */

public class Symbol {
    private final String imagePath;
    private final SymbolType symbolType;
    private final double appearFactor;
    private final double multiplierX3;
    private final double multiplierX4;
    private final double multiplierX5;
    private final boolean isWild;
    private final boolean isFreeSpin;

    public Symbol(String imagePath, SymbolType symbolType, double appearChance, double multiplierX3, double multiplierX4, double multiplierX5, boolean isWild, boolean isFreeSpin) {
        this.imagePath = imagePath;
        this.symbolType = symbolType;
        this.appearFactor = appearChance;
        this.multiplierX3 = multiplierX3;
        this.multiplierX4 = multiplierX4;
        this.multiplierX5 = multiplierX5;
        this.isWild = isWild;
        this.isFreeSpin = isFreeSpin;
    }

    public String getImagePath() {
        return imagePath;
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
    public int hashCode() {
        return Objects.hash(imagePath, symbolType, appearFactor, multiplierX3, multiplierX4, multiplierX5, isWild, isFreeSpin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Symbol symbolToCompare)) {
            return false;
        }

        return Objects.equals(symbolToCompare.getSymbolType(), this.symbolType);
    }

    @Override
    public String toString() {
        return symbolType.name();
    }
}
