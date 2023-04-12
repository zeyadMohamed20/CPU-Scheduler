import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class GUI_ME {


    public static void create_And_Show(){

        //Color custom_backcolor = new Color(220,204,162);
        // Color custom_backcolor = new Color(69,73,74);
        // Color custom_backcolor = new Color(17,30,48);
          Color custom_backcolor = new Color(14,32,30);

        //============== LISTENERS =======================================

        ActionListener staticListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame n = new JFrame();
                JLabel b = new JLabel("Asaaaa");
                n.add(b);
                n.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                n.setVisible(true);
            }
        };


        //=================================================================


        JFrame mainFrame = new JFrame("Scheduler");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(1400,1000);


        //============================== LEFT PANEL ================================================

        JPanel panel_Left = new JPanel();
        panel_Left.setLayout(new BorderLayout());
        panel_Left.setBorder(BorderFactory.createEmptyBorder(10,40,10,10));


        JPanel panel_Left_Container1 = new JPanel();
        panel_Left_Container1.setLayout(new BorderLayout());
        panel_Left_Container1.setBorder(BorderFactory.createEmptyBorder(10,10,10,5));


        JPanel leftPanel_One = new JPanel();
        leftPanel_One.setLayout(new FlowLayout());
        leftPanel_One.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        JLabel label1 = new JLabel("Number of Processes: ");
        JTextField txt_Field1 = new JTextField(9);

        SpinnerNumberModel model = new SpinnerNumberModel(0,0,100,1);
        JSpinner spinner_field = new JSpinner(model);

        leftPanel_One.add(label1);
        leftPanel_One.add(spinner_field);


        JPanel leftPanel_Two = new JPanel();
        leftPanel_Two.setLayout(new FlowLayout());
        leftPanel_Two.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        JLabel label2 = new JLabel("Scheduler Type: ");
        String [] schedule_Options = {"FCFS","SJF_Preemptive", "SJF_Non Preemptive","Round_Robin", "Priority"};
        JComboBox<String> comboBox = new JComboBox<String>(schedule_Options);

        // checkbox_listner

        ItemListener checkListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int num = (int) spinner_field.getValue();

                if(num > 5){
                    JFrame n = new JFrame();
                    JLabel b = new JLabel("lollll");
                    n.add(b);
                    n.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    n.setVisible(true);
                }

                else{
                    JFrame n = new JFrame();
                    JLabel b = new JLabel("nooooo");
                    n.add(b);
                    n.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    n.setVisible(true);
                }

            }
        };

        comboBox.addItemListener(checkListener);                                 ///LISTNER
        leftPanel_Two.add(label2);
        leftPanel_Two.add(comboBox);


        panel_Left_Container1.add(leftPanel_One,BorderLayout.NORTH);
        panel_Left_Container1.add(leftPanel_Two,BorderLayout.CENTER);


        JPanel panel_Left_Container2 = new JPanel();
        panel_Left_Container2.setLayout(new BorderLayout());
        panel_Left_Container2.setBorder(BorderFactory.createEmptyBorder(100,10,10,5));

        JPanel leftPanel_three = new JPanel();
        leftPanel_three.setLayout(new BorderLayout());
        leftPanel_three.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTable table1 = new JTable(10,3);
        table1.setFillsViewportHeight(true);
        table1.setPreferredScrollableViewportSize(table1.getPreferredSize());
        table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane sp1 = new JScrollPane(table1);
        sp1.setPreferredSize(table1.getPreferredSize());

        JPanel button_pane = new JPanel();
        button_pane.setLayout(new FlowLayout(FlowLayout.CENTER,50,10));
        button_pane.setBorder(BorderFactory.createEmptyBorder(200,10,10,10));

        JButton b1 = new JButton("Static Schedule") ;
        b1.addActionListener(staticListener);                               ///LISTNER
        JButton b2 = new JButton("Dynamic Schedule");

        button_pane.add(b1);
        button_pane.add(b2);

        panel_Left_Container2.add(sp1,BorderLayout.NORTH);
        panel_Left_Container2.add(button_pane,BorderLayout.CENTER);


        panel_Left.add(panel_Left_Container1,BorderLayout.NORTH);
        panel_Left.add(panel_Left_Container2,BorderLayout.CENTER);

        //============================== RIGHT PANEL ================================================

        JPanel panel_right = new JPanel();
        panel_right.setLayout(new BorderLayout());
        panel_right.setBorder(BorderFactory.createEmptyBorder(40,50,10,90));

        JPanel times_panel = new JPanel(new FlowLayout(FlowLayout.CENTER,90,10));
        times_panel.setBorder(BorderFactory.createEmptyBorder(300,5,5,5));
        JPanel waitBox = new JPanel(new BorderLayout());
        JPanel turnTimeBox = new JPanel();
        waitBox.setLayout(new BoxLayout(waitBox,BoxLayout.Y_AXIS));
        turnTimeBox.setLayout(new BoxLayout(turnTimeBox,BoxLayout.Y_AXIS));
        JLabel label3 = new JLabel("Avg Waiting Time");
        Font f1 = new Font("Arial" ,Font.BOLD,16);
        label3.setFont(f1);
        JLabel wait_value = new JLabel("11");
        wait_value.setFont(f1);
        wait_value.setBorder(BorderFactory.createEmptyBorder(5,45,2,2));
        waitBox.add(label3);
        waitBox.add(wait_value);

        JLabel label4 = new JLabel("Avg Turn-Around Time");
        label4.setFont(f1);
        JLabel turnTime_value = new JLabel("22");
        turnTime_value.setFont(f1);
        turnTime_value.setBorder(BorderFactory.createEmptyBorder(5,60,2,2));
        turnTimeBox.add(label4);
        turnTimeBox.add(turnTime_value);

        times_panel.add(waitBox);
        times_panel.add(turnTimeBox);

        JTable process_table = new JTable(2,3);  // 2  is constant but colomn depends on process_num
        process_table.setFillsViewportHeight(true);
        process_table.setPreferredScrollableViewportSize(process_table.getPreferredSize());
        process_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane sp2 = new JScrollPane(process_table);
        sp2.setPreferredSize(process_table.getPreferredSize());

        JPanel time_container = new JPanel(new BorderLayout());
        time_container.add(times_panel);

        panel_right.add(sp2,BorderLayout.NORTH);
        panel_right.add(time_container,BorderLayout.CENTER);


        //================ COLOR ===================================

        panel_right.setBackground(custom_backcolor);
        panel_Left.setBackground(custom_backcolor);
        panel_Left_Container1.setBackground(custom_backcolor);
        panel_Left_Container2.setBackground(custom_backcolor);
        button_pane.setBackground(custom_backcolor);
        label1.setBackground(custom_backcolor);
        label2.setBackground(custom_backcolor);
        label3.setBackground(custom_backcolor);
        label4.setBackground(custom_backcolor);
        sp1.setBackground(custom_backcolor);
        sp2.setBackground(custom_backcolor);
        leftPanel_One.setBackground(custom_backcolor);
        leftPanel_Two.setBackground(custom_backcolor);
        times_panel.setBackground(custom_backcolor);
        waitBox.setBackground(custom_backcolor);
        turnTimeBox.setBackground(custom_backcolor);
        label3.setForeground(Color.BLUE);
        label4.setForeground(Color.BLUE);
        wait_value.setForeground(Color.BLUE);
        turnTime_value.setForeground(Color.BLUE);


        // option 2,3,4 only
        label1.setForeground(Color.WHITE);
        label2.setForeground(Color.WHITE);
        label3.setForeground(Color.WHITE);
        label4.setForeground(Color.WHITE);
        wait_value.setForeground(Color.WHITE);
        turnTime_value.setForeground(Color.WHITE);

        //======================== MAIN FRAME ======================================


        /*JPanel main_Panel = new JPanel();
        main_Panel.setLayout(new BorderLayout());
        main_Panel.setBorder(BorderFactory.createEmptyBorder(15,10,10,10));
        main_Panel.add(panel_Left,BorderLayout.WEST);
        main_Panel.add(panel_right,BorderLayout.EAST);*/

        JSplitPane spl = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel_Left,panel_right);
        spl.setDividerLocation(700);
        spl.setOneTouchExpandable(true);
        BasicSplitPaneUI sui = (BasicSplitPaneUI) spl.getUI() ;
        sui.getDivider().setForeground(Color.BLACK);

        mainFrame.add(spl,BorderLayout.CENTER);


        mainFrame.setBackground(custom_backcolor);

        mainFrame.setVisible(true);

        //=====================================================================



    }

    public static void main(String args[]){
        create_And_Show();
    }
}
