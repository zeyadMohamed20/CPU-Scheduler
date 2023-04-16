import java.util.ArrayList;
import java.util.Scanner;

public class Test_Priority
{
    public void execute_priority()
    {
        System.out.println("****Priority Non premeptive Scheduler****");
        Scanner input = new Scanner(System.in);
        System.out.print("Preemptive (true or false): ");
        boolean preemptionOption = input.nextBoolean();
        System.out.print("Enter number of processes: ");
        int n = input.nextInt();
        ArrayList<Process> processes = new ArrayList<Process>();
        int i = 1;
        while(i <= n)
        {
            Process process = new Process();
            System.out.println("p" + i);
            System.out.print("Arrival time: ");
            process.setArrivalTime(input.nextInt());
            System.out.print("Burst time: ");
            process.setBurstTime(input.nextInt());
            System.out.print("Priority: ");
            process.setPriority(input.nextInt());
            process.setProcessID(i);
            i++;
            processes.add(process);
        }
        System.out.println("Process ID\t\tArrival Time\t\tBurst Time\t\tPriority");
        System.out.println("----------------------------------------------------");
        i = 0;
        while(i < processes.size())
        {
            System.out.println(processes.get(i).getProcessID() + "\t\t\t" +
                    processes.get(i).getArrivalTime() + "\t\t\t" +
                    processes.get(i).getBurstTime() + "\t\t\t" +
                    processes.get(i).getPriority());
            i++;
        }
        Scheduler s1 = new Priority(processes, preemptionOption);
        ArrayList<Gantt_Process>ganttprocesses = s1.get_GanttChart();
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
