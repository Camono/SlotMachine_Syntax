package at.ac.fhcampuswien.slotmachine_syntax.Model;

import java.util.List;

public class GameResult {
    //initialize variables
    private double profit;
    private double newBalance;
    private List<Symbol> symbols;


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
