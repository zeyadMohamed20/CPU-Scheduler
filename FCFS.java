import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
enum GANTT_SORTING_CRITERIA
{
    ID,
    START_TIME,
    END_TIME
}
public class FCFS extends  Scheduler
{
    private ArrayList<Gantt_Process> IDLE;
    private ArrayList<Gantt_Process> merge;
   
    public FCFS(ArrayList<Process> readyQueue)
    {
        
        super(readyQueue);
    }
    //sorting based on start time of gantt processes line
    public void sortGantt(GANTT_SORTING_CRITERIA option)
    {
        Collections.sort(merge, new Comparator<Gantt_Process>()
        {
            @Override
            public int compare(Gantt_Process gp1, Gantt_Process gp2)
            {
                switch(option)
                {
                    case ID:
                        return gp1.getProcessId()- gp2.getProcessId();
                    case START_TIME:
                        return gp1.getStartTime() - gp2.getStartTime();
                    case END_TIME:
                        return gp1.getEndTime()- gp2.getEndTime();
                    default:
                        return 0;
                }
            }
        });

    }


    
 public void get_GanttChart(){
         IDLE = new ArrayList<Gantt_Process>();
         merge = new ArrayList<Gantt_Process>();
         ganttChart = new ArrayList<Gantt_Process>();
     
        sort_readyQueue(SORTING_CRITERIA.ARRIVAL_TIME);
        int a1 = 0;
        int a2 = readyQueue.get(0).getArrivalTime();
        int a3 =  a2+readyQueue.get(0).getBurstTime();
        
        if(a2 > a1){
            IDLE.add(new Gantt_Process(0, a1, a2));
            ganttChart.add(new Gantt_Process(readyQueue.get(0).getProcessID(),a2,a3));
            for (int i = 1; i < readyQueue.size(); i++) {
                a1 = ganttChart.get(i-1).getEndTime();
                a2 = readyQueue.get(i).getArrivalTime();
      
                if(a2 > a1){
                    IDLE.add(new Gantt_Process(0, a1, a2));
                    ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(), a2, a2+readyQueue.get(i).getBurstTime())); 
                }else{
                   ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(), a3, a3+readyQueue.get(i).getBurstTime())); 
                
                }   
                 a3 = ganttChart.get(i).getEndTime();
            }
        }else{
            ganttChart.add(new Gantt_Process(readyQueue.get(0).getProcessID(),a2,a3));
            for (int i = 1; i < readyQueue.size(); i++) {
                a1 = ganttChart.get(i-1).getEndTime();
                a2 = readyQueue.get(i).getArrivalTime();
       
                if(a2 > a1){
                    IDLE.add(new Gantt_Process(0, a1, a2));
                    ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(), a2, a2+readyQueue.get(i).getBurstTime())); 
                }else{ 
                   ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(), a3, a3+readyQueue.get(i).getBurstTime())); 
                }  
               a3 = ganttChart.get(i).getEndTime();
            }
        }
       
 
        for (int i = 0; i < IDLE.size(); i++) {
            merge.add(IDLE.get(i));
            
        }
        for (int i = 0; i < ganttChart.size(); i++) {
            merge.add(ganttChart.get(i));
            
        }
        ganttChart = merge;
        sortGantt(GANTT_SORTING_CRITERIA.START_TIME);
    }
}
