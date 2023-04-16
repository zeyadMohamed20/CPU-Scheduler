import java.util.ArrayList;

public class FCFS extends  Scheduler
{
    public FCFS(ArrayList<Process> readyQueue)
    {
        super(readyQueue);
    }

    @Override
    public void get_GanttChart()
    {
        return;
    }
}
