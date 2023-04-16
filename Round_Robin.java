import java.util.ArrayList;

public class Round_Robin extends  Scheduler
{
    public Round_Robin(ArrayList<Process> readyQueue, int quantum)
    {
        super(readyQueue, quantum);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        ArrayList<Process> Queue;
        Queue = new ArrayList<Process>() ;
        ganttChart = new ArrayList<Gantt_Process>();
        //sorting readyQueue based on the arrival time
        sort(SORTING_CRITERIA.ARRIVAL_TIME);
        int currentTime = 0 ;
        int NumOfContinue = 0 ;
        int minArrivalProcessIndex = 0;
        for(int currentProcessIndex = 0 ; currentProcessIndex < readyQueue.size(); currentProcessIndex++)
        {
            Queue.add(readyQueue.get(currentProcessIndex)) ;
        }
        while(!Queue.isEmpty())
        {
            for(int currentProcessIndex = 0 ; currentProcessIndex < Queue.size(); currentProcessIndex++)
            {
                if(Queue.get(currentProcessIndex).getArrivalTime() > currentTime)
                {
                    NumOfContinue++ ;
                    if (NumOfContinue == Queue.size())
                    {
                        Gantt_Process ganttProcess = new Gantt_Process(0, currentTime, Queue.get(minArrivalProcessIndex).getArrivalTime());
                        ganttChart.add(ganttProcess);
                        currentTime = Queue.get(minArrivalProcessIndex).getArrivalTime();
                    }
                }
                else
                {
                    //Create gantt process and add it to gantt chart
                    Gantt_Process ganttProcess = new Gantt_Process(Queue.get(currentProcessIndex).getProcessID(), currentTime,  currentTime + ((Queue.get(currentProcessIndex).getBurstTime()<quantum)?Queue.get(currentProcessIndex).getBurstTime():quantum));
                    ganttChart.add(ganttProcess);
                    //update the current time
                    currentTime += ((Queue.get(currentProcessIndex).getBurstTime()<quantum)?Queue.get(currentProcessIndex).getBurstTime():quantum) ;
                    Queue.get(currentProcessIndex).setBurstTime((Queue.get(currentProcessIndex).getBurstTime()-quantum));
                    if ( Queue.get(currentProcessIndex).getBurstTime() <= 0 )
                        Queue.remove(currentProcessIndex);
                    NumOfContinue = 0;
                }
            }
        }
            return ganttChart;
    }
}
