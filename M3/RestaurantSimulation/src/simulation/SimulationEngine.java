package simulation;

import java.util.*;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import config.SimulationConfig;
import entities.*;
import metrics.MetricsCollector;
import models.InventoryModel;

import static config.SimulationConfig.*;

public class SimulationEngine {

    private Queue<Customer> waitingCustomers = new LinkedList<>();
    private Queue<Customer> kitchenQueue = new LinkedList<>();

    private PriorityQueue<Event> eventQueue = new PriorityQueue<>();

    private List<Server> servers = new ArrayList<>();
    private List<Cook> cooks = new ArrayList<>();

    private double currentTime = SimulationConfig.OPENING_TIME;

    private MetricsCollector metrics = new MetricsCollector();
    private InventoryModel inventory = new InventoryModel();
    private int availableTables = SimulationConfig.MAX_TABLES;
    private int serverIndex = 0;
    private PrintWriter logWriter;


    //constructor
    public SimulationEngine() {

        try {
            logWriter = new PrintWriter("simulation_log.csv");
            logWriter.println("Time,Event,CustomerID,Details");
            System.out.println("Log file created at: " +
                    new java.io.File("simulation_log.csv").getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initialize servers
        for (int i = 0; i < SimulationConfig.INITIAL_SERVERS; i++) {
            servers.add(new Server(i));
        }

        //initialize cooks
        for (int i = 0; i < SimulationConfig.INITIAL_COOKS; i++) {
            cooks.add(new Cook(i));
        }
    }

    //logging
    private void log(String event, Customer customer, String details) {
        if (logWriter != null) {
            int id = (customer != null) ? customer.getId() : -1;
            logWriter.println(currentTime + "," + event + "," + id + "," + details);
        }
    }
    private void writeSummaryToCSV() {

        if (logWriter == null) return;

        logWriter.println(); // blank line
        logWriter.println("===== Simulation Summary =====");

        logWriter.println("Total Customers," + metrics.getTotalCustomers());
        logWriter.println("Abandonment Rate," + metrics.getAbandonmentRate());
        logWriter.println("Average Satisfaction," + metrics.getAverageSatisfaction());
        logWriter.println("Food Cost Ratio," + metrics.getFoodCostRatio());
        logWriter.println("Meets Success Criteria," + metrics.meetsSuccessCriteria());
        logWriter.println("Cooks: " +INITIAL_COOKS+" Servers: "+INITIAL_SERVERS+" Tables: "+MAX_TABLES);

    }

    //event scheduling
    public void scheduleEvent(Event event) {
        eventQueue.add(event);
    }

    //customer arrival logic
    private double getArrivalRate(double time) {

        double hour = 11 + (time / 60);

        if (hour >= 11 && hour < 13) return 25;
        if (hour >= 15 && hour < 17) return 10;
        return 15;
    }

    private double generateInterArrival(double lambdaPerHour) {
        double lambdaPerMinute = lambdaPerHour / 60.0;
        return -Math.log(1 - Math.random()) / lambdaPerMinute;
    }

    //server logic
    private Server findAvailableServer() {

        if (servers.isEmpty()) return null;

        serverIndex = serverIndex % servers.size();

        // First: rotation
        for (int i = 0; i < servers.size(); i++) {

            Server s = servers.get(serverIndex);
            serverIndex = (serverIndex + 1) % servers.size();

            if (s.isAvailable() && s.getCurrentTables() < SimulationConfig.CAPACITY_THRESHOLD) {
                return s;
            }
        }

        //fallback: lowest load
        Server best = null;
        int minTables = Integer.MAX_VALUE; //when server at max util find lowest util server

        for (Server s : servers) {
            if (s.getCurrentTables() < minTables) {
                minTables = s.getCurrentTables();
                best = s;
            }
        }

        return best;
    }

    //customer seating
    private void seatCustomer(Customer customer, Server server) {

        boolean allMaxed = true;

        for (Server s : servers) {
            if (s.getCurrentTables() < SimulationConfig.CAPACITY_THRESHOLD * 0.8) {
                allMaxed = false;
                break;
            }
        }

        if (allMaxed) { //waiting queue for maxed out restaurant
            waitingCustomers.add(customer);
            log("WAITING", customer, "No table/server available");

            scheduleEvent(
                    new Event(
                            currentTime + 1,
                            EventType.PATIENCE_CHECK,
                            customer
                    )
            );

            return;
        }

        customer.setServer(server);
        if (availableTables > 0) {
            server.assignTable();
            availableTables--;
            log("SEATED", customer, "Server " + server.getId());
        } else {
            waitingCustomers.add(customer);
            log("WAITING", customer, "No table available");
            scheduleEvent(
                    new Event(
                            currentTime + 1,
                            EventType.PATIENCE_CHECK,
                            customer
                    )
            );
            return;
        }
        kitchenQueue.add(customer);
        tryStartCooking(currentTime);
    }

    //cook logic
    private Cook findAvailableCook() {
        for (Cook c : cooks) {
            if (c.isAvailable()) return c;
        }
        return null;
    }

    private void tryStartCooking(double currentTime) {

        Cook cook = findAvailableCook();

        if (cook != null && !kitchenQueue.isEmpty()) {

            Customer customer = kitchenQueue.poll();

            cook.setBusy(true);

            log("COOK_START", customer, "Cook " + cook.getId());
            double cookTime = cook.generateCookTime();

            double errorRate = customer.getServer().calculateErrorRate(availableTables);
            cookTime *= (1 + errorRate);

            scheduleEvent(
                    new Event(
                            currentTime + cookTime,
                            EventType.COOK_COMPLETE,
                            customer,
                            cook
                    )
            );
        }
    }

    //main run method
    public void run() {

        scheduleEvent(new Event(0, EventType.CUSTOMER_ARRIVAL, null));
        while (!eventQueue.isEmpty()
                && currentTime < SimulationConfig.SIMULATION_DURATION) {

            Event event = eventQueue.poll();
            currentTime = event.time;
            processEvent(event);
        }

        metrics.printSummary();//print to console
        writeSummaryToCSV();


        if (logWriter != null) {
            logWriter.close();
        }
    }

    //even processing
    private void processEvent(Event event) {

        double hour = 11 + (currentTime / 60);


        switch (event.type) {


            case CUSTOMER_ARRIVAL:

                Customer customer = new Customer(currentTime);
                log("ARRIVAL", customer, "Entered restaurant");
                Server server = findAvailableServer();

                if (server != null) {
                    seatCustomer(customer, server);
                } else {
                    waitingCustomers.add(customer);
                    log("WAITING", customer, "No server available");

                    scheduleEvent(
                            new Event(
                                    currentTime + 1,
                                    EventType.PATIENCE_CHECK,
                                    customer
                            )
                    );
                }

                double lambda = getArrivalRate(currentTime);
                double nextArrival = currentTime + generateInterArrival(lambda);

                scheduleEvent(new Event(
                        nextArrival,
                        EventType.CUSTOMER_ARRIVAL,
                        null));

                break;


            case PATIENCE_CHECK:

                Customer waitingCustomer = event.customer;
//                System.out.println("Checking patience for customer..."); #debugCode
                if (waitingCustomers.contains(waitingCustomer)) {

                    if (!waitingCustomer.evaluatePatience(currentTime)) {
//                        System.out.println("Customer abandoned!"); #debugCode
                        waitingCustomers.remove(waitingCustomer);
                        metrics.recordAbandonment();
                        log("ABANDON", waitingCustomer, "Left due to wait time");

                    } else {

                        scheduleEvent(
                                new Event(
                                        currentTime + 1,
                                        EventType.PATIENCE_CHECK,
                                        waitingCustomer
                                )
                        );
                    }
                }

                break;


            case COOK_COMPLETE:

                Customer cookedCustomer = event.customer;
                Cook cook = event.cook;

                cook.setBusy(false);

                //simulated eating time
                double eatTime = 5; //have customers eat in 5 minutes for simplicity. add complexity later!

                scheduleEvent(
                        new Event(
                                currentTime + eatTime,
                                EventType.CUSTOMER_COMPLETE,
                                cookedCustomer
                        )
                );

                //meals cooked in sequential order
                tryStartCooking(currentTime);
                log("COOK_COMPLETE", cookedCustomer, "Cook " + cook.getId());
                break;

            case CUSTOMER_COMPLETE:

                Customer completedCustomer = event.customer;
                Server s = completedCustomer.getServer();

                if (s != null) {
                    s.releaseTable();
                    availableTables++;
                }
                if (hour > 14 && servers.size() > 2) { //post lunch rush

                    Iterator<Server> iterator = servers.iterator();

                    while (iterator.hasNext()) {

                        Server sCheck = iterator.next();

                        //only cut idle / low-util servers after lunch rush
                        if (sCheck.getUtilization() < SimulationConfig.CUT_THRESHOLD
                                && sCheck.getCurrentTables() <= 2) {

                            iterator.remove();
                            break; // remove one at a time
                        }
                    }

                }
                double serviceTime =
                        currentTime - completedCustomer.getArrivalTime();

                double satisfaction =
                        completedCustomer.calculateSatisfaction(serviceTime);

                double errorRate = s.calculateErrorRate(availableTables);

                inventory.addWaste(errorRate);
                inventory.useIngredients(5, errorRate);

                double revenue = 25.0;
                double foodCost = inventory.getTotalFoodCost();

                metrics.recordCompletion(
                        revenue,
                        foodCost,
                        satisfaction);
                log("COMPLETE", completedCustomer,
                        "Satisfaction=" + satisfaction);
                //seats next "waiting" customer
                if (!waitingCustomers.isEmpty()) {

                    Server nextServer = findAvailableServer();

                    if (nextServer != null) {
                        Customer next = waitingCustomers.poll();
                        seatCustomer(next, nextServer);
                    }
                }

                break;


        }
    }
}

