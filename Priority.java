import java.util.*;
public class Priority extends  Scheduler
{
    private  PriorityQueue<Process> priorityQueue;
    private int currentTime;
    private int newProcessesCount;
    public Priority(ArrayList<Process> readyQueue, boolean preemptive)
    {
        super(readyQueue, preemptive);
        priorityQueue = new PriorityQueue<Process>((t1, t2) -> Integer.compare(t1.getPriority(), t2.getPriority()));
        currentTime = 0;
        newProcessesCount = 0;
        ganttChart = new ArrayList<Gantt_Process>();
    }

    /*
     * @Function Name: update_readyQueue
     *
     * @Description: this function is called at the beginning of each cycle to update  ready queue by removing the
     * new processes that are added to the priority queue
     *
     */
    void update_readyQueue()
    {
        //remove the processes from ready queue by number = newProcessCount which is the number of new processes added to
        //priority queue
        for(int i=0;i< newProcessesCount;i++)
            readyQueue.remove(0);
    }
    /*
     * @Function Name: update_priorityQueue
     *
     * @Description: this function is called at the beginning of each cycle to update  priority queue by adding to the queue
     * the processes that have arrival time less than or equal to the current time (processes have already arrived)
     * each time we add process to priority queue we increment newProcessCount
     */
    void update_priorityQueue()
    {
        for(Process p:readyQueue)
        {
            if(p.getArrivalTime()<= currentTime)
            {
                priorityQueue.add(p);
                newProcessesCount++;
            }
            else /*The ready queue is sorted so the first process that has arrival time greater than current time
                   then all processes after it have arrival time greater than current time, so we break */
                break;
        }
    }
    /*
     * @Function Name: non_preemptive_priority
     *
     * @Description: this function execute the priority scheduling algorithm but with no preemption and add gantt process
     * to the gantt chart
     */
    public void non_preemptive_priority()
    {
            //Check each cycle whether ready queue or priority queue are empty, if both are empty then break the loop
            while(!readyQueue.isEmpty() || !priorityQueue.isEmpty())
            {
                Gantt_Process ganttProcess = new Gantt_Process();
                newProcessesCount = 0; //Initialize the number of new processes that will be added to priority queue
                update_priorityQueue();
                update_readyQueue();

                /*If the priority queue is empty then this means the IDLE Task that means the cpu is idle and do nothing
                 *because the priority queue is empty means that we can't add any processes from ready queue to priority queue
                 * because their arrival time is greater than current time
                 */
                if(priorityQueue.isEmpty())
                {
                    ganttProcess.processId = 0; // The IDLE task is assumed with id = 0
                    ganttProcess.startTime = currentTime; // The IDLE task begin from the current time
                    currentTime+= readyQueue.get(0).getArrivalTime()-currentTime; //The IDLE Task will continue till the first arrival time in ready queue(sorted)
                    ganttProcess.endTime = currentTime;
                }
                else //If the priority queue has processes then it means that cpu can execute them
                {
                    ganttProcess.processId = priorityQueue.peek().getProcessID(); // The gantt process will have ID equal to the current process
                    ganttProcess.startTime = currentTime; //The start time of gantt process begins with the current time
                    currentTime+= priorityQueue.peek().getBurstTime(); //The end time of gantt process will be the burst time of current process (No preemption)
                    ganttProcess.endTime = currentTime;
                    priorityQueue.poll(); // delete the executed process from the priority queue
                }
                ganttChart.add(ganttProcess); // add the gantt process to the gantt chart
            }
            priorityQueue.clear(); //Clear the priority queue at the end of this non-preemptive operation
    }

    /*
     * @Function Name: preemptive_priority
     *
     * @Description: this function execute the priority scheduling algorithm but with allowing to preemption and add gantt process
     * to the gantt chart
     */
    public void preemptive_priority()
    {
        
    }

    /*
     * @Function Name: get_GanttChart
     *
     * @Description: This function implements the get_GanttChart inherited from Scheduler class
     *
     * @Return: This functions returns gantt chart which is arraylist of gantt process where each item in arraylist has
     * process ID, start time, end time
     */
    @Override
    public ArrayList<Gantt_Process>get_GanttChart()
    {
        /* Sort the processes according to its arrival time where the type of sort is merge sort to be  stable sort
         * so that if there are multiple processes of same arrival time then process ID will be used to break the tie
        */
        sort(SORTING_CRITERIA.ARRIVAL_TIME);
        //Check false preemption so non-preemptive priority scheduling will be executed
        if(preemptive == false )
            non_preemptive_priority();
        else if(preemptive == true) //Check true preemption so preemptive priority scheduling will be executed
            preemptive_priority();

        return ganttChart; //return the gantt chart
    }
}
