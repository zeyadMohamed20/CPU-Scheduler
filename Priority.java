/***************************************************************************************************************
 * Module: priority Scheduler
 *
 * File Name: Priority.java
 *
 * Description: Implementation of priority scheduling with the two types preemptive and non-preemptive
 *
 * Created by: Zeyad Mohamed Abd El-Hamid
 *****************************************************************************************************************/

import java.util.*;

public class Priority extends  Scheduler
{
    private final PriorityQueue<Process> priorityQueue;
    private int currentTime;
    private int currentProcessIndex;
    public Priority(ArrayList<Process> readyQueue, boolean preemptive)
    {
        super(readyQueue, preemptive);
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
        currentTime = 0;
        currentProcessIndex = 0;
        ganttChart = new ArrayList<>();
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
        for(int i = currentProcessIndex; i<readyQueue.size(); i++)
        {
            if(readyQueue.get(i).getArrivalTime()<= currentTime)
            {
                priorityQueue.add(readyQueue.get(i));
                currentProcessIndex++;
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
        while(!(currentProcessIndex >= readyQueue.size())  || !priorityQueue.isEmpty())
        {
            Gantt_Process ganttProcess = new Gantt_Process();
            update_priorityQueue();

            /*If the priority queue is empty then this means the IDLE Task that means the cpu is idle and do nothing
             *because the priority queue is empty means that we can't add any processes from ready queue to priority queue
             * because their arrival time is greater than current time
             */
            if(priorityQueue.isEmpty())
            {
                ganttProcess.processId = 0; // The IDLE task is assumed with id = 0
                ganttProcess.startTime = currentTime; // The IDLE task begin from the current time
                currentTime+= readyQueue.get(currentProcessIndex).getArrivalTime()-currentTime; //The IDLE Task will continue till the first arrival time in ready queue(sorted)
                ganttProcess.endTime = currentTime;
            }
            else //If the priority queue has processes then it means that cpu can execute them
            {
                ganttProcess.processId = priorityQueue.peek().getProcessID(); // The gantt process will have ID equal to the current process
                ganttProcess.startTime = currentTime; //The start time of gantt process begins with the current time
                currentTime+= priorityQueue.peek() != null ? priorityQueue.peek().getBurstTime() : 0; //The end time of gantt process will be the burst time of current process (No preemption)
                ganttProcess.endTime = currentTime;
                priorityQueue.poll(); // delete the executed process from the priority queue
            }
            ganttChart.add(ganttProcess); // add the gantt process to the gantt chart
        }
    }

    /*
     * @Function Name: preemptive_priority
     *
     * @Description: this function execute the priority scheduling algorithm but with allowing to preemption and add gantt process
     * to the gantt chart
     */
    public void preemptive_priority()
    {
        int totalProcessTime;
        int executionTime;
        boolean processPreemption;
        //Check each cycle whether ready queue or priority queue are empty, if both are empty then break the loop
        while(!(currentProcessIndex >= readyQueue.size()) || !priorityQueue.isEmpty())
        {
            Gantt_Process ganttProcess = new Gantt_Process();
            processPreemption = false;

            update_priorityQueue();

            /*If the priority queue is empty then this means the IDLE Task that means the cpu is idle and do nothing
             *because the priority queue is empty means that we can't add any processes from ready queue to priority queue
             * because their arrival time is greater than current time
             */
            if(priorityQueue.isEmpty())
            {
                ganttProcess.processId = 0; // The IDLE task is assumed with id = 0
                ganttProcess.startTime = currentTime; // The IDLE task begin from the current time
                currentTime+= readyQueue.get(currentProcessIndex).getArrivalTime()-currentTime; //The IDLE Task will continue till the first arrival time in ready queue(sorted)
                ganttProcess.endTime = currentTime;
            }
            else //If the priority queue has processes then it means that cpu can execute them
            {
                //The end time if the processes has been executed without preemption
                totalProcessTime = currentTime + priorityQueue.peek().getBurstTime();
                for(int i = currentProcessIndex; i<readyQueue.size(); i++)
                {
                    /*Find the nearest process in the ready queue that satisfy two conditions:
                      a) Its arrival time is less than the end time of the current process
                      b) Its priority is greater than the priority of the current process (Higher priority means less value)
                      If there is a process in ready queue satisfy these conditions, it means that this process can preempt
                      the current process
                     */
                    if(totalProcessTime > readyQueue.get(i).getArrivalTime() && (priorityQueue.peek() != null ? priorityQueue.peek().getPriority() : 0) > readyQueue.get(i).getPriority())
                    {
                        ganttProcess.processId = priorityQueue.peek() != null ? priorityQueue.peek().getProcessID() : 0; //The gantt process will have ID equal to the current process
                        ganttProcess.startTime = currentTime; //The start time of gantt process begins with the current time
                        executionTime = readyQueue.get(i).getArrivalTime() - currentTime; //The current process will execute till the arrival time of the preempted process
                        currentTime+= executionTime;
                        ganttProcess.endTime = currentTime;
                        assert priorityQueue.peek() != null;
                        priorityQueue.peek().setBurstTime((priorityQueue.peek() != null ? priorityQueue.peek().getBurstTime() : 0) -executionTime); //Calculate the remaining burst time of the current process by decrementing the execution time from burst time
                        processPreemption = true; //processPreemption happens
                        break; //Break from the loop
                    }
                }
                // If no preemption occur then this means that the current process can be completely executed
                if(!processPreemption)
                {
                    ganttProcess.processId = priorityQueue.peek() != null ? priorityQueue.peek().getProcessID() : 0;//The gantt process will have ID equal to the current process
                    ganttProcess.startTime = currentTime; //The start time of gantt process begins with the current time
                    currentTime+= priorityQueue.peek() != null ? priorityQueue.peek().getBurstTime() : 0; //The end time of gantt process will be the burst time of current process (No preemption)
                    ganttProcess.endTime = currentTime;
                    priorityQueue.poll(); //Remove the process from the priority queue as it is completely executed
                }
            }
            ganttChart.add(ganttProcess); // add the gantt process to the gantt chart
        }
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
    public void get_GanttChart()
    {
        /* Sort the processes according to its arrival time when the type of sort is merge sort to be  stable sort
         * so that if there are multiple processes of same arrival time then process ID will be used to break the tie
         */
        sort_readyQueue(SORTING_CRITERIA.ARRIVAL_TIME);
        //Check false preemption so non-preemptive priority scheduling will be executed
        if(!preemptive)
            non_preemptive_priority();
        else //Check true preemption so preemptive priority scheduling will be executed
            preemptive_priority();

    }
}
