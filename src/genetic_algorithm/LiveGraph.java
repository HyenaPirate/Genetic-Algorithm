package genetic_algorithm;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JFrame;
import java.util.HashMap;
import java.util.Map;

public class LiveGraph {

    private final XYPlot plot;

    private final Map<String, XYSeries> seriesMap = new HashMap<>();
    private final Map<String, Double> valueLabels = new HashMap<>();
    private final javax.swing.JPanel infoPanel =
            new javax.swing.JPanel();

    public LiveGraph() {

        infoPanel.setLayout(
                new java.awt.FlowLayout()
        );

        NumberAxis xAxis = new NumberAxis("Generation");

        plot = new XYPlot();

        plot.setDomainAxis(xAxis);

        JFreeChart chart = new JFreeChart(
                "Genetic Algorithm",
                JFreeChart.DEFAULT_TITLE_FONT,
                plot,
                true
        );

        JFrame frame = new JFrame("Genetic Algorithm");

        ChartPanel chartPanel = new ChartPanel(chart);

        javax.swing.JPanel mainPanel =
                new javax.swing.JPanel(
                        new java.awt.BorderLayout()
                );

        mainPanel.add(
                chartPanel,
                java.awt.BorderLayout.CENTER
        );

        mainPanel.add(
                infoPanel,
                java.awt.BorderLayout.SOUTH
        );

        frame.add(mainPanel);

        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void addSeries(XYSeries series) {

        int index = plot.getDatasetCount();

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        NumberAxis axis = new NumberAxis(series.getKey().toString());

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));

        plot.setDataset(index, dataset);
        plot.setRangeAxis(index, axis);
        plot.mapDatasetToRangeAxis(index, index);
        plot.setRenderer(index, renderer);

        seriesMap.put(series.getKey().toString(), series);
        valueLabels.put(series.getKey().toString(), 0.0);

        // Position axis
        if (index == 0) {
            // First axis stays on the left
            plot.setRangeAxisLocation(
                    index,
                    org.jfree.chart.axis.AxisLocation.BOTTOM_OR_LEFT
            );
        } else {
            // Other axes go on the right
            plot.setRangeAxisLocation(
                    index,
                    org.jfree.chart.axis.AxisLocation.BOTTOM_OR_RIGHT
            );
        }
    }

    public XYSeries getSeries(String name) {
        return seriesMap.get(name);
    }

    public void updateLabel(String name, double value) {
        valueLabels.put(name, value);
    }

    public void updateInfoPanel(int generation) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            infoPanel.removeAll();

            javax.swing.JLabel generationLabel =
                    new javax.swing.JLabel(
                            "Generation: " + generation
                    );

            infoPanel.add(generationLabel);

            for (Map.Entry<String, Double> entry : valueLabels.entrySet()) {

                String text =
                        entry.getKey()
                                + ": "
                                + String.format(
                                "%.2f",
                                entry.getValue()
                        );

                infoPanel.add(
                        new javax.swing.JLabel(text)
                );
            }

            infoPanel.revalidate();
            infoPanel.repaint();
        });
    }
}