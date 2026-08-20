package genetic_algorithm;

import problems.ProblemBlueprint;

import java.util.Arrays;

public class TestManager {

    // --------VARIABLES
    private final ProblemBlueprint problem;
    private final int amountOfSubjects;
    private final float chanceOfMutation;

    private Subject[] generation;

    private LiveGraph graph;

    //---------CONSTRUCTOR
    public TestManager(
            ProblemBlueprint problem,
            int amountOfSubjects,
            float chanceOfMutation
    ){
      this.problem = problem;
      this.amountOfSubjects = amountOfSubjects;
      this.chanceOfMutation = chanceOfMutation;
      this.graph = new LiveGraph();
      problem.AddGraphSeries(graph);
    }

    public void CreateRandomSubjects(){
        Subject[] newSubjects = new Subject[amountOfSubjects];
        for (int i = 0; i< amountOfSubjects; i++){
            newSubjects[i] = new Subject(problem.createRandomChromosome());
        }
        generation = newSubjects;
    }

    public void DisplaySubjects(){
        for(Subject subject : generation){
            System.out.print("[" + subject.getFitness() + "] - ");
            subject.DisplayChromosome();
            System.out.println();
        }
    }

    public void CalculateFitnesses() {
        for (Subject subject : generation) {
            problem.calculateFitness(subject);
        }
    }

    private void SelectNewGeneration() {

        Subject[] newSubjects = new Subject[amountOfSubjects];

        // Find the lowest fitness
        float minFitness = Float.MAX_VALUE;

        for (Subject subject : generation) {
            if (subject.getFitness() < minFitness) {
                minFitness = subject.getFitness();
            }
        }

        // Shift all fitnesses if there are negative values
        float offset = minFitness < 0 ? -minFitness : 0;

        // Calculate total shifted fitness
        float totalFitness = 0;

        for (Subject subject : generation) {
            totalFitness += subject.getFitness() + offset;
        }

        // If all selection weights are zero
        if (totalFitness == 0) {
            CreateRandomSubjects();
            return;
        }

        // Roulette wheel selection
        for (int i = 0; i < amountOfSubjects; i++) {

            float pointer = (float) (Math.random() * totalFitness);
            float currentFitness = 0;

            for (Subject subject : generation) {

                currentFitness += subject.getFitness() + offset;

                if (currentFitness >= pointer) {
                    newSubjects[i] = new Subject(subject.getChromosome().copy());
                    break;
                }
            }
        }

        generation = newSubjects;
    }

    private void CrossSubjects() {
        int[] pairs = new int[amountOfSubjects];
        for (int i = 0; i < amountOfSubjects; i++) pairs[i] = i;

        for (int i = pairs.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = pairs[i];
            pairs[i] = pairs[j];
            pairs[j] = temp;
        }

        Subject[] newGeneration = new Subject[amountOfSubjects];

        for (int i = 0; i < amountOfSubjects; i += 2) {
            Chromosome parent1 = generation[pairs[i]].getChromosome();
            Chromosome parent2 = generation[pairs[i + 1]].getChromosome();
            Chromosome child1;
            Chromosome child2;

            if (parent1 instanceof Chromosome.Permutation) {
                child1 = crossoverPermutation(
                        (Chromosome.Permutation) parent1,
                        (Chromosome.Permutation) parent2
                );
                child2 = crossoverPermutation(
                        (Chromosome.Permutation) parent2,
                        (Chromosome.Permutation) parent1
                );

            } else {
                child1 = parent1.copy();
                child2 = parent2.copy();

                int crossoverPoint = (int) (Math.random() * problem.getData().length);

                for (int j = 0; j < crossoverPoint; j++) {
                    Object temp = child1.getGene(j);
                    child1.setGene(j, child2.getGene(j));
                    child2.setGene(j, temp);
                }
            }
            newGeneration[i] = new Subject(child1);
            newGeneration[i + 1] = new Subject(child2);
        }
        generation = newGeneration;
    }

    private Chromosome.Permutation crossoverPermutation(
            Chromosome.Permutation parent1,
            Chromosome.Permutation parent2
    ) {
        int size = parent1.getSize();
        int[] child = new int[size];

        // Empty child
        Arrays.fill(child, -1);

        // Pick two points
        int start = (int) (Math.random() * size);
        int end = (int) (Math.random() * size);

        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }

        // Copy section from parent 1
        for (int i = start; i <= end; i++) {
            child[i] = (int) parent1.getGene(i);
        }

        // Fill remaining spaces from parent 2
        int position = (end + 1) % size;

        for (int i = 0; i < size; i++) {

            int city =
                    (int) parent2.getGene((end + 1 + i) % size);

            // Check if city already exists
            boolean exists = false;

            for (int j = 0; j < size; j++) {

                if (child[j] == city) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {

                child[position] = city;

                position = (position + 1) % size;
            }
        }

        return new Chromosome.Permutation(child);
    }

    private void MutateGeneration(){
        Subject[] newGeneration = new Subject[amountOfSubjects];
        for (int i = 0; i< amountOfSubjects; i++){
            Chromosome newChromosome = generation[i].getChromosome().copy();
            for (int j = 0; j < newChromosome.getSize(); j++) {
                if (Math.random() < chanceOfMutation) {
                    newChromosome.mutateGene(j);
                }
            }
            newGeneration[i] = new Subject(newChromosome);
        }
        this.generation = newGeneration;
    }

    public void EvolveGeneration(){
        Chromosome bestChromosome = GetBestSubject().getChromosome().copy();
        SelectNewGeneration();
        CrossSubjects();
        MutateGeneration();
        generation[0] = new Subject(bestChromosome);
        CalculateFitnesses();
    }

    public void RunTest(int iterations, int delayMs) throws InterruptedException {

        System.out.println("=================================");
        System.out.println("Starting test: " + problem.getClass().getSimpleName());
        System.out.println("Data points: " + problem.getData().length);
        System.out.println("Subjects per generation: " + amountOfSubjects);
        System.out.println("Iterations: " + iterations);
        System.out.println("---------------------------------");

        CreateRandomSubjects();
        CalculateFitnesses();

        for (int i = 1; i <= iterations; i++) {

            EvolveGeneration();
            problem.UpdateGraph(graph, i, GetBestSubject());

            if (delayMs > 0) {
                Thread.sleep(delayMs);
            }
        }
        System.out.println("Test finished.");
        DisplayBestSubject();
        System.out.println("=================================");
    }

    public Subject GetBestSubject(){
        Subject bestSubject = generation[0];

        for (Subject subject : generation) {
            if (subject.getFitness() > bestSubject.getFitness()) {
                bestSubject = subject;
            }
        }
        return bestSubject;
    }

    public void DisplayBestSubject() {
        problem.displaySubjectResults(GetBestSubject());
    }

}
