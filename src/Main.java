import genetic_algorithm.TestManager;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        //problems.Knapsack problem = new problems.Knapsack("data/Items215.json", 30f);
        problems.TravellingSalesman problem = new problems.TravellingSalesman("data/Citties50.json");
        TestManager test = new TestManager(problem, 1000, 0.001f);

        test.RunTest(1000, 0);


    }
}