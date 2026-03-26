package config;

public class SimulationConfig {


    //SUCCESS CRITERIA
    public static  double TARGET_SATISFACTION = 70.0; //average 70% people being happy
    public static final double MAX_ABANDONMENT_RATE = 0.15;
    public static final double MAX_FOOD_COST_RATIO = 0.35;
    public static final double EXPECTED_SERVICE_TIME = 45; // minutes

    //INITIALIZATION PARAMETERS
    public static  int INITIAL_SERVERS = 6;
    public static  int INITIAL_COOKS = 3;
    public static  int INITIAL_INVENTORY = 1000;
    public static  int MAX_TABLES = 50;

    public static final double OPENING_TIME = 0.0;
    public static final double SIMULATION_DURATION = 480.0; // 8 hours

    //PATIENCE MODEL PARAMETERS
    //patience(t) = 100 - 5*(wait - 30)
    public static final double PATIENCE_MAX = 30;
    public static final double EXPECTED_WAIT = 30.0;
    public static final double PATIENCE_DECAY_RATE = 5.0;

    //ERROR MODEL PARAMETERS
    public static final double BASE_ERROR = 0.04;
    public static final double MAX_ERROR = 0.10;
    public static final int CAPACITY_THRESHOLD = 6;
    public static final double CUT_THRESHOLD = 0.25; //25% utilization

}