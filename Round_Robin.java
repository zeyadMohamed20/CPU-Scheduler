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
        ganttChart = new ArrayList<Gantt_Process>();
        //sorting readyQueue based on the arrival time
        sort(SORTING_CRITERIA.ARRIVAL_TIME);
        int currentTime = 0 ;
        int NumOfContinue = 0 ;
        int minArrivalProcessIndex = 0;
        while(!readyQueue.isEmpty())
        {
            for(int currentProcessIndex = 0 ; currentProcessIndex < readyQueue.size(); currentProcessIndex++)
            {
                if(readyQueue.get(currentProcessIndex).getArrivalTime() > currentTime)
                {
                    NumOfContinue++ ;
                    if (NumOfContinue == readyQueue.size() )
                    {
                        Gantt_Process ganttProcess = new Gantt_Process(0, currentTime, readyQueue.get(minArrivalProcessIndex).getArrivalTime());
                        ganttChart.add(ganttProcess);
                        currentTime = readyQueue.get(minArrivalProcessIndex).getArrivalTime();
                    }
                }
                else
                {
                    //Create gantt process and add it to gantt chart
                    Gantt_Process ganttProcess = new Gantt_Process(readyQueue.get(currentProcessIndex).getProcessID(), currentTime,  currentTime + quantum);
                    ganttChart.add(ganttProcess);
                    //update the current time
                    currentTime += quantum ;
                    readyQueue.get(currentProcessIndex).setBurstTime((readyQueue.get(currentProcessIndex).getBurstTime()-quantum));
                    if ( readyQueue.get(currentProcessIndex).getBurstTime() <= 0 )
                        readyQueue.remove(currentProcessIndex);
                    NumOfContinue = 0;
                }
            }
        }
            return ganttChart;
    }
}
