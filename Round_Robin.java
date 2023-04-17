import java.util.ArrayList;
import java.io.* ;
import java.util.* ;

public class Round_Robin extends  Scheduler
{
    public Round_Robin(ArrayList<Process> readyQueue, int quantum)
    {
        super(readyQueue, quantum);
    }

    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        Vector<Integer> flag2 = new Vector<Integer>() ;
        int currentTime = 0 , NumOfContinue = 0 , minArrivalProcessIndex = 0 , flag = 0 ,size = readyQueue.size();
        ganttChart = new ArrayList<Gantt_Process>();
        //sorting readyQueue based on the arrival time
        sort(SORTING_CRITERIA.ARRIVAL_TIME) ;
        while(readyQueue.size()!=flag)
        {
            for(int currentProcessIndex = 0 ; currentProcessIndex < (readyQueue.size()-flag); currentProcessIndex++)
            {
                if(readyQueue.get(currentProcessIndex).getArrivalTime() > currentTime)
                {
                    NumOfContinue++ ;
                    if (NumOfContinue == readyQueue.size())
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
                    if ( readyQueue.get(currentProcessIndex).getBurstTime() <= 0 )
                    {
                        flag2.add(currentProcessIndex) ;
                    }
                    NumOfContinue = 0 ;
                }
            }
            if(!flag2.isEmpty())
            {
                for(int i=0 ; i<flag2.size() ; i++)
                {
                    int x = flag2.get(i);
                    readyQueue.add(readyQueue.get(flag2.get(i)));
                    readyQueue.remove(x);
                    flag++;
                }
                flag2.clear();
            }
        }
            return ganttChart;
    }
}
