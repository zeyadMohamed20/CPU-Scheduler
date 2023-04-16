import java.util.ArrayList;
import java.util.Scanner;

public class Program {

    public static void main(String args[])
    {
        System.out.println("****Round_Robin Scheduler****");
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Quantum time : ");
        int quantam = input.nextInt() ;
        System.out.print("Enter number of processes: ");
        int n = input.nextInt();
        ArrayList<Process>processes = new ArrayList<Process>();
        int i = 0;
        while(i < n)
        {
            Process process = new Process();
            System.out.println("p" + (i + 1));
            System.out.print("Arrival time: ");
            process.setArrivalTime(input.nextInt());
            System.out.print("Burst time: ");
            process.setBurstTime(input.nextInt());
            process.setProcessID(i + 1);
            i++;
            processes.add(process);
        }
        System.out.println("Process ID\t\tArrival Time\t\tBurst Time");
        System.out.println("----------------------------------------------------");
        i = 0;
        while(i < processes.size())
        {
            System.out.println(processes.get(i).getProcessID() + "\t\t\t" +
                    processes.get(i).getArrivalTime() + "\t\t\t" +
                    processes.get(i).getBurstTime());
            i++;
        }

        Round_Robin omar = new Round_Robin(processes, quantam);
        ArrayList<Gantt_Process> ganttprocesses = omar.get_GanttChart();

        i = 0;
        System.out.println("\n***GanttChart***");
        while(i < ganttprocesses.size())
        {
            System.out.print("<" + ganttprocesses.get(i).startTime + "," +
                    ganttprocesses.get(i).endTime + "> " +
                    "p" + ganttprocesses.get(i).getProcessId());
            if(i != ganttprocesses.size() - 1)
                System.out.print("  |  ");
            i++;
        }
        input.close();
    }
}