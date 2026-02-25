package metrics;

import config.SimulationConfig;

public class MetricsCollector {

    private int totalCustomers = 0;
    private int abandonedCustomers = 0;

    private double totalRevenue = 0;
    private double totalFoodCost = 0;
    private double totalSatisfaction = 0;

    public void recordCompletion(double revenue,
                                 double foodCost,
                                 double satisfaction) { //meal complete

        totalCustomers++;
        totalRevenue += revenue;
        totalFoodCost += foodCost;
        totalSatisfaction += satisfaction;
    }

    public void recordAbandonment() {
        totalCustomers++;
        abandonedCustomers++;
    }

    public double getAverageSatisfaction() {
        if (totalCustomers == 0) return 0;
        return totalSatisfaction / totalCustomers;
    }

    public double getAbandonmentRate() {
        if (totalCustomers == 0) return 0;
        return (double) abandonedCustomers / totalCustomers;
    }

    public double getFoodCostRatio() {
        if (totalRevenue == 0) return 0;
        return totalFoodCost / totalRevenue;
    }

    public boolean meetsSuccessCriteria() {

        return getAverageSatisfaction()
                > SimulationConfig.TARGET_SATISFACTION
                && getAbandonmentRate()
                < SimulationConfig.MAX_ABANDONMENT_RATE
                && getFoodCostRatio()
                < SimulationConfig.MAX_FOOD_COST_RATIO;
    }

    public void printSummary() {

        System.out.println("===== Simulation Summary =====");
        System.out.println("Total Customers: " + totalCustomers);
        System.out.println("Abandonment Rate: " + getAbandonmentRate());
        System.out.println("Average Satisfaction: " + getAverageSatisfaction());
        System.out.println("Food Cost Ratio: " + getFoodCostRatio());
        System.out.println("Meets Success Criteria: " + meetsSuccessCriteria());
    }
}

/*
To DO:
-customers have predetermined orders, so IF they abandon queue have the potential revenue lost be listed
-have errors reflect increased food cost. when cost increase during increase error rate
     take the difference and note it
 */