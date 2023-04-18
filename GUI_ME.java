import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI_ME {

    protected static JLabel turnTime_value;
    protected static JLabel wait_value;
    protected static JLabel time_value;
    protected static JComboBox<String> comboBox;
    protected static JSpinner spinner_field;
    protected static JSpinner quantum_field;
    protected static JButton stop_resume;
    protected static JLabel labelq;
    protected static JLabel labelmsg;
    protected static JPanel leftPanel_Two;
    protected static JPanel timerJPanel;
    protected static JPanel leftPanel_four = new JPanel(new BorderLayout());
    protected static JPanel times_panel;
    protected static DefaultTableModel model1;
    protected static DefaultTableModel modelqueue;
    protected static JTable table1;
    protected static JTable queue;
    protected static JScrollPane sp1;
    protected static JScrollPane sp2;
    protected static JFrame mainFrame;

    protected static int process_count = 1;
    protected static Integer[] a = new Integer[4];
    protected static int next = 1;
    protected static Scheduler s1;
    protected static ArrayList<Process> readyQueue;
    protected static ArrayList<Gantt_Process> DynamicQueue = new ArrayList<Gantt_Process>(0);
    protected static Timer timer = null;
    protected static boolean stop = true;

    public static boolean get_data() {
        readyQueue = new ArrayList<Process>();

        for (int count = 0; count < process_count; count++) {
            int id = count + 1; // Process ID
            int arrival = 0; // Arrival Time
            int CPU_Time = 0; // CPU Burst Time
            int pri = 0; // Priority
            try {
                int temp = Integer.valueOf((String) table1.getValueAt(count, 0));
                id = (temp <= process_count) ? temp : id;
                arrival = Integer.valueOf((String) table1.getValueAt(count, 1));
                CPU_Time = Integer.valueOf((String) table1.getValueAt(count, 2));
                pri = Integer.valueOf((String) table1.getValueAt(count, 3)) == null ? 5
                        : Integer.valueOf((String) table1.getValueAt(count, 3));
            } catch (Exception e) {
                labelmsg.setText("Please Complete the process information at row: " + (count + 1));
                labelmsg.setForeground(new Color(255, 0, 0));
                return false;
            }
            Process p = new Process(id, arrival, CPU_Time, pri);

            readyQueue.add(p);
            labelmsg.setText("Data OK - Schedular is running...");
            // labelmsg.setForeground(new Color(0, 0, 255));
        }

        return true;

    }

    public static void StaticButtonPressed() {
        String schedular = comboBox.getSelectedObjects()[0].toString();
        int quantum = 1;

        sp2.setVisible(false);
        timerJPanel.setVisible(false);

        try {
            timer.stop();
        } catch (Exception e) {
            ;
        }

        if (!get_data())
            return;

        SwingUtilities.updateComponentTreeUI(mainFrame);

        if (schedular == "FCFS") {

            s1 = new FCFS(readyQueue);

        } else if (schedular == "SJF_Preemptive") {

            s1 = new SJF(readyQueue, false);

        } else if (schedular == "SJF_Non_Preemptive") {

            s1 = new SJF(readyQueue, true);

        } else if (schedular == "Round_Robin") {
            try {
                quantum_field.commitEdit();
                quantum = (Integer) quantum_field.getValue();
            } catch (java.text.ParseException e) {
                labelmsg.setText("Error while reading Quantum value - set to 1 (Default)");
                labelmsg.setForeground(new Color(255, 0, 0));
                SwingUtilities.updateComponentTreeUI(mainFrame);
            }
            s1 = new Round_Robin(readyQueue, quantum);

        } else if (schedular == "Priority_Non_Preemptive") {

            s1 = new Priority(readyQueue, false);

        } else if (schedular == "Priority_Preemptive") {

            s1 = new Priority(readyQueue, true);

        }

        s1.execute();

        leftPanel_four.removeAll();
        Gantt g = new Gantt("CPU Schedular", s1.ganttChart, process_count);
        g.setVisible(true);
        leftPanel_four.add(g);
        wait_value.setText(s1.averageWaitingTime + " s");
        turnTime_value.setText(s1.averageTurnAroundTime + " s");
        times_panel.setVisible(true);
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public static void DynamicButtonPressed() {
        next = 0;
        DynamicQueue.removeAll(DynamicQueue);
        times_panel.setVisible(false);

        try {
            timer.stop();
            stop = true;
            stop_resume.setText("Stop");
        } catch (Exception e) {
            ;
        }

        timer = new Timer(2000, new Dynamic_Gantt());
        timer.setRepeats(true);
        timer.setInitialDelay(2000);
        timer.start();

        String schedular = comboBox.getSelectedObjects()[0].toString();
        int quantum = 1;

        if (!get_data())
            return;
        for (int i = 0; i < process_count; i++) {
            modelqueue.setValueAt("", i, 0);
            modelqueue.setValueAt("", i, 1);
        }
        SwingUtilities.updateComponentTreeUI(mainFrame);

        if (schedular == "FCFS") {

            s1 = new FCFS(readyQueue);

        } else if (schedular == "SJF_Preemptive") {

            s1 = new SJF(readyQueue, true);

        } else if (schedular == "SJF_Non_Preemptive") {

            s1 = new SJF(readyQueue, false);

        } else if (schedular == "Round_Robin") {
            try {
                quantum_field.commitEdit();
                quantum = (Integer) quantum_field.getValue();
            } catch (java.text.ParseException e) {
                labelmsg.setText("Error while reading Quantum value - set to 1 (Default)");
                labelmsg.setForeground(new Color(255, 0, 0));
                SwingUtilities.updateComponentTreeUI(mainFrame);
            }
            s1 = new Round_Robin(readyQueue, quantum);

        } else if (schedular == "Priority_Non_Preemptive") {

            s1 = new Priority(readyQueue, false);

        } else if (schedular == "Priority_Preemptive") {

            s1 = new Priority(readyQueue, true);

        }

        s1.execute();

        for (Process p : readyQueue) {
            if(next == p.getArrivalTime()){
                modelqueue.setValueAt("P"+p.getProcessID(), p.getProcessID()-1, 0);
                modelqueue.setValueAt(p.getBurstTime(), p.getProcessID()-1, 1);
            }            
        }       

        leftPanel_four.removeAll();
        Gantt g = new Gantt("CPU Schedular", DynamicQueue, process_count);
        leftPanel_four.add(g);
        
        time_value.setText("Time: 0 Sec");
        wait_value.setText(s1.averageWaitingTime + " s");
        turnTime_value.setText(s1.averageTurnAroundTime + " s");

        sp2.setVisible(true);
        timerJPanel.setVisible(true);

        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public static void stopResume() {
        if (stop) {
            stop = false;
            timer.stop();
            stop_resume.setText("Resume");
        } else {
            stop = true;
            String schedular = comboBox.getSelectedObjects()[0].toString();
            int quantum = 1;
            
            if (!get_data())
                return;

            if (schedular == "FCFS") {

                s1 = new FCFS(readyQueue);

            } else if (schedular == "SJF_Preemptive") {

                s1 = new SJF(readyQueue, true);

            } else if (schedular == "SJF_Non_Preemptive") {

                s1 = new SJF(readyQueue, false);

            } else if (schedular == "Round_Robin") {
                try {
                    quantum_field.commitEdit();
                    quantum = (Integer) quantum_field.getValue();
                } catch (java.text.ParseException e) {
                    labelmsg.setText("Error while reading Quantum value - set to 1 (Default)");
                    labelmsg.setForeground(new Color(255, 0, 0));
                    SwingUtilities.updateComponentTreeUI(mainFrame);
                }
                s1 = new Round_Robin(readyQueue, quantum);

            } else if (schedular == "Priority_Non_Preemptive") {

                s1 = new Priority(readyQueue, false);

            } else if (schedular == "Priority_Preemptive") {

                s1 = new Priority(readyQueue, true);

            }

            if(next == s1.readyQueue.get(process_count-1).getArrivalTime()){
                modelqueue.setValueAt("P"+s1.readyQueue.get(process_count-1).getProcessID(), s1.readyQueue.get(process_count-1).getProcessID()-1, 0);
                modelqueue.setValueAt(s1.readyQueue.get(process_count-1).getBurstTime(), s1.readyQueue.get(process_count-1).getProcessID()-1, 1);
            }  
                      
            s1.execute();
            timer.start();
            stop_resume.setText("Stop");
        }

        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public static void repeat() {
        if (next == s1.ganttChart.size() - 1) {
            timer.stop();
            times_panel.setVisible(true);
            timerJPanel.setVisible(false);
        }

        DynamicQueue.addAll(s1.ganttChart.subList(next, ++next));
        Gantt g = new Gantt("CPU Schedular", DynamicQueue, process_count);
        leftPanel_four.removeAll();
        leftPanel_four.add(g);
        time_value.setText("Time: "+next+" Sec");

        for (Process p : readyQueue) {
            if(next == p.getArrivalTime()){
                modelqueue.setValueAt("P"+p.getProcessID(), p.getProcessID()-1, 0);
                modelqueue.setValueAt(p.getBurstTime(), p.getProcessID()-1, 1);
            }            
        }        
        
        int id = DynamicQueue.get(next-1).getProcessId()-1;
        if(id >= 0){
            Integer burst;
            try {
                burst = (Integer) modelqueue.getValueAt(id, 1);
            } catch (Exception e) {
                burst = Integer.valueOf((String) modelqueue.getValueAt(id, 1));
            }
            modelqueue.setValueAt(burst-1, id, 1);        
        }

        SwingUtilities.updateComponentTreeUI(mainFrame);

    }

    public static void SchedulerChange() {
        String schedular = comboBox.getSelectedObjects()[0].toString();
        if (schedular == "Round_Robin") {
            leftPanel_Two.add(labelq);
            leftPanel_Two.add(quantum_field);
        } else {
            leftPanel_Two.remove(labelq);
            leftPanel_Two.remove(quantum_field);
        }
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public static void ProccessChange() {

        try {
            spinner_field.commitEdit();
            process_count = (Integer) spinner_field.getValue();
        } catch (java.text.ParseException e) {
            ; // Do Nothing
        }
        for (int i = table1.getRowCount(); i < process_count; i++) {
            model1.addRow(a);
            table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
            sp1.setPreferredSize(new Dimension(600, (process_count + 1) * table1.getRowHeight()));
            modelqueue.addRow(new Integer[2]);
            queue.setPreferredScrollableViewportSize(queue.getPreferredSize());
            int height = (process_count + 1) * table1.getRowHeight();
            sp2.setPreferredSize(new Dimension(600,(height > 300)?300:height));
        }

        SwingUtilities.updateComponentTreeUI(mainFrame);

    }

    public static void create_And_Show() {

        // Color custom_backcolor = new Color(220,204,162);
        Color custom_backcolor = new Color(69, 73, 74);
        // Color custom_backcolor = new Color(17,30,48);
        // Color custom_backcolor = new Color(14,32,30);

        // =================================================================

        mainFrame = new JFrame("Scheduler");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1400, 1000);

        // =================================== Fonts
        // ================================================

        Font f = new Font("Arial", Font.PLAIN, 14);
        Font msg = new Font("Arial", Font.BOLD, 16);
        Font header = new Font("Serif", Font.BOLD, 22);


        // ============================== LEFT PANEL
        // ================================================

        JPanel panel_Left = new JPanel();
        panel_Left.setLayout(new BoxLayout(panel_Left, BoxLayout.Y_AXIS));
        panel_Left.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel panel_Left_Container1 = new JPanel();
        panel_Left_Container1.setLayout(new BorderLayout());
        panel_Left_Container1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));

        JPanel leftPanel_One = new JPanel();
        leftPanel_One.setLayout(new BoxLayout(leftPanel_One, BoxLayout.Y_AXIS));
        leftPanel_One.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JPanel leftPanel_One_1 = new JPanel();
        leftPanel_One_1.setLayout(new FlowLayout());
        leftPanel_One_1.setBorder(BorderFactory.createEmptyBorder(20, 3, 3, 3));

        JLabel label0 = new JLabel("Input Panel");
        label0.setFont(header);
        JLabel label1 = new JLabel("Number of Processes: ");
        labelq = new JLabel("Quantum: ");

        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
        SpinnerNumberModel model2 = new SpinnerNumberModel(1, 1, 100, 1);
        spinner_field = new JSpinner(model);
        quantum_field = new JSpinner(model2);
        spinner_field.addChangeListener(e -> ProccessChange());

        leftPanel_One_1.add(label1);
        leftPanel_One_1.add(spinner_field);
        leftPanel_One_1.setFont(f);

        leftPanel_One.add(label0);
        leftPanel_One.add(leftPanel_One_1);

        leftPanel_Two = new JPanel();
        leftPanel_Two.setLayout(new FlowLayout());
        leftPanel_Two.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        JLabel label2 = new JLabel("Scheduler Type: ");
        String[] schedule_Options = { "--Choose Schedular--", "FCFS", "SJF_Preemptive", "SJF_Non_Preemptive",
                "Round_Robin", "Priority_Non_Preemptive", "Priority_Preemptive" };
        comboBox = new JComboBox<String>(schedule_Options);
        comboBox.addActionListener(e -> SchedulerChange());

        leftPanel_Two.add(label2);
        leftPanel_Two.add(comboBox);

        JPanel leftPanel_three = new JPanel();
        leftPanel_three.setLayout(new BorderLayout());
        leftPanel_three.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        labelmsg = new JLabel();
        labelmsg.setFont(msg);

        leftPanel_three.add(labelmsg);

        panel_Left_Container1.add(leftPanel_One, BorderLayout.NORTH);
        panel_Left_Container1.add(leftPanel_Two, BorderLayout.CENTER);
        panel_Left_Container1.add(leftPanel_three, BorderLayout.AFTER_LAST_LINE);

        JPanel panel_Left_Container2 = new JPanel();
        panel_Left_Container2.setLayout(new BoxLayout(panel_Left_Container2, BoxLayout.Y_AXIS));
        panel_Left_Container2.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        model1 = new DefaultTableModel();
        table1 = new JTable(model1);
        model1.addColumn("Proccess ID");
        model1.addColumn("Arrival Time");
        model1.addColumn("CPU Burst Time");
        model1.addColumn("Priority");
        model1.addRow(a);
        table1.setRowHeight(20);
        table1.setFillsViewportHeight(true);
        table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        sp1 = new JScrollPane(table1);
        sp1.setPreferredSize(new Dimension(600, table1.getRowHeight() * 2));

        JPanel button_pane = new JPanel();
        button_pane.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        button_pane.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        JButton b1 = new JButton("Static Schedule");
        JButton b2 = new JButton("Dynamic Schedule");
        b1.addActionListener(e -> StaticButtonPressed());
        b2.addActionListener(e -> DynamicButtonPressed());
        button_pane.add(b1);
        button_pane.add(b2);

        panel_Left_Container2.add(sp1, BorderLayout.NORTH);
        panel_Left_Container2.add(button_pane, BorderLayout.CENTER);

        panel_Left.add(panel_Left_Container1, BorderLayout.NORTH);
        panel_Left.add(panel_Left_Container2, BorderLayout.CENTER);

        // ============================== RIGHT PANEL
        // ================================================

        JPanel panel_right = new JPanel();
        panel_right.setLayout(new BoxLayout(panel_right, BoxLayout.Y_AXIS));
        panel_right.setBorder(BorderFactory.createEmptyBorder(10, 50, 100, 90));

        JLabel label0_0 = new JLabel("Output Panel");
        label0_0.setFont(header);

        JPanel right_label = new JPanel(new FlowLayout(FlowLayout.CENTER));
        right_label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        right_label.add(label0_0, BorderLayout.CENTER);

        JPanel waitBox = new JPanel(new BorderLayout());
        JPanel turnTimeBox = new JPanel();
        waitBox.setLayout(new BoxLayout(waitBox, BoxLayout.Y_AXIS));
        turnTimeBox.setLayout(new BoxLayout(turnTimeBox, BoxLayout.Y_AXIS));

        JLabel label3 = new JLabel("Avg Waiting Time");
        label3.setFont(msg);
        wait_value = new JLabel("11");
        wait_value.setFont(msg);
        wait_value.setBorder(BorderFactory.createEmptyBorder(5, 45, 2, 2));

        waitBox.add(label3);
        waitBox.add(wait_value);

        JLabel label4 = new JLabel("Avg Turn-Around Time");
        label4.setFont(msg);
        turnTime_value = new JLabel("22");
        turnTime_value.setFont(msg);
        turnTime_value.setBorder(BorderFactory.createEmptyBorder(5, 60, 2, 2));

        turnTimeBox.add(label4);
        turnTimeBox.add(turnTime_value);

        leftPanel_four.setLayout(new BoxLayout(leftPanel_four, BoxLayout.Y_AXIS));
        leftPanel_four.setBorder(BorderFactory.createEmptyBorder(5, 2, 20, 2));

        times_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 90, 10));
        times_panel.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));

        times_panel.add(waitBox);
        times_panel.add(turnTimeBox);
        times_panel.setVisible(false);

        modelqueue = new DefaultTableModel();
        queue = new JTable(modelqueue);
        modelqueue.addColumn("Process in Ready Queue"); 
        modelqueue.addColumn("Remaining Burst Time"); 
        modelqueue.addRow(new Integer[2]);
        queue.setRowHeight(20);
        queue.setFillsViewportHeight(true);
        queue.setPreferredScrollableViewportSize(queue.getPreferredSize());
        queue.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        sp2 = new JScrollPane(queue);
        sp2.setPreferredSize(new Dimension(600, queue.getRowHeight()*2));
        sp2.setVisible(false);

        JPanel time_container = new JPanel(new BorderLayout());
        time_container.add(times_panel);
        time_container.setVisible(true);

        time_value = new JLabel("Time: 0 Sec");
        time_value.setFont(msg);

        stop_resume = new JButton("Stop");
        stop_resume.addActionListener(e -> stopResume());

        timerJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 90, 10));
        timerJPanel.add(time_value);
        timerJPanel.add(stop_resume);
        timerJPanel.setVisible(false);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(timerJPanel);
        container.add(time_container);
        container.setVisible(true);

        
        panel_right.add(right_label, BorderLayout.PAGE_START);
        panel_right.add(leftPanel_four);
        panel_right.add(container);
        panel_right.add(sp2, BorderLayout.CENTER);


        // ================ COLOR ===================================
        leftPanel_four.setBackground(custom_backcolor);
        panel_right.setBackground(custom_backcolor);
        panel_Left.setBackground(custom_backcolor);
        panel_Left_Container1.setBackground(custom_backcolor);
        panel_Left_Container2.setBackground(custom_backcolor);
        button_pane.setBackground(custom_backcolor);
        label1.setBackground(custom_backcolor);
        labelmsg.setBackground(custom_backcolor);
        label2.setBackground(custom_backcolor);
        label3.setBackground(custom_backcolor);
        label4.setBackground(custom_backcolor);
        sp1.setBackground(custom_backcolor);
        container.setBackground(custom_backcolor);
        leftPanel_One.setBackground(custom_backcolor);
        leftPanel_One_1.setBackground(custom_backcolor);
        leftPanel_Two.setBackground(custom_backcolor);
        leftPanel_three.setBackground(custom_backcolor);
        times_panel.setBackground(custom_backcolor);
        right_label.setBackground(custom_backcolor);
        waitBox.setBackground(custom_backcolor);
        turnTimeBox.setBackground(custom_backcolor);
        label3.setForeground(Color.BLUE);
        label4.setForeground(Color.BLUE);
        wait_value.setForeground(Color.BLUE);
        turnTime_value.setForeground(Color.BLUE);
        time_container.setBackground(custom_backcolor);
        timerJPanel.setBackground(custom_backcolor);
        sp2.setBackground(custom_backcolor);

        // option 2,3,4 only

        label0.setForeground(Color.WHITE);
        label0_0.setForeground(Color.WHITE);
        label1.setForeground(Color.WHITE);
        label2.setForeground(Color.WHITE);
        label3.setForeground(Color.WHITE);
        label4.setForeground(Color.WHITE);
        labelq.setForeground(Color.WHITE);
        labelmsg.setForeground(Color.WHITE);
        wait_value.setForeground(Color.WHITE);
        turnTime_value.setForeground(Color.WHITE);
        time_value.setForeground(Color.WHITE);

        // ======================== MAIN FRAME ======================================

        /*
         * JPanel main_Panel = new JPanel();
         * main_Panel.setLayout(new BorderLayout());
         * main_Panel.setBorder(BorderFactory.createEmptyBorder(15,10,10,10));
         * main_Panel.add(panel_Left,BorderLayout.WEST);
         * main_Panel.add(panel_right,BorderLayout.EAST);
         */

        JSplitPane spl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel_Left, panel_right);
        spl.setDividerLocation(700);
        spl.setOneTouchExpandable(true);
        BasicSplitPaneUI sui = (BasicSplitPaneUI) spl.getUI();
        sui.getDivider().setForeground(Color.BLACK);

        mainFrame.add(spl, BorderLayout.CENTER);

        mainFrame.setBackground(custom_backcolor);

        mainFrame.setVisible(true);

        // =====================================================================

    }

    public static void main(String args[]) {
        create_And_Show();
    }
}

class Dynamic_Gantt implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        GUI_ME.repeat();
    }
}