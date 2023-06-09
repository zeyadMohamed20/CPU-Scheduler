import java.util.ArrayList;

public class SJF extends  Scheduler
{
	private int currentTime = 0, currentProcessIndex = 0;
	private ArrayList<Process>myReadyQueue = new ArrayList<Process>();
	
    public SJF(ArrayList<Process> readyQueue, boolean preemptive)
    {
        super(readyQueue, preemptive);
    }
	
    /*
     * @Function Name: update_ReadyQueue
     * 
     * @Description: this function is called after adding each gantt process to gantt chart to 
     * modify the burst times of the process in the readyQueue. When the burst time of a process
     * in the ready queue equals to zero, the process is deleted from ready queue.
     * 
     */
    public void update_ReadyQueue(int currentProcessIndex, boolean preemptionOption)
    {    	    	  	     
    	//If the scheduler used was SJF without preemption
    	if(!preemptionOption)    	
    		myReadyQueue.remove(currentProcessIndex);    	
    	//If the scheduler used was SJF with preemption
    	else
    	{
    		//Decrement the burst time by 1
    		if(myReadyQueue.get(currentProcessIndex).getBurstTime() > 0)
    			myReadyQueue.get(currentProcessIndex).setBurstTime(myReadyQueue.get(currentProcessIndex).getBurstTime() - 1);
    		if(myReadyQueue.get(currentProcessIndex).getBurstTime() == 0)
    			myReadyQueue.remove(currentProcessIndex);
    	}
    }
    
    
    /*
     * @Function Name: choose_ProcessIndex
     * 
     * @Description: choose the right process from the ready queue to be executed based on 
     * SJF scheduling.
     */
   public void choose_ProcessIndex()
   {
	   int i = 0, minBurstProcessIndex = 0;
	   while(i < myReadyQueue.size() && myReadyQueue.get(i).getArrivalTime() <= currentTime)
	   {		    
	   		if(myReadyQueue.get(i).getBurstTime() < myReadyQueue.get(minBurstProcessIndex).getBurstTime())
	   		{
	   			minBurstProcessIndex = i;
	   		}    	
	   		
	   		//if the two processes have the same burst time then choose the process that has smaller id
	   		else if(myReadyQueue.get(i).getBurstTime() == myReadyQueue.get(minBurstProcessIndex).getBurstTime())
	   		{	   		
	   			if(myReadyQueue.get(minBurstProcessIndex).getProcessID() > myReadyQueue.get(i).getProcessID())
	   				minBurstProcessIndex = i;
	   		}
	   		i++;
	   }
	   currentProcessIndex = minBurstProcessIndex;
   }
  
   
   /*
    * @Function Name: merge_ToGanttChart
    * 
    * @return: ArrayList<Gantt_Process>
    * 
    * @input parameter: arrayList of gantt processes, each one has a time slice of 1 sec. 
    * (for example: if p2 is executed from time 0 -> 3 sec, then this arrayList must contain
    *  <0,1>p2 | <1,2>p2, <2,3>p3).
    * 
    * @output: arrayList of gantt processes (the final version) by merging the input arrayList
    * 
    * @Description: it is useful in SJF (with preemption option). it merges all the neighboring
    * time slices of the same process 
    */
   public ArrayList<Gantt_Process> merge_ToGanttChart(ArrayList<Gantt_Process>tempGanttChart)
   {
	   int startMergedTimeSlice = 0, endMergedTimeSlice, currentProcessID = tempGanttChart.get(0).getProcessId();
	   ArrayList<Gantt_Process>myGanttProcess = new ArrayList<Gantt_Process>();
	   for (int i = 1; i < tempGanttChart.size(); i++)
	   {
		   if(tempGanttChart.get(i).getProcessId() != tempGanttChart.get(i - 1).getProcessId())
		   {			   
			   endMergedTimeSlice = tempGanttChart.get(i).startTime;
			   Gantt_Process ganttProcess = new Gantt_Process(currentProcessID, startMergedTimeSlice, endMergedTimeSlice);
			   myGanttProcess.add(ganttProcess);
			   currentProcessID = tempGanttChart.get(i).getProcessId();
			   startMergedTimeSlice = endMergedTimeSlice;
			   if(i == tempGanttChart.size() - 1)
			   {
				   ganttProcess = new Gantt_Process(currentProcessID, startMergedTimeSlice, tempGanttChart.get(i).endTime);
				   myGanttProcess.add(ganttProcess);
			   }
		   }
		   
		   else
		   {
			   if(i == tempGanttChart.size() - 1)
			   {
				   Gantt_Process ganttProcess = new Gantt_Process(currentProcessID, startMergedTimeSlice, tempGanttChart.get(i).endTime);
				   myGanttProcess.add(ganttProcess);
			   }
		   }
	   }
	   return myGanttProcess;		
   }
   
   
   
   /*
    * @Function Name: ready_QueueDeepClone
    * 
    * @return: ArrayList<Process>
    * 
    * @Description: Clone the readyQueue deeply not to modify the original one
    */
   private ArrayList<Process> ready_QueueDeepClone()
   {
   	for(int i = 0; i < readyQueue.size(); i++)
   	{
   		Process myProcess = new Process();
   		myProcess.setArrivalTime(readyQueue.get(i).getArrivalTime());
   		myProcess.setBurstTime(readyQueue.get(i).getBurstTime());
   		myProcess.setPriority(readyQueue.get(i).getPriority());
   		myProcess.setProcessID(readyQueue.get(i).getProcessID());
   		myReadyQueue.add(myProcess);
   	}
   	return myReadyQueue;
   }
   
   
   
    
    @Override
    public void get_GanttChart()
    {
    	ganttChart = new ArrayList<Gantt_Process>();
    	//sorting readyQueue based on the arrival time
    	sort_readyQueue(SORTING_CRITERIA.ARRIVAL_TIME);
    	
    	this.ready_QueueDeepClone();
    	
    	//If the used scheduler was SJF without preemption
        if(preemptive == false )
        {
        	while(!myReadyQueue.isEmpty())
        	{        		        	
            	choose_ProcessIndex(); 
            	//If the arrival time of the process is greater than the current time, then add ideal task
            	if(myReadyQueue.get(currentProcessIndex).getArrivalTime() > currentTime)
            	{
            		Gantt_Process ganttProcess = new Gantt_Process(0, currentTime, myReadyQueue.get(currentProcessIndex).getArrivalTime());
            		ganttChart.add(ganttProcess);
            		currentTime = myReadyQueue.get(currentProcessIndex).getArrivalTime();
            	}
            	else
            	{
            		//Create gantt process and add it to gantt chart
                	Gantt_Process ganttProcess = new Gantt_Process(myReadyQueue.get(currentProcessIndex).getProcessID(), currentTime, currentTime + myReadyQueue.get(currentProcessIndex).getBurstTime());
                	ganttChart.add(ganttProcess);
                	//update the current time
                	currentTime += myReadyQueue.get(currentProcessIndex).getBurstTime();
                	//update the readyQueue by eliminating the executed time of the process from the burst time
                	update_ReadyQueue(currentProcessIndex, preemptive);	
            	}            	            	         
        	}        	        		
        }
        
        //If the used scheduler was SJF with preemption
        else if(preemptive == true)
        {
        	//This is array list to add gantt processes in it, but each one has 1 sec time slice
        	ArrayList<Gantt_Process>tempGanttChart = new ArrayList<Gantt_Process>();
        	
        	while(!myReadyQueue.isEmpty())
        	{
        		choose_ProcessIndex();
            	//If the arrival time of the process is greater than the current time, then add ideal task
            	if(myReadyQueue.get(currentProcessIndex).getArrivalTime() > currentTime)
            	{        		     
            		//add temp gantt process of time slice 1 sec to temp gant chart 
        			Gantt_Process tempGanttProcess = new Gantt_Process(0, currentTime, currentTime + 1);
        			currentTime++;
        			tempGanttChart.add(tempGanttProcess);        		             		
            	}
            	else
            	{
            		//Create gantt process and add it to gantt chart
                	Gantt_Process tempGanttProcess = new Gantt_Process(myReadyQueue.get(currentProcessIndex).getProcessID(), currentTime, currentTime + 1);
                	tempGanttChart.add(tempGanttProcess);
                	//update the current time
                	currentTime ++;
                	//update the readyQueue by eliminating the executed time of the process from the burst time
                	update_ReadyQueue(currentProcessIndex, preemptive);	
            	} 
        	}        	
        	ganttChart = merge_ToGanttChart(tempGanttChart);      	
        }
    }
}
