public class Gantt_Process
{
    int processId;
    int startTime;
    int endTime;

    public Gantt_Process(int processId, int startTime, int endTime)
    {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Gantt_Process()
    {
        this.processId = 1;
        this.startTime = 0;
        this.endTime = 0;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
