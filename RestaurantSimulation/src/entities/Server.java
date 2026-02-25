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

    public double calculateErrorRate() {

        double stressFactor =
                (double) currentTables / SimulationConfig.CAPACITY_THRESHOLD;

        double error =
                SimulationConfig.BASE_ERROR +
                        stressFactor *
                                (SimulationConfig.MAX_ERROR - SimulationConfig.BASE_ERROR);

        return Math.min(error, SimulationConfig.MAX_ERROR);
    }

    public int getCurrentTables() { //create table id's
        return currentTables;
    }
}

/*
To DO:
implement real table assignment
server workload changes as customers enter/leave
maintain list of servers
assign customers to least busy server
increase server workload / error rate
cut servers as utilization rates plummet after peak hours
 */