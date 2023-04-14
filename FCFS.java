package program;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
enum SORTING_CRITERIA2
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
    public void sortGantt(SORTING_CRITERIA2 option)
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


    
 public ArrayList<Gantt_Process>get_GanttChart(){
         IDLE = new ArrayList<Gantt_Process>();
         merge = new ArrayList<Gantt_Process>();
         ganttChart = new ArrayList<Gantt_Process>();
     
         sort(SORTING_CRITERIA.valueOf("ARRIVAL_TIME"));
        int c=0;
        int cumv1 = readyQueue.get(0).getBurstTime()+readyQueue.get(0).getArrivalTime();
         int cumv2 = readyQueue.get(0).getBurstTime()+readyQueue.get(0).getArrivalTime();
         int cumv3 = readyQueue.get(0).getBurstTime()+readyQueue.get(0).getArrivalTime();
        if(readyQueue.get(0).getArrivalTime() > 0){
            IDLE.add(new Gantt_Process(0,0, readyQueue.get(0).getArrivalTime()));
            ganttChart.add(new Gantt_Process(readyQueue.get(0).getProcessID(),readyQueue.get(0).getArrivalTime(),cumv1));
             for (int i = 1; i < readyQueue.size(); i++) 
             {
                 c = readyQueue.get(i-1).getArrivalTime()+readyQueue.get(i-1).getBurstTime();
                 
                 if(readyQueue.get(i).getArrivalTime()> c)
                 {
                     IDLE.add(new Gantt_Process(0,c,readyQueue.get(i).getArrivalTime()));
                     
                     cumv1 = readyQueue.get(i).getArrivalTime();
                     ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv1 ,readyQueue.get(i).getBurstTime()+ cumv1));
                   
                    
                 }else if(readyQueue.get(i).getArrivalTime() == c){
                     cumv3 = readyQueue.get(i).getArrivalTime();
                     ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv3 ,readyQueue.get(i).getBurstTime()+ cumv3));
                 }
                 else{
                     cumv2 = ganttChart.get(i-1).getEndTime();
                     ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv2,readyQueue.get(i).getBurstTime()+ cumv2));
                  

                 }

             }
        }else{
              ganttChart.add(new Gantt_Process(readyQueue.get(0).getProcessID(),readyQueue.get(0).getArrivalTime(),cumv1));
             for (int i = 1; i < readyQueue.size(); i++) 
             {
                 c = readyQueue.get(i-1).getArrivalTime()+readyQueue.get(i-1).getBurstTime();
                 
                 if(readyQueue.get(i).getArrivalTime()> c)
                 {
                     IDLE.add(new Gantt_Process(0,c,readyQueue.get(i).getArrivalTime()));
                     
                     cumv1 = readyQueue.get(i).getArrivalTime();
                     ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv1 ,readyQueue.get(i).getBurstTime()+ cumv1));
                   
                    
                 }else if(readyQueue.get(i).getArrivalTime() == c){
                     cumv3 = readyQueue.get(i).getArrivalTime();
                     ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv3 ,readyQueue.get(i).getBurstTime()+ cumv3));
                 }
                 else{
                     cumv2 = ganttChart.get(i-1).getEndTime();
                     ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv2,readyQueue.get(i).getBurstTime()+ cumv2));
                  

                 }

             }
        }
        for (int i = 0; i < IDLE.size(); i++) {
            merge.add(IDLE.get(i));
            
        }
        for (int i = 0; i < ganttChart.size(); i++) {
            merge.add(ganttChart.get(i));
            
        }
         sortGantt(SORTING_CRITERIA2.valueOf("START_TIME"));
        return merge;
    }
    

}
    

      

 


