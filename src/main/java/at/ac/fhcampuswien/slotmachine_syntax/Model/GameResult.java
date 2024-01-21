package at.ac.fhcampuswien.slotmachine_syntax.Model;

import java.util.List;

public class GameResult {
    private final double profit;
    private final double newBalance;
    private final List<Symbol> symbols;

    public GameResult(double profit, double newBalance, List<Symbol> symbols) {
        this.profit = profit;
        this.newBalance = newBalance;
        this.symbols = symbols;
    }

    public double getProfit() {
        return profit;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }
}
