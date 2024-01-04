package at.ac.fhcampuswien.slotmachine_syntax.Controller;

import at.ac.fhcampuswien.slotmachine_syntax.Model.SymbolType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameManager {

    //variables
    private int bet;
    private double balance;
    private List<Integer> betRange;



    //constructors


    public GameManager(int bet, double balance) {
        this.bet = bet;
        this.balance = balance;
        this.betRange = Arrays.asList(1,2,5,10,20,50,100); //not part of constructor as it's hardcoded during runtime init.
    }


    public void initialize() {

    }

    /**
     * the below methods are used to in/decrease your current bet to the next higher/lower one.
     * e.g. from 10 to 5 credits per spin or vice versa.
     */
    public int decreaseBet() {

        return 0;
    }

    public int increaseBet() {

        return 0;
    }
}
