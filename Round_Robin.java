import java.util.ArrayList;
import java.util.* ;

public class Round_Robin extends  Scheduler
{
    public Round_Robin(ArrayList<Process> readyQueue, int quantum)
    {
        super(readyQueue, quantum);
    }

    @Override
    public void get_GanttChart()
    {
        Vector<Integer> flag2 = new Vector<>() ;
        int currentTime = 0 , NumOfContinue = 0 , minArrivalProcessIndex = 0 , flag = 0 ,size = readyQueue.size();
        ganttChart = new ArrayList<Gantt_Process>();
        //sorting readyQueue based on the arrival time
        sort_readyQueue(SORTING_CRITERIA.ARRIVAL_TIME); ;
        while(size!=flag)
        {
            for(int currentProcessIndex = 0 ; currentProcessIndex < (size-flag); currentProcessIndex++)
            {
                if(readyQueue.get(currentProcessIndex).getArrivalTime() > currentTime)
                {
                    NumOfContinue++ ;
                    if (NumOfContinue == (size-flag))
                    {
                        Gantt_Process ganttProcess = new Gantt_Process(0, currentTime, readyQueue.get(minArrivalProcessIndex).getArrivalTime());
                        ganttChart.add(ganttProcess);
                        currentTime = readyQueue.get(minArrivalProcessIndex).getArrivalTime();
                    }
                }
                else
                {
                    //Create gantt process and add it to gantt chart
                    Gantt_Process ganttProcess = new Gantt_Process(readyQueue.get(currentProcessIndex).getProcessID(), currentTime,  currentTime + ((readyQueue.get(currentProcessIndex).getBurstTime()<quantum)?readyQueue.get(currentProcessIndex).getBurstTime():quantum));
                    ganttChart.add(ganttProcess);
                    //update the current time
                    currentTime += ((readyQueue.get(currentProcessIndex).getBurstTime()<quantum)?readyQueue.get(currentProcessIndex).getBurstTime():quantum) ;
                    readyQueue.get(currentProcessIndex).setBurstTime((readyQueue.get(currentProcessIndex).getBurstTime()-quantum));
                    if (0 >= readyQueue.get(currentProcessIndex).getBurstTime())
                    {
                        flag2.add(currentProcessIndex) ;
                    }
                    NumOfContinue = 0 ;
                }
            }
            if(!flag2.isEmpty())
            {
                int y =0;
                for(int i=0 ; i < flag2.size() ; i++)
                {
                    int x = flag2.get(i);
                    readyQueue.add(readyQueue.get(flag2.get(i)-y));
                    readyQueue.remove(x-y);
                    flag++;
                    y++;
                }
                flag2.clear();
            }
        }
    }
}
