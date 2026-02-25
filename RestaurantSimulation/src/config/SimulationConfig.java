package config;

public class SimulationConfig {


    // SUCCESS CRITERIA
    public static final double TARGET_SATISFACTION = 80.0;
    public static final double MAX_ABANDONMENT_RATE = 0.15;
    public static final double MAX_FOOD_COST_RATIO = 0.35;

    // INITIALIZATION PARAMETERS
    public static final int INITIAL_SERVERS = 2;
    public static final int INITIAL_COOKS = 2;
    public static final int INITIAL_INVENTORY = 1000;

    public static final double OPENING_TIME = 0.0;
    public static final double SIMULATION_DURATION = 480.0; // 8 hours

    // PATIENCE MODEL PARAMETERS
    // patience(t) = 100 - 5*(wait - 30)
    public static final double PATIENCE_MAX = 100.0;
    public static final double EXPECTED_WAIT = 30.0;
    public static final double PATIENCE_DECAY_RATE = 5.0;

    // ERROR MODEL PARAMETERS
    public static final double BASE_ERROR = 0.04;
    public static final double MAX_ERROR = 0.10;
    public static final int CAPACITY_THRESHOLD = 6;
}