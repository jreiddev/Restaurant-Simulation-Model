package entities;

import config.SimulationConfig;

public class Server extends Employee {

    private int currentTables = 0;

    public Server(int id) {
        super(id);
    }

    public void assignTable() {
        currentTables++;
    }

    public void releaseTable() {
        if (currentTables > 0) {
            currentTables--;
        }
    }

    public boolean isAvailable() {
        return currentTables < getMaxTables();
    }

    public int getMaxTables() {
        return SimulationConfig.CAPACITY_THRESHOLD;
    }

    public double calculateErrorRate(int remainingTables) {

        //workload-based stress
        double workloadStress =
                (double) currentTables / SimulationConfig.CAPACITY_THRESHOLD;

        double baseError =
                SimulationConfig.BASE_ERROR +
                        workloadStress *
                                (SimulationConfig.MAX_ERROR - SimulationConfig.BASE_ERROR);

        //busy restaurant stress (restaurant-wide)
        double crowdStress = 0;

        double threshold = SimulationConfig.MAX_TABLES * 0.25;

        if (remainingTables < threshold) {

            double crowdFactor =
                    (threshold - remainingTables) / threshold;

            crowdStress = 0.05 * crowdFactor; // small increase
        }

        //combining both of the above
        double totalError = baseError + crowdStress;

        return Math.min(totalError, SimulationConfig.MAX_ERROR);
    }

    public int getCurrentTables() {
        return currentTables;
    }
    public double getUtilization() {
        return (double) currentTables / SimulationConfig.CAPACITY_THRESHOLD;
    }
}
