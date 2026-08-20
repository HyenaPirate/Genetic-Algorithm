import org.jfree.data.xy.XYSeries;

public class Knapsack extends ProblemBlueprint {

    private final float maxWeight;

    public Knapsack(String filePath, float maxWeight) {
        super(filePath);
        this.maxWeight = maxWeight;
    }

    @Override
    public Chromosome createRandomChromosome() {
        return new Chromosome.Boolean(data.length);
    }

    @Override
    public float calculateFitness(Subject subject) {

        float totalValue = 0;
        float totalWeight = 0;

        boolean[] chromosome = ((Chromosome.Boolean) subject.getChromosome()).getGenes();

        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i]) {
                totalValue += data[i].get("value").getAsFloat();
                totalWeight += data[i].get("weight").getAsFloat();
            }
        }

        float fitness;

        if (totalWeight <= maxWeight) fitness = totalValue + (maxWeight - totalWeight);
        else {
            float excessWeight = totalWeight - maxWeight;
            fitness = -excessWeight * excessWeight;
        }

        subject.setFitness(fitness);
        return fitness;
    }

    @Override
    public void displaySubjectResults(Subject subject) {

        float totalValue = 0;
        float totalWeight = 0;

        boolean[] chromosome = ((Chromosome.Boolean) subject.getChromosome()).getGenes();

        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i]) {
                totalValue += data[i].get("value").getAsFloat();
                totalWeight += data[i].get("weight").getAsFloat();
            }
        }
        System.out.println("Total monetary value gained: $" + totalValue);
        System.out.println("Total weight of items selected: " + totalWeight + " kg");
    }

    @Override
    public void AddGraphSeries(LiveGraph graph) {


        XYSeries fitness = new XYSeries("Fitness");
        XYSeries value = new XYSeries("Value");
        XYSeries weight = new XYSeries("Weight");

        graph.addSeries(fitness);
        graph.addSeries(value);
        graph.addSeries(weight);
    }

    @Override
    public void UpdateGraph(LiveGraph graph, int generation, Subject bestSubject) {

        XYSeries fitness = graph.getSeries("Fitness");
        XYSeries value = graph.getSeries("Value");
        XYSeries weight = graph.getSeries("Weight");

        float totalValue = 0;
        float totalWeight = 0;

        boolean[] chromosome = ((Chromosome.Boolean) bestSubject.getChromosome()).getGenes();

        for (int i = 0; i < chromosome.length; i++) {
            if (chromosome[i]) {
                totalValue += data[i].get("value").getAsFloat();
                totalWeight += data[i].get("weight").getAsFloat();
            }
        }

        fitness.add(generation, bestSubject.getFitness());
        value.add(generation, totalValue);
        weight.add(generation, totalWeight);

        graph.updateLabel("Fitness", bestSubject.getFitness());
        graph.updateLabel(  "Value", totalValue);
        graph.updateLabel("Weight", totalWeight);
        graph.updateInfoPanel(generation);
    }
}