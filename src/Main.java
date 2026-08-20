public class Main {

    public static void main(String[] args) throws InterruptedException {

        Knapsack problem = new Knapsack("src/Items215.json", 30f);
        //SetCovering problem = new SetCovering("src/SetCovering30.json", 20);
        //TravellingSalesman problem = new TravellingSalesman("src/Citties50.json");
        TestManager test = new TestManager(problem, 1000, 0.001f);

        test.RunTest(1000, 0);


    }
}