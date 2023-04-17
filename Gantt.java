import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.xy.IntervalXYDataset;


public class Gantt extends JPanel {


    public static ArrayList<Gantt_Process> ganttChart;
    public static int p_no;
    public static int count;
    public static String[] process;

    /**
     * Constructs the demo application.
     *
     * @param title  the frame title.
     */
    public Gantt(String title, ArrayList<Gantt_Process> gantt, int n) {
        //super(title);
        ganttChart = gantt;
        p_no = n;


        process = new String[n+1];
        for (int i = 0; i <= n; i++) {
            if(i==0){
                process[i] = "IDLE";
                continue;
            }
            process[i] = "P"+i;
        }

        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        add(chartPanel);
        //setContentPane(chartPanel);
    }

    /**
     * Creates the chart.
     *
     * @param dataset  the dataset.
     *
     * @return A chart.
     */
    private static JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart("CPU Gantt Chart",
                "Resource", false, "Time", dataset,
                PlotOrientation.HORIZONTAL,
                true, false, false);

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setRangePannable(true);
        SymbolAxis xAxis = new SymbolAxis("Process", process);
        xAxis.setGridBandsVisible(true);
        plot.setDomainAxis(xAxis);
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setUseYInterval(true);
        plot.setRangeAxis(new NumberAxis("Time"));
        plot.getRangeAxis().setAutoRange(true);
        ChartUtilities.applyCurrentTheme(chart);

        return chart;
    }

    /**
     * Creates a panel for the demo.
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    /**
     * Creates the sample dataset.
     *
     * @return The sample dataset.
     */
    private static IntervalXYDataset createDataset() {
        return new XYTaskDataset(createTasks());
    }

    /**
     * Creates a collection of tasks.
     *
     * @return A collection of tasks.
     */
    private static TaskSeriesCollection createTasks() {
        TaskSeriesCollection dataset = new TaskSeriesCollection();

        TaskSeries[] ss = new TaskSeries[p_no+1];

        for (int i = 0; i<=p_no; i++){
            if(i==0){
                ss[i] = new TaskSeries("IDLE");
                continue;
            }
            ss[i] = new TaskSeries("P"+i);
        }

        for (int i = 0; i < ganttChart.size(); i++)
            ss[ganttChart.get(i).processId].add(new Task("P"+i+ganttChart.get(i).processId, new SimpleTimePeriod(ganttChart.get(i).startTime, ganttChart.get(i).endTime)));
        

        for (TaskSeries taskSeries : ss)
            dataset.add(taskSeries);
        

        return dataset;
    }


    public static void main(String[] args) {
        ArrayList<Gantt_Process> gg = new ArrayList<Gantt_Process>();
        Gantt_Process p1 = new Gantt_Process(1,0,3);
        Gantt_Process p2 = new Gantt_Process(2,3,5);
        Gantt_Process p3 = new Gantt_Process(3,5,6);
        Gantt_Process p4 = new Gantt_Process(1,7,8);
        Gantt_Process p5 = new Gantt_Process(4,8,10);
        Gantt_Process p6 = new Gantt_Process(1,10,13);
        gg.add(p5);
        gg.add(p3);
        gg.add(p2);

        gg.add(p1);        
        gg.add(p6);
        gg.add(p4);

        Gantt demo = new Gantt(
                "JFreeChart : XYTaskDatasetDemo1.java", gg, 4);
        //demo.pack();
        //RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}