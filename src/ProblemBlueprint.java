import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;

public abstract class ProblemBlueprint {

    protected JsonObject[] data;

    public ProblemBlueprint(String filePath) {
        loadData(filePath);
    }

    private void loadData(String filePath) {

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filePath)) {

            data = gson.fromJson(reader, JsonObject[].class);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load problem file: " + filePath,
                    e
            );
        }
    }

    public abstract Chromosome createRandomChromosome();

    public abstract float calculateFitness(Subject subject);

    public void displaySubjectResults(Subject subject) {
        System.out.println("Best fitness: " + subject.getFitness());
        subject.DisplayChromosome();
        System.out.println();
    }

    public abstract void AddGraphSeries(LiveGraph graph);

    public abstract void UpdateGraph(
            LiveGraph graph,
            int generation,
            Subject bestSubject
    );
}