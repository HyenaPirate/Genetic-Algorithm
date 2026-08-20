public class Subject {

    private Chromosome chromosome;
    private float fitness = 0;

    public Subject(Chromosome chromosome) {
        this.chromosome = chromosome;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }

    public void DisplayChromosome(){
        chromosome.display();
    }
}