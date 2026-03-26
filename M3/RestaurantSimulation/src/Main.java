import java.util.Scanner;

import config.SimulationConfig;
import simulation.SimulationEngine;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {

            System.out.println("\n===== Restaurant Simulation Menu =====");
            System.out.println("1. Run Simulation");
            System.out.println("2. Set Number of Cooks (Current: " + SimulationConfig.INITIAL_COOKS + ")");
            System.out.println("3. Set Number of Servers (Current: " + SimulationConfig.INITIAL_SERVERS + ")");
            System.out.println("4. Set Number of Tables (Current: " + SimulationConfig.MAX_TABLES + ")");
            System.out.println("5. Reset to default initialized parameters: 3 Cooks, 6 Servers, 50 Tables");

            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    System.out.println();

                    SimulationEngine engine = new SimulationEngine();
                    engine.run();
                    System.out.println();

                    break;


                case 2:
                    System.out.println();

                    System.out.print("Enter number of cooks: ");
                    SimulationConfig.INITIAL_COOKS = scanner.nextInt();
                    System.out.println();

                    break;

                case 3:
                    System.out.println();

                    System.out.print("Enter number of servers: ");
                    SimulationConfig.INITIAL_SERVERS = scanner.nextInt();
                    System.out.println();

                    break;

                case 4:
                    System.out.println();

                    System.out.print("Enter number of tables: ");
                    SimulationConfig.MAX_TABLES = scanner.nextInt();
                    System.out.println();

                    break;
                case 5:
                    SimulationConfig.INITIAL_COOKS = 3;
                    SimulationConfig.INITIAL_SERVERS = 6;
                    SimulationConfig.MAX_TABLES = 50;
                    System.out.println();

                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting...");
                    break;


                default:
                    System.out.println("Invalid choice.");
                    System.out.println();
            }
        }

        scanner.close();
    }
}