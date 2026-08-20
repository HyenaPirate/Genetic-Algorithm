import org.jfree.data.xy.XYSeries;

public class TravellingSalesman extends ProblemBlueprint{

    public TravellingSalesman(String filePath) {
        super(filePath);
    }

    @Override
    public Chromosome createRandomChromosome() {
        return new Chromosome.Permutation(data.length);
    }

    @Override
    public float calculateFitness(Subject subject) {
        int[] chromosome = ((Chromosome.Permutation)subject.getChromosome()).getGenes();
        //if (!validateSolution(chromosome)) return 0f;
        double pathLength = calculatePathLength(chromosome);
        float fitness =  (float)(1000 / pathLength);
        subject.setFitness(fitness);
        return fitness;
    }

    private double calculatePathLength(int[] path){
        double totalPathLength = 0;

        for (int city = 1; city<path.length; city++){
            float moved_x = Math.abs(data[path[city]].get("x").getAsFloat() - data[path[city-1]].get("x").getAsFloat());
            float moved_y = Math.abs(data[path[city]].get("y").getAsFloat() - data[path[city-1]].get("y").getAsFloat());
            totalPathLength += Math.sqrt((moved_x * moved_x) + (moved_y * moved_y));
        }
        float moved_x = Math.abs(data[path[path.length-1]].get("x").getAsFloat() - data[path[0]].get("x").getAsFloat());
        float moved_y = Math.abs(data[path[path.length-1]].get("y").getAsFloat() - data[path[0]].get("y").getAsFloat());
        totalPathLength += Math.sqrt((moved_x * moved_x) + (moved_y * moved_y));
        return totalPathLength;
    }

    @Override
    public void displaySubjectResults(Subject subject){
        System.out.println("The shortest path length: " + calculatePathLength(((Chromosome.Permutation)subject.getChromosome()).getGenes()) + " units.");
        System.out.println("Order of cities: ");
        subject.DisplayChromosome();
        System.out.println((validateSolution(((Chromosome.Permutation)subject.getChromosome()).getGenes()))? "Solution valid." : "Solution invalid: duplicated cities.");
    }

    @Override
    public void AddGraphSeries(LiveGraph graph) {
        XYSeries fitness = new XYSeries("Fitness");
        XYSeries pathLength = new XYSeries("PathLength");

        graph.addSeries(fitness);
        graph.addSeries(pathLength);
    }

    @Override
    public void UpdateGraph(LiveGraph graph, int generation, Subject bestSubject) {
        XYSeries fitness = graph.getSeries("Fitness");
        XYSeries pathLength = graph.getSeries("PathLength");

        double bestPathLength = calculatePathLength(((Chromosome.Permutation)bestSubject.getChromosome()).getGenes());

        fitness.add(generation, bestSubject.getFitness());
        pathLength.add(generation, bestPathLength);

        graph.updateLabel("Fitness", bestSubject.getFitness());
        graph.updateLabel(  "PathLength", bestPathLength);
        graph.updateInfoPanel(generation);

    }

    private boolean validateSolution(int[] path) {
        if (path.length != data.length) return false;

        boolean[] visited = new boolean[data.length];

        for (int city : path) {
            if (city < 0 || city >= data.length) return false;
            if (visited[city]) return false;
            visited[city] = true;
        }
        return true;
    }
}
