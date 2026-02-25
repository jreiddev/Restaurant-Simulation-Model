package models;

import config.SimulationConfig;

public class InventoryModel {

    private int ingredients =
            SimulationConfig.INITIAL_INVENTORY;

    private double totalFoodCost = 0;

    public boolean useIngredients(int amount, double errorRate) {

        int waste = (int)(amount * errorRate);
        int totalNeeded = amount + waste;

        if (ingredients >= totalNeeded) {

            ingredients -= totalNeeded;

            double costPerUnit = 2.0;
            totalFoodCost += totalNeeded * costPerUnit;

            return true;
        }

        return false;
    }

    public double getTotalFoodCost() {
        return totalFoodCost;
    }

    public int getRemainingIngredients() {
        return ingredients;
    }
}

/*
To DO
real food cost, not overall double for everything
call inventory.useIngredients()
use errorRates from coook/server
track food waste
 */