package os;
import java.util.ArrayList;
import java.util.Scanner;

public class FCFS extends  Scheduler
{
 
    private int maxSize;
  
    public FCFS() 
    {
      
    }

   
    public FCFS(ArrayList<Process> readyQueue)
    {
        
        super(readyQueue);
       
    }
    
    public void processInfo()
    {
       
        int ID,ar,bt;
        System.out.println("Enter the information of processes: ");
        for (int i = 0; i < maxSize; i++)
        {
            Scanner sc = new Scanner(System.in);
            System.out.println("enter ID ,ArrrivalTime,BurstTime of p"+(i+1));
            ID=sc.nextInt();
            ar=sc.nextInt();
            bt=sc.nextInt();
            readyQueue.add(new Process(ID, ar, bt));
        }
        sort(SORTING_CRITERIA.valueOf("ARRIVAL_TIME"));
        
        
        
     }
    public  ArrayList<Process> FCFS_Execute()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of processes: "); //Entering the number of processes
        int n = sc.nextInt();
        maxSize = n;
        processInfo();
       return readyQueue;
    }
    public void printInfo()
    {
        System.out.println("Process Id\t\tArrival time\t\tBurst time");
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 0; i < maxSize; i++) 
        {
            System.out.println("    p"+readyQueue.get(i).getProcessID()+"\t\t\t    "+readyQueue.get(i).getArrivalTime()+"\t\t\t    "+readyQueue.get(i).getBurstTime());
        }
    }  
    
    
    @Override
    public  ArrayList<Gantt_Process> get_GanttChart()
    {
        int cumv = readyQueue.get(0).getBurstTime()+readyQueue.get(0).getArrivalTime();
        ganttChart.add(new Gantt_Process(readyQueue.get(0).getProcessID(), readyQueue.get(0).getArrivalTime(), cumv));
        
        for (int i = 1; i < maxSize; i++) {
            if(readyQueue.get(i).getArrivalTime() > readyQueue.get(i-1).getArrivalTime()){
                ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),readyQueue.get(i).getArrivalTime(),readyQueue.get(i).getBurstTime()+ readyQueue.get(i).getArrivalTime()));
            }else{
                ganttChart.add(new Gantt_Process(readyQueue.get(i).getProcessID(),cumv,readyQueue.get(i).getBurstTime()+ cumv));
                cumv += readyQueue.get(i).getBurstTime();
            }
        }
        return ganttChart;
    }
   public void printChart(){
         System.out.println("\tGantt Chart Line");
       System.out.println("-------------------------------------------------------------------------------------");
       System.out.print("time: ");
       if(readyQueue.get(0).getArrivalTime() != 0){
             System.out.print("<"+0+"-"+readyQueue.get(0).getArrivalTime()+"> IDLE | ");
             for (int i = 1; i < maxSize; i++) {
                    if(readyQueue.get(i).getArrivalTime() > readyQueue.get(i-1).getArrivalTime()){
                     System.out.print("<"+(readyQueue.get(i-1).getArrivalTime()+readyQueue.get(i-1).getBurstTime())+"-"+readyQueue.get(i).getArrivalTime()+"> IDLE | ");
                         for (int j = i-1; j < maxSize; j++) {
                             System.out.print(get_GanttChart().get(j).toString());
                         }
                         System.out.println();
                    }else{
                         for (int j = 0; j < maxSize; j++) {
                             System.out.print(get_GanttChart().get(j).toString());
                         }
                         System.out.println();
                    }
        }
       }else{
        for (int i = 1; i < maxSize; i++) {
            if(readyQueue.get(i).getArrivalTime() > readyQueue.get(i-1).getArrivalTime()){
             System.out.print("<"+(readyQueue.get(i-1).getArrivalTime()+readyQueue.get(i-1).getBurstTime())+"-"+readyQueue.get(i).getArrivalTime()+"> IDLE | ");
                 for (int j = i-1; j < maxSize; j++) {
                     System.out.print(get_GanttChart().get(j).toString());
                 }
                 System.out.println();
             }else{
                 for (int j = 0; j < maxSize; j++) {
                     System.out.print(get_GanttChart().get(j).toString());
                 }
                 System.out.println();
             }
        }
       }
   }
    
}
      

 


