package simulation;

import java.util.PriorityQueue;

import config.SimulationConfig;
import entities.Customer;
import metrics.MetricsCollector;
import models.InventoryModel;

public class SimulationEngine {

    private PriorityQueue<Event> eventQueue =
            new PriorityQueue<>();

    private double currentTime =
            SimulationConfig.OPENING_TIME;

    private MetricsCollector metrics =
            new MetricsCollector();

    private InventoryModel inventory =
            new InventoryModel();

    public void scheduleEvent(Event event) {
        eventQueue.add(event);
    }

    private double getArrivalRate(double time) {

        double hour = 11 + (time / 60);

        if (hour >= 11 && hour < 13) return 25; //peak lunch rush
        if (hour >= 15 && hour < 17) return 10;
        return 15;
    }

    private double generateInterArrival(double lambdaPerHour) {

        double lambdaPerMinute = lambdaPerHour / 60.0;
        return -Math.log(1 - Math.random())
                / lambdaPerMinute;
    }

    public void run() {

        // first arrival
        scheduleEvent(new Event(0,
                EventType.CUSTOMER_ARRIVAL,
                null));

        while (!eventQueue.isEmpty()
                && currentTime
                < SimulationConfig.SIMULATION_DURATION) {

            Event event = eventQueue.poll();
            currentTime = event.time;
            processEvent(event);
        }

        metrics.printSummary();
    }

    private void processEvent(Event event) {

        switch (event.type) {

            case CUSTOMER_ARRIVAL:

                Customer customer =
                        new Customer(currentTime);

                // Schedule abandonment check
                scheduleEvent(new Event(
                        currentTime + 35,
                        EventType.CUSTOMER_ABANDON,
                        customer));

                // Schedule completion
                scheduleEvent(new Event(
                        currentTime + 45,
                        EventType.CUSTOMER_COMPLETE,
                        customer));

                // schedule next arrival
                double lambda =
                        getArrivalRate(currentTime);

                double nextArrival =
                        currentTime
                                + generateInterArrival(lambda);

                scheduleEvent(new Event(
                        nextArrival,
                        EventType.CUSTOMER_ARRIVAL,
                        null));

                break;

            case CUSTOMER_ABANDON:

                if (!event.customer
                        .evaluatePatience(currentTime)) {

                    metrics.recordAbandonment();
                }

                break;

            case CUSTOMER_COMPLETE:

                double serviceTime =
                        currentTime
                                - event.customer
                                .getArrivalTime();

                double satisfaction =
                        event.customer
                                .calculateSatisfaction(serviceTime);

                double revenue = 25.0; //assume items ordered cost 25
                double foodCost = 10.0; //assume ignredients cost 10

                metrics.recordCompletion(
                        revenue,
                        foodCost,
                        satisfaction);

                break;
        }
    }
}

/*
To do:
dynamic revenue model: currently 25 per meal, have dependant on order by order

 */