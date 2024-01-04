package at.ac.fhcampuswien.slotmachine_syntax.Model;

import java.util.ArrayList;
import java.util.List;

public class GameResult {

    private int profit;
    private int newBalance;
    private List<Symbol> symbols;


    public GameResult(int profit, int newBalance, List<Symbol> symbols) {
        this.profit = profit;
        this.newBalance = newBalance;
        this.symbols = symbols;
    }

    public int getProfit() {
        return profit;
    }

    public int getNewBalance() {
        return newBalance;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }
}
