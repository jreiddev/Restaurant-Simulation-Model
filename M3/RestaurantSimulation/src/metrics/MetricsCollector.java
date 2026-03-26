package metrics;

import config.SimulationConfig;
import java.io.FileWriter;
import java.io.IOException;

import static config.SimulationConfig.*;

public class MetricsCollector {

    public int totalCustomers = 0;
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
        //test food cost print #debugCode
        //System.out.println("foodCost "+foodCost);
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
        return ((totalFoodCost / totalRevenue)/100);
    }
    public int getTotalCustomers() {
        return totalCustomers;
    }
    public boolean meetsSuccessCriteria() {
        //test Success print #debugCode
//        System.out.println(getAverageSatisfaction() +" >?"+ SimulationConfig.TARGET_SATISFACTION);
//        System.out.println(getAbandonmentRate()+ "< "+ SimulationConfig.MAX_ABANDONMENT_RATE);
//        System.out.println(getFoodCostRatio() +"< "+SimulationConfig.MAX_FOOD_COST_RATIO);

        return getAverageSatisfaction()
                > SimulationConfig.TARGET_SATISFACTION
                && getAbandonmentRate()
                < SimulationConfig.MAX_ABANDONMENT_RATE
                && getFoodCostRatio()
                < SimulationConfig.MAX_FOOD_COST_RATIO;
    }

    public void printSummary() {

        String output =
                "===== Simulation Summary =====\n" +
                        "Total Customers: " + totalCustomers + "\n" +
                        "Abandonment Rate: " + getAbandonmentRate() + "\n" +
                        "Average Satisfaction: " + getAverageSatisfaction() + "\n" +
                        "Food Cost Ratio: " + getFoodCostRatio() + "\n" +
                        "Meets Success Criteria: " + meetsSuccessCriteria() + "\n";

        System.out.println(output);

        try {
            FileWriter writer = new FileWriter("simulation_log.txt", true); //shows where log is sent

            writer.write("=== New Run ===\n");
            writer.write("Cooks: " + INITIAL_COOKS + "\n");
            writer.write(output);
            writer.write("\n");

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
