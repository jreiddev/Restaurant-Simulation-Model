package entities;


import config.SimulationConfig;

public class Customer {

    private static int idCounter = 0;

    private int id;
    private double arrivalTime;
    private Server server;
    private double patienceMax;

    public Customer(double arrivalTime) {
        this.id = ++idCounter;
        this.arrivalTime = arrivalTime;
        double rand = Math.random();

        if (rand < 0.15) {
            //very impatient customers
            this.patienceMax = 5 + Math.random() * 5;
        } else if (rand < 0.5) {
            //med patience
            this.patienceMax = 10 + Math.random() * 10;
        } else {
            //very patient customers
            this.patienceMax = 20 + Math.random() * 15;
        }
    }

    public int getId() {
        return id;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    //patience(t) = 100 - 5*(wait - 30)
    public double calculatePatience(double currentTime) {

        double waitTime = currentTime - arrivalTime;

        return patienceMax
                - SimulationConfig.PATIENCE_DECAY_RATE
                * (waitTime - SimulationConfig.EXPECTED_WAIT);
    }

    public boolean evaluatePatience(double currentTime) {
        return calculatePatience(currentTime) > 0;
    }

    public double calculateSatisfaction(double serviceTime) {

        double expected = SimulationConfig.EXPECTED_SERVICE_TIME;

        //customers tolerate some delay
        double grace = 10; //minutes

        if (serviceTime <= expected + grace) {
            return 100.0;
        }

        double delay = serviceTime - (expected + grace);

        //much smoother decay
        double satisfaction = 100 * Math.exp(-0.02 * delay);
        return Math.max(0, Math.min(100, satisfaction));

    }

    //gets and sets server related to customer
    public void setServer(Server server) {
        this.server = server;
    }
    public Server getServer() {
        return server;
    }
}
