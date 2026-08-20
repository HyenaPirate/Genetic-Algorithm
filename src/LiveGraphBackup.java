import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class LiveGraphBackup {

    private static final int VISIBLE_GENERATIONS = 200;

    private final XYSeries bestFitnessSeries;
    private final XYSeries bestValueSeries;
    private final XYSeries bestWeightSeries;

    private final XYPlot plot;
    private final JLabel fitnessLabel;
    private final JLabel valueLabel;
    private final JLabel weightLabel;

    private boolean showLatest = true;
    private int latestGeneration = 0;

    public LiveGraphBackup() {

        // --------------------------------
        // Series
        // --------------------------------

        bestFitnessSeries = new XYSeries("Best Fitness");
        bestValueSeries = new XYSeries("Best Value ($)");
        bestWeightSeries = new XYSeries("Best Weight (kg)");

        // --------------------------------
        // Datasets
        // --------------------------------

        XYSeriesCollection fitnessDataset = new XYSeriesCollection();
        fitnessDataset.addSeries(bestFitnessSeries);

        XYSeriesCollection valueDataset = new XYSeriesCollection();
        valueDataset.addSeries(bestValueSeries);

        XYSeriesCollection weightDataset = new XYSeriesCollection();
        weightDataset.addSeries(bestWeightSeries);

        // --------------------------------
        // Chart
        // --------------------------------

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Genetic Algorithm",
                "Generation",
                "Fitness",
                fitnessDataset
        );

        plot = chart.getXYPlot();

        // --------------------------------
        // Fitness axis - LEFT
        // --------------------------------

        NumberAxis fitnessAxis = new NumberAxis("Fitness");
        plot.setRangeAxis(0, fitnessAxis);

        // --------------------------------
        // Value axis - RIGHT
        // --------------------------------

        NumberAxis valueAxis = new NumberAxis("Value ($)");
        plot.setRangeAxis(1, valueAxis);

        plot.setDataset(1, valueDataset);
        plot.mapDatasetToRangeAxis(1, 1);

        // --------------------------------
        // Weight axis - RIGHT
        // --------------------------------

        NumberAxis weightAxis = new NumberAxis("Weight (kg)");
        plot.setRangeAxis(2, weightAxis);

        plot.setDataset(2, weightDataset);
        plot.mapDatasetToRangeAxis(2, 2);

        // --------------------------------
        // Renderers
        // --------------------------------

        XYLineAndShapeRenderer fitnessRenderer =
                new XYLineAndShapeRenderer(true, false);

        XYLineAndShapeRenderer valueRenderer =
                new XYLineAndShapeRenderer(true, false);

        XYLineAndShapeRenderer weightRenderer =
                new XYLineAndShapeRenderer(true, false);

        plot.setRenderer(0, fitnessRenderer);
        plot.setRenderer(1, valueRenderer);
        plot.setRenderer(2, weightRenderer);

        // --------------------------------
        // Toggle button
        // --------------------------------

        JButton toggleButton = new JButton("Show All");

        toggleButton.addActionListener(e -> {

            showLatest = !showLatest;

            if (showLatest) {
                toggleButton.setText("Show All");
                updateLatestView();
            } else {
                toggleButton.setText("Show Latest");
                showEntireGraph();
            }
        });

        // --------------------------------
        // Window
        // --------------------------------

        JFrame frame = new JFrame("Genetic Algorithm");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel statsPanel = new JPanel();

        fitnessLabel = new JLabel("Fitness: 0");
        valueLabel = new JLabel("Value: $0");
        weightLabel = new JLabel("Weight: 0 kg");

        statsPanel.add(fitnessLabel);
        statsPanel.add(valueLabel);
        statsPanel.add(weightLabel);

        panel.add(statsPanel, BorderLayout.NORTH);

        panel.add(new ChartPanel(chart), BorderLayout.CENTER);
        panel.add(toggleButton, BorderLayout.SOUTH);

        frame.add(panel);

        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void Update(
            int generation,
            float bestFitness,
            float bestValue,
            float bestWeight
    ) {

        latestGeneration = generation;

        // Best subject only
        bestFitnessSeries.add(generation, bestFitness);
        bestValueSeries.add(generation, bestValue);
        bestWeightSeries.add(generation, bestWeight);

        // Keep the graph following the latest generations
        if (showLatest) {
            updateLatestView();
        }
        fitnessLabel.setText(String.format(
                "Fitness: %.2f",
                bestFitness
        ));

        valueLabel.setText(String.format(
                "Value: $%.2f",
                bestValue
        ));

        weightLabel.setText(String.format(
                "Weight: %.2f kg",
                bestWeight
        ));
    }

    private void updateLatestView() {

        int lowerBound = Math.max(
                0,
                latestGeneration - VISIBLE_GENERATIONS
        );

        int upperBound = Math.max(
                VISIBLE_GENERATIONS,
                latestGeneration
        );

        plot.getDomainAxis().setRange(
                lowerBound,
                upperBound
        );
    }

    private void showEntireGraph() {

        plot.getDomainAxis().setAutoRange(true);
    }
}