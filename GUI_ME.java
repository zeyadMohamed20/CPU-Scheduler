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
    protected static String schedular;
    protected static Integer[] a = new Integer[4];
    protected static int next = 0;
    protected static int change = 0;
    protected static Scheduler s1;
    protected static ArrayList<Process> readyQueue;
    protected static ArrayList<Gantt_Process> DynamicQueue = new ArrayList<Gantt_Process>(0);
    protected static Timer timer = null;
    protected static boolean stop = true;

    public static boolean get_data() {
        readyQueue = new ArrayList<Process>();

        for (int count = 0; count < process_count; count++) {
            Integer id = count + 1;     // Process ID
            Integer arrival = 0;        // Arrival Time
            Integer CPU_Time = 0;       // CPU Burst Time
            Integer pri = 0;            // Priority
            try {
                arrival = Integer.valueOf((String) table1.getValueAt(count, 1));
                if(arrival < 0){
                    labelmsg.setText("Arrival Time is set to less than Zero at proccess: " + (id));
                    labelmsg.setForeground(Color.RED);
                    return false;
                }
                CPU_Time = Integer.valueOf((String) table1.getValueAt(count, 2));
                if(CPU_Time <= 0){
                    labelmsg.setText("CPU Burst Time is set to Zero or less at proccess: " + (id));
                    labelmsg.setForeground(Color.RED);
                    return false;
                }
                if (schedular == "Priority_Preemptive" || schedular == "Priority_Non_Preemptive") {
                    pri = Integer.valueOf((String) table1.getValueAt(count, 3));
                    if(pri < 0)
                        pri = 0;
                    else if(pri > 127)
                        pri = 127;
                    model1.setValueAt(pri.toString(), count, 3);
                }
            } catch (Exception e) {
                StackTraceElement[] a = e.getStackTrace();
                switch(a[2].toString().substring(28, 30)){
                    case "53":
                        labelmsg.setText("Missing (Arrival Time) of proccess: " + (id) + " - Please press Enter in the cell");
                        break;
                    case "59":
                        labelmsg.setText("Missing (CPU Burst Time) of proccess: " + (id) + " - Please press Enter in the cell");
                        break;
                    case "66":
                        labelmsg.setText("Missing (Priority) of proccess: " + (id) + " - Please press Enter in the cell");
                        break;
                    default:
                        labelmsg.setText("Missing/Wrong Information at row: " + (count + 1) + " - Please press Enter in each cell");
                }
                labelmsg.setForeground(Color.RED);
                return false;
            }
            Process p = new Process(id, arrival, CPU_Time, pri);

            readyQueue.add(p);
            labelmsg.setText("Data OK - Schedular is running...");
            labelmsg.setForeground(Color.WHITE);
        }

        return true;

    }

    public static boolean intializScheduler(){
        int quantum = 1;
        if (schedular == "FCFS") {

            s1 = new FCFS(readyQueue);

        } else if (schedular == "SJF_Preemptive") {

            s1 = new SJF(readyQueue,true);

        } else if (schedular == "SJF_Non_Preemptive") {

            s1 = new SJF(readyQueue, false);

        } else if (schedular == "Round_Robin") {
            try {
                quantum_field.commitEdit();
                quantum = (Integer) quantum_field.getValue();
            } catch (java.text.ParseException e) {
                labelmsg.setText("Error while reading Quantum value - set to 1 (Default)");
                labelmsg.setForeground(Color.RED);
            }
            s1 = new Round_Robin(readyQueue, quantum);

        } else if (schedular == "Priority_Non_Preemptive") {

            s1 = new Priority(readyQueue, false);

        } else if (schedular == "Priority_Preemptive") {

            s1 = new Priority(readyQueue, true);

        }else{
            labelmsg.setText("Please Select a Scheduler");
            labelmsg.setForeground(Color.RED);
            return false;
        }
        return true;
    }

    public static void StaticButtonPressed() {
        change = 0;
        quantum_field.setEnabled(true);
        comboBox.setEnabled(true);
        spinner_field.setEnabled(true);
        table1.setEnabled(true);
        sp2.setVisible(false);
        timerJPanel.setVisible(false);
        leftPanel_four.removeAll();
        times_panel.setVisible(false);

        try {
            stop = true;
            timer.stop();
            stop_resume.setText("Stop");
        } catch (Exception e) {
            ;
        }

        if (!get_data())
            return;

        if(!intializScheduler()) 
            return ;

        s1.execute();

        Gantt g = new Gantt("CPU Schedular", s1.ganttChart, process_count);
        g.setVisible(true);
        leftPanel_four.add(g);
        wait_value.setText(s1.averageWaitingTime + " s");
        turnTime_value.setText(s1.averageTurnAroundTime + " s");
        times_panel.setVisible(true);
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    public static void DynamicButtonPressed() {
        change = 0;
        next = 0;
        DynamicQueue.removeAll(DynamicQueue);
        times_panel.setVisible(false);
        leftPanel_four.removeAll();

        try {
            stop = true;
            timer.stop();
            stop_resume.setText("Stop");
        } catch (Exception e) {
            ;
        }

        if (!get_data())
            return;
        for (int i = 0; i < process_count; i++) {
            modelqueue.setValueAt("", i, 0);
            modelqueue.setValueAt("", i, 1);
        }

        if(!intializScheduler()) 
            return ;
            
        quantum_field.setEnabled(false);
        comboBox.setEnabled(false);
        spinner_field.setEnabled(false);
        table1.setEnabled(false);

        s1.execute();

        timer = new Timer(1000, new Dynamic_Gantt());
        timer.setRepeats(true);
        timer.setInitialDelay(2000);
        timer.start();

        for (Process p : readyQueue) {
            if(next == p.getArrivalTime()){
                modelqueue.setValueAt("P"+p.getProcessID(), p.getProcessID()-1, 0);
                modelqueue.setValueAt(p.getBurstTime(), p.getProcessID()-1, 1);
            }            
        }       

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
            spinner_field.setEnabled(true);
            table1.setEnabled(true);
            stop = false;
            timer.stop();
            stop_resume.setText("Resume");
        } else {
            
            if (!get_data())
                return ;

            if(!intializScheduler()) 
                return ;

            for (; change > 0; change--) {
                if(next == s1.readyQueue.get(process_count-change).getArrivalTime()){
                    modelqueue.setValueAt("P"+s1.readyQueue.get(process_count-change).getProcessID(), s1.readyQueue.get(process_count-change).getProcessID()-1, 0);
                    modelqueue.setValueAt(s1.readyQueue.get(process_count-change).getBurstTime(), s1.readyQueue.get(process_count-change).getProcessID()-1, 1);
                }else if(next > s1.readyQueue.get(process_count-change).getArrivalTime()){
                    labelmsg.setText("Arrival Time is set to less than current time at proccess: " + (process_count-change));
                    labelmsg.setForeground(Color.RED);
                    return ;                
                }              
            }

            stop = true;
            spinner_field.setEnabled(false);
            table1.setEnabled(false);
            
                      
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
            quantum_field.setEnabled(true);
            comboBox.setEnabled(true);
            spinner_field.setEnabled(true);
            table1.setEnabled(true);
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
        schedular = comboBox.getSelectedObjects()[0].toString();
        if (schedular == "Round_Robin") {
            leftPanel_Two.add(labelq);
            leftPanel_Two.add(quantum_field);
        } else {
            leftPanel_Two.remove(labelq);
            leftPanel_Two.remove(quantum_field);
        }
        if (schedular == "Priority_Preemptive" || schedular == "Priority_Non_Preemptive") {
            table1.getColumnModel().getColumn(3).setPreferredWidth(table1.getColumnModel().getColumn(0).getWidth());
            table1.getColumnModel().getColumn(3).setMaxWidth(table1.getColumnModel().getColumn(0).getMaxWidth());
            table1.getColumnModel().getColumn(3).setMinWidth(table1.getColumnModel().getColumn(0).getMinWidth());
            table1.getColumnModel().getColumn(3).setResizable(true);
        }else{
            table1.getColumnModel().getColumn(3).setPreferredWidth(0);
            table1.getColumnModel().getColumn(3).setMaxWidth(0);
            table1.getColumnModel().getColumn(3).setMinWidth(0);
            table1.getColumnModel().getColumn(3).setResizable(false);
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
            change++;
            a[0]++;
            model1.addRow(a);
            table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
            int height = (process_count + 1) * table1.getRowHeight();
            sp1.setPreferredSize(new Dimension(600, (height > 500)?500:height));
            modelqueue.addRow(new Integer[2]);
            queue.setPreferredScrollableViewportSize(queue.getPreferredSize());
            sp2.setPreferredSize(new Dimension(600,(height > 300)?300:height));
        }
        for(int i = table1.getRowCount()-1; i >= process_count ; i--){
            change++;
            a[0]--;
            model1.removeRow(i);;
            table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
            int height = (process_count + 1) * table1.getRowHeight();
            sp1.setPreferredSize(new Dimension(600, (height > 500)?500:height));
            modelqueue.removeRow(i);;
            queue.setPreferredScrollableViewportSize(queue.getPreferredSize());
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
        panel_Left.setLayout(new BorderLayout());
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
        table1 = new JTable(model1){
            public boolean isCellEditable(int row, int column) {                
                return column != 0;               
            };
        };
        model1.addColumn("Proccess ID");
        model1.addColumn("Arrival Time");
        model1.addColumn("CPU Burst Time");
        model1.addColumn("Priority");
        table1.putClientProperty("terminateEditOnFocusLost", true);
        
        a[0] = 1;
        model1.addRow(a);
        table1.setRowHeight(20);
        table1.setFillsViewportHeight(true);
        table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        sp1 = new JScrollPane(table1);
        sp1.setPreferredSize(new Dimension(600, table1.getRowHeight() * 2));
        sp1.setMaximumSize(new Dimension(600, 25*table1.getRowHeight()));
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
        JScrollPane pl = new JScrollPane(panel_Left);
        pl.setPreferredSize(new Dimension(700, 1000));
        pl.setVisible(true);
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
        queue.setEnabled(false);
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
        JScrollPane pr = new JScrollPane(panel_right);
        pr.setPreferredSize(new Dimension(700, 1000));
        pr.setVisible(true);

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
        pl.setBackground(custom_backcolor);
        pr.setBackground(custom_backcolor);

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

        JSplitPane spl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pl, pr);
        spl.setDividerLocation(700);
        spl.setOneTouchExpandable(true);
        BasicSplitPaneUI sui = (BasicSplitPaneUI) spl.getUI();
        sui.getDivider().setForeground(Color.BLACK);

        mainFrame.add(spl, BorderLayout.CENTER);

        mainFrame.setBackground(custom_backcolor);

        mainFrame.setVisible(true);


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