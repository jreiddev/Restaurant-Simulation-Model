package entities;

import config.SimulationConfig;

public class Customer {

    private static int idCounter = 0;

    private int id;
    private double arrivalTime;

    public Customer(double arrivalTime) {
        this.id = ++idCounter;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    // patience(t) = 100 - 5*(wait - 30)
    public double calculatePatience(double currentTime) {

        double waitTime = currentTime - arrivalTime;

        return SimulationConfig.PATIENCE_MAX
                - SimulationConfig.PATIENCE_DECAY_RATE
                * (waitTime - SimulationConfig.EXPECTED_WAIT);
    }

    public boolean evaluatePatience(double currentTime) {
        return calculatePatience(currentTime) > 0;
    }

    public double calculateSatisfaction(double serviceTime) {
        double satisfaction = 100 - (serviceTime * 2);
        return Math.max(0, satisfaction);
    }
}

/*
To DO:
customers enter queue upon enterin restaurant before being sat
patience determins abandonment of queue's
INCREASE patience levels as events complete earlier than avg, if normal wait is 5, then being sat immedietly
     will add based on percentage
 */